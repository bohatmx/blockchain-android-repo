package com.aftarobot.insurancecompany.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.aftarobot.insurancecompany.R;
import com.aftarobot.insurancecompany.services.FCMMessagingService;
import com.aftarobot.mlibrary.util.MyBroadcastReceiver;
import com.aftarobot.mlibrary.util.SharedPrefUtil;
import com.aftarobot.mlibrary.api.ChainListAPI;
import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.InsuranceCompany;
import com.aftarobot.mlibrary.data.Policy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private TextView txtPolicies, txtClaims, txtClients, txtDate;
    private View layout3, layout1, layout2;
    private ChainListAPI chainListAPI;
    private List<Client> clients;
    private List<Beneficiary> beneficiaries;
    private List<Policy> policies;
    private List<Claim> claims;
    private InsuranceCompany company;
    public static final SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm");
    public static final String TAG = NavActivity.class.getSimpleName();
    public static final DecimalFormat df = new DecimalFormat("###,###,###,###");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Claims Business Network");


        setup();
        listen();
        getClients();
        getClaims();
        getPolicies();
    }

    @Override
    public void onResume() {
        super.onResume();
        getClients();
    }
    private void setup() {
        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showSnack("Refreshing Dashboard ...","ok","yellow");
                getClients();
                getClaims();
                getPolicies();

            }
        });

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //drawer.openDrawer(GravityCompat.START);

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        chainListAPI = new ChainListAPI(this);
        company = SharedPrefUtil.getCompany(this);
        if (company != null) {
            View hdr = navigationView.getHeaderView(0);
            TextView txt = hdr.findViewById(R.id.txtTitle);
            txt.setText(company.getName());
            getSupportActionBar().setSubtitle(company.getName());
        }
        txtClaims = findViewById(R.id.claims);
        txtPolicies = findViewById(R.id.policies);
        txtClients = findViewById(R.id.clients);
        txtDate = findViewById(R.id.txtDate);
        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        layout3 = findViewById(R.id.layout3);
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent m = new Intent(getApplicationContext(), PolicyActivity.class);
                startActivity(m);
            }
        });
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent m = new Intent(getApplicationContext(), ClientsActivity.class);
                startActivity(m);
            }
        });
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent m = new Intent(getApplicationContext(), ClaimsActivity.class);
                startActivity(m);
            }
        });

        txtClients.setText("0");
        txtPolicies.setText("0");
        txtClaims.setText("0");
    }

    private void getClaims() {
        chainListAPI.getClaims(new ChainListAPI.ClaimsListener() {
            @Override
            public void onResponse(List<Claim> list) {
                claims = list;
                Log.w(TAG, "onResponse: claims found on blockchain: " + list.size());
                txtClaims.setText(df.format(claims.size()));
                txtDate.setText(sdf.format(new Date()));

            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }


    private void getClients() {
        chainListAPI.getClients(new ChainListAPI.ClientListener() {
            @Override
            public void onResponse(List<Client> list) {
                clients = list;
                Log.w(TAG, "onResponse: clients found on blockchain: " + list.size());
                txtClients.setText(df.format(clients.size()));

                txtDate.setText(sdf.format(new Date()));

            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    private void getBeneficiaries() {
        chainListAPI.getBeneficiaries(new ChainListAPI.BeneficiaryListener() {
            @Override
            public void onResponse(List<Beneficiary> list) {
                beneficiaries = list;
                Log.w(TAG, "onResponse: beneficiaries found on blockchain: " + list.size());

            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    private void getPolicies() {
        chainListAPI.getPolicies( new ChainListAPI.PolicyListener() {
                    @Override
                    public void onResponse(List<Policy> list) {
                        Log.d(TAG, "onResponse: all policies found on blockchain: " + list.size());
                        policies = new ArrayList<>();
                        for (Policy p: list) {
                            if (p.getInsuranceCompany().contains(company.getInsuranceCompanyID())) {
                                policies.add(p);
                            }
                        }
                        txtPolicies.setText(df.format(policies.size()));
                        Log.w(TAG, "onResponse: filtered company policies found on blockchain: " + policies.size());
                        Snackbar.make(toolbar,"Dashboard refreshed",Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(String message) {
                        showError(message);
                    }
                });
    }

    private Snackbar snackbar;
    private void showError(String message) {
        snackbar = Snackbar.make(toolbar,message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor("red"));
        snackbar.setAction("Error", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }
    private void showSnack(String message, String action, String color) {
        snackbar = Snackbar.make(toolbar,message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
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

    private void listen() {
        IntentFilter filterBurial = new IntentFilter(FCMMessagingService.BROADCAST_BURIAL);
        IntentFilter filterCert = new IntentFilter(FCMMessagingService.BROADCAST_CERT);
        IntentFilter filterClaim = new IntentFilter(FCMMessagingService.BROADCAST_CLAIM);
        IntentFilter filterPolicy = new IntentFilter(FCMMessagingService.BROADCAST_POLICY);

        MyBroadcastReceiver receiver = new MyBroadcastReceiver(this);
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.registerReceiver(receiver,filterCert);
        broadcastManager.registerReceiver(receiver,filterClaim);
        broadcastManager.registerReceiver(receiver,filterPolicy);
        broadcastManager.registerReceiver(receiver,filterBurial);

    }
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
}
