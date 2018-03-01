package com.aftarobot.client;

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

import com.aftarobot.mlibrary.api.ChainDataAPI;
import com.aftarobot.mlibrary.api.ChainListAPI;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.InsuranceCompany;
import com.aftarobot.mlibrary.data.Policy;
import com.aftarobot.mlibrary.util.PolicyBag;
import com.aftarobot.mlibrary.util.SharedPrefUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ClientMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = ClientMainActivity.class.getSimpleName();
    Client client;
    ChainListAPI chainListAPI;
    List<Policy> policies;
    Toolbar toolbar;
    TextView txtCount;
    RecyclerView recyclerView;
    DrawerLayout drawer;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("OneConnect Blockchain");

        client = SharedPrefUtil.getClient(this);
        getSupportActionBar().setSubtitle(client.getFullName());
        chainListAPI = new ChainListAPI(this);
        setup();
        getPolicies();


    }

    private void getPolicies() {
        showSnackbar("Loading policies ...", "ok", "cyan");
        fab.setAlpha(0.3f);
        fab.setEnabled(false);
        chainListAPI.getPoliciesByClientId(client.getIdNumber(), new ChainListAPI.PolicyListener() {
            @Override
            public void onResponse(List<Policy> list) {
                Log.i(TAG, "onResponse: found policies: " + list.size());
                showSnackbar("Found policies: " + list.size() + ", getting details ...", "ok", "green");
                fab.setAlpha(1.0f);
                fab.setEnabled(true);
                policies = list;
                index = 0;
                controlPolicies();
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    private void setList() {
        Log.d(TAG, "setList: .....................bags : ".concat(GSON.toJson(bags)));
        policySummaryAdapter = new PolicySummaryAdapter(bags, new PolicySummaryAdapter.PolicyBagListener() {
            @Override
            public void onClaimCheck(PolicyBag bag) {
                confirm(bag.getPolicy());
            }
        });

        recyclerView.setAdapter(policySummaryAdapter);
        txtCount.setText(String.valueOf(policies.size()));
    }

    private void confirm(final Policy policy) {
        AlertDialog.Builder x = new AlertDialog.Builder(this);
        x.setTitle("Policy Claim")
                .setMessage("Do you want to start a Claim Process against this Policy? \n\n"
                        .concat(policy.getPolicyNumber()))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startClaim(policy);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    ChainDataAPI chainDataAPI;

    private void startClaim(final Policy policy) {
        showSnackbar("Starting claims process ...", "ok", "yellow");
        Claim claim = new Claim();
        claim.setClaimId(policy.getPolicyNumber());
        claim.setAmount(policy.getAmount());
        claim.setApproved(false);
        claim.setClient("resource:com.oneconnect.insurenet.Client#".concat(client.getIdNumber()));
        claim.setPolicyNumber(policy.getPolicyNumber());
        claim.setPolicy("resource:com.oneconnect.insurenet.Policy#".concat(policy.getPolicyNumber()));
        claim.setDateTime(sdf.format(new Date()));
        String[] strings = policy.getInsuranceCompany().split("#");
        claim.setCompanyId(strings[1]);

        chainDataAPI.submitClaim(claim, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                showSnackbar("Claims process started for: ".concat(policy.getPolicyNumber()), "ok", "green");
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.client_main, menu);
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
        snackbar.setAction("error", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    private void setup() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        txtCount = findViewById(R.id.txtCount);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPolicies();
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());

    int index;

    private void controlPolicies() {
        Log.d(TAG, "controlPolicies: index " + index + " policies: " + policies.size());
        if (index < policies.size()) {
            getClientAndCompany(policies.get(index));
        } else {
            bags = new ArrayList<>();
            for (Map.Entry<String, PolicyBag> entry: map.entrySet()) {
                bags.add(entry.getValue());
            }
            setList();
            showSnackbar("Found details for ".concat(String.valueOf(
                    policies.size()).concat(" policies")), "Ok", "green");
        }
    }

    List<PolicyBag> bags;
    PolicySummaryAdapter policySummaryAdapter;

    private void getClientAndCompany(final Policy policy) {
        Log.w(TAG, "getClientAndCompany: policy: ".concat(GSON.toJson(policy)) );
        String[] strings = policy.getInsuranceCompany().split("#");
        final String companyID = strings[1];
        String[] strings2 = policy.getClient().split("#");
        final String clientID = strings2[1];

        showSnackbar("Loading policy details","ok","cyan");
        chainListAPI.getClient(clientID, new ChainListAPI.ClientListener() {
            @Override
            public void onResponse(List<Client> clients) {
                if (!clients.isEmpty()) {
                    final Client client = clients.get(0);
                    Log.w(TAG, "onResponse: client found: ".concat(GSON.toJson(client)));
                    chainListAPI.getInsuranceCompany(companyID, new ChainListAPI.CompanyListener() {
                        @Override
                        public void onResponse(List<InsuranceCompany> companies) {
                            if (!companies.isEmpty()) {
                                InsuranceCompany insuranceCompany = companies.get(0);
                                Log.e(TAG, "onResponse: company found: ".concat(GSON.toJson(insuranceCompany)));
                                PolicyBag bag = new PolicyBag(policy, client, insuranceCompany);
                                map.put(policy.getPolicyNumber(), bag);
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
                showError(message);
            }
        });

    }

    private HashMap<String, PolicyBag> map = new HashMap<>();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

}
