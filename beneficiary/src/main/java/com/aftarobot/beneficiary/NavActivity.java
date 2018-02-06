package com.aftarobot.beneficiary;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.aftarobot.mlibrary.api.ChainListAPI;
import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.InsuranceCompany;
import com.aftarobot.mlibrary.data.Policy;
import com.aftarobot.mlibrary.util.PolicyBag;
import com.aftarobot.mlibrary.util.SharedPrefUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class NavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Beneficiary beneficiary, chainBeneficiary;
    Toolbar toolbar;
    List<Policy> policies = new ArrayList<>();
    ChainListAPI chainListAPI;
    TextView txtCount;
    RecyclerView recyclerView;

    public static final String TAG = NavActivity.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        txtCount = findViewById(R.id.txtCount);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        beneficiary = SharedPrefUtil.getBeneficiary(this);
        Log.e(TAG, "onCreate: beneficiary: ".concat(GSON.toJson(beneficiary)) );
        getSupportActionBar().setTitle("Beneficiary Services");
        getSupportActionBar().setSubtitle(beneficiary.getFullName());

        chainListAPI = new ChainListAPI(this);
        setup();
        index = 0;
        controlPolicies();
    }

    int index;

    private void controlPolicies() {
        if (beneficiary.getPolicies() != null) {
            if (index < beneficiary.getPolicies().size()) {
                String[] strings = beneficiary.getPolicies().get(index).split("#");
                getPolicy(strings[1]);
            } else {
                setList();
                showSnackbar("Found details for ".concat(String.valueOf(
                        beneficiary.getPolicies().size()).concat(" policies")),"Ok","green");
            }
        } else {
            showError("Beneficiary has no policies");
        }
    }
    List<PolicyBag> bags;
    PolicySummaryAdapter adapter;

    private void setList() {
        bags = new ArrayList<>();
        Set<String> mset = map.keySet();
        for (String policyNumber: mset) {
            PolicyBag bag = map.get(policyNumber);
            bags.add(bag);
        }
        if (adapter == null) {
            adapter = new PolicySummaryAdapter(bags, new PolicySummaryAdapter.PolicyBagListener() {
                @Override
                public void onClaimCheck(PolicyBag bag) {
                    checkClaim(bag);
                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        txtCount.setText(String.valueOf(beneficiary.getPolicies().size()));

    }
    Claim claim;
    private void checkClaim(final PolicyBag bag) {
        chainListAPI.getClaims(new ChainListAPI.ClaimsListener() {
            @Override
            public void onResponse(List<Claim> claims) {
                if (!claims.isEmpty()) {
                    boolean isFound = false;

                    for (Claim c: claims) {
                        String[] strings = c.getPolicy().split("#");
                        String policyNum = strings[1];
                        if (policyNum.equalsIgnoreCase(bag.getPolicy().getPolicyNumber())) {
                            claim = c;
                            isFound = true;
                        }
                    }
                    if (isFound) {
                        onClaimFound(claim);
                    } else {
                        showSnackbar("No Claims found for ".concat(bag.getPolicy().getPolicyNumber()),"close", "cyan");
                    }

                } else {
                    showSnackbar("No Claims found on the Blockhain","close", "cyan");
                }
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }
    private void onClaimFound(Claim claim) {
        AlertDialog.Builder x = new AlertDialog.Builder(this);
        x.setTitle("Claim Found")
                .setMessage("A claim against this Policy has been registered.\n\n"
                .concat(claim.getClaimId()).concat("\n").concat(claim.getDateTime()))
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
        showSnackbar("Claim found: ".concat(claim.getClaimId()),"OK","green");

    }
    private void getPolicy(String policyNumber) {
        showSnackbar("Getting policy details: ".concat(policyNumber), "OK","yellow");
        chainListAPI.getPolicy(policyNumber, new ChainListAPI.PolicyListener() {
            @Override
            public void onResponse(List<Policy> list) {
                if (!list.isEmpty()) {
                    policies.add(list.get(0));
                    Log.w(TAG, "onResponse: policy found: ".concat(GSON.toJson(list.get(0))) );
                    getClientAndCompany(list.get(0));
                }

            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }
    private void getClientAndCompany(final Policy policy) {
        String[] strings = policy.getInsuranceCompany().split("#");
        final String companyID = strings[1];
        String[] strings2 = policy.getClient().split("#");
        final String clientID = strings2[1];

        chainListAPI.getClient(clientID, new ChainListAPI.ClientListener() {
            @Override
            public void onResponse(List<Client> clients) {
                if (!clients.isEmpty()) {
                    final Client client = clients.get(0);
                    Log.w(TAG, "onResponse: client found: ".concat(GSON.toJson(client)) );
                    chainListAPI.getInsuranceCompany(companyID, new ChainListAPI.CompanyListener() {
                        @Override
                        public void onResponse(List<InsuranceCompany> companies) {
                            if (!companies.isEmpty()) {
                                InsuranceCompany x = companies.get(0);
                                Log.e(TAG, "onResponse: company found: ".concat(GSON.toJson(x)) );
                                PolicyBag bag = new PolicyBag(policy,client,x);
                                map.put(policy.getPolicyNumber(),bag);
                            }
                            index++;
                            controlPolicies();
                        }

                        @Override
                        public void onError(String message) {
                            showError(message);
                            index++;
                            controlPolicies();
                        }
                    });
                }
            }

            @Override
            public void onError(String message) {

            }
        });

    }
    private HashMap<String,PolicyBag> map = new HashMap<>();

    private void setup() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    Snackbar snackbar;

    private void showSnackbar(String message, String action, String color) {
        snackbar = Snackbar.make(toolbar, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }
    private void showError(String message) {
        snackbar = Snackbar.make(toolbar, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor("red"));
        snackbar.setAction("Error", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

}
