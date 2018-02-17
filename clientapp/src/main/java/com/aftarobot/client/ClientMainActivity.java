package com.aftarobot.client;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.aftarobot.mlibrary.api.ChainDataAPI;
import com.aftarobot.mlibrary.api.ChainListAPI;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.Policy;
import com.aftarobot.mlibrary.util.ListUtil;
import com.aftarobot.mlibrary.util.SharedPrefUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
        chainListAPI.getPoliciesByClientId(client.getIdNumber(), new ChainListAPI.PolicyListener() {
            @Override
            public void onResponse(List<Policy> list) {
                Log.i(TAG, "onResponse: found policies: " + list.size());
                policies = list;
                setList();
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    PolicyAdapter adapter;

    private void setList() {
        adapter = new PolicyAdapter(policies, new PolicyAdapter.PolicyListener() {
            @Override
            public void onPolicyTapped(Policy policy) {
                confirm(policy);
            }
        });
        recyclerView.setAdapter(adapter);
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
        claim.setClaimId(ListUtil.getRandomClaimId());
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

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");


}
