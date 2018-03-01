package com.aftarobot.insurancecompany.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.aftarobot.insurancecompany.R;
import com.aftarobot.insurancecompany.adapters.HistorianAdapter;
import com.aftarobot.mlibrary.api.ChainListAPI;
import com.aftarobot.mlibrary.data.BankAccount;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.HistorianRecord;
import com.aftarobot.mlibrary.data.Policy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HistorianActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final String TAG = HistorianActivity.class.getSimpleName();
    TextView txtCount, txtDate;
    RecyclerView recyclerView;
    ChainListAPI chainListAPI;
    Snackbar snackbar;
    Toolbar toolbar;
    List<HistorianRecord> records;
    HistorianRecord record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historian);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Blockchain Transactions");
        getSupportActionBar().setSubtitle("Chronological History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chainListAPI = new ChainListAPI(this);

        setup();
    }

    @Override
    public void onResume() {
        super.onResume();
        getHistorianRecords();
    }
    private void getHistorianRecords() {
        fab.setEnabled(false);
        fab.setAlpha(0.3f);
        showSnackbar("Loading Transaction Records ...","ok","cyan");
        chainListAPI.getHistorianRecords(new ChainListAPI.HistorianListener() {
            @Override
            public void onResponse(List<HistorianRecord> list) {
                records = list;
                snackbar.dismiss();
                fab.setEnabled(true);
                fab.setAlpha(1.0f);
                Log.i(TAG, "getHistorianRecords onResponse: ".concat(GSON.toJson(records)));
                setList();
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    private void setList() {
        Collections.sort(records);
        HistorianAdapter adapter = new HistorianAdapter(records, new HistorianAdapter.HistorianListener() {
            @Override
            public void onClaimTapped(HistorianRecord rec) {
                record = rec;
                showDetails();
            }
        });
        recyclerView.setAdapter(adapter);
        txtCount.setText(String.valueOf(records.size()));
        txtDate.setText(sdf.format(new Date()));

    }
    public static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss");
    public static final int POLICY = 1, BANK_ACCOUNT = 2, CLIENT_ACCOUNT = 3;
    private void showDetails() {
        List<Object> events = record.getEventsEmitted();

        if (events != null && !events.isEmpty()) {
            LinkedTreeMap<String, Object> o = (LinkedTreeMap<String, Object>) events.get(0);
            JsonObject jsonObject = GSON.toJsonTree(o).getAsJsonObject();
            JsonObject pol = (JsonObject) jsonObject.get("policy");
            if (pol != null) {
                Policy policy = new Policy();
                policy.setPolicyNumber(pol.get("policyNumber").toString());
                policy.setAmount(Double.parseDouble(pol.get("amount").toString()));
                policy.setIdNumber(pol.get("idNumber").toString());
                policy.setDescription(pol.get("description").toString());
                display(policy,POLICY);

            }
            JsonObject acc = (JsonObject) jsonObject.get("bankAccount");
            if (acc != null) {
                BankAccount bankAccount = new BankAccount();
                bankAccount.setAccountNumber(acc.get("accountNumber").toString());
                bankAccount.setBalance(Double.parseDouble(acc.get("balance").toString()));
                if (acc.get("client") != null) {
                    bankAccount.setClient(acc.get("client").toString());
                }
                if (acc.get("beneficiary") != null) {
                    bankAccount.setBeneficiary(acc.get("beneficiary").toString());
                }
                display(bankAccount,BANK_ACCOUNT);

            }




            return;
        }
        display(record,0);
    }

    private void display(Data data, int type ) {
        StringBuilder sb = new StringBuilder();
        sb.append("Details of Transaction\n");
        switch (type) {
            case 0:
                HistorianRecord h = (HistorianRecord) data;
                sb.append(GSON.toJson(h));
                break;
            case POLICY:
                sb.append("Policy Event Emitted\n\n");
                Policy p = (Policy)data;
                sb.append(GSON.toJson(p));
                break;
            case BANK_ACCOUNT:
                sb.append("Bank Account Event Emitted\n\n");
                BankAccount acc = (BankAccount) data;
                sb.append(GSON.toJson(acc));
                break;

        }
        AlertDialog.Builder x = new AlertDialog.Builder(this);
        x.setTitle("Transaction Details")
                .setMessage(sb.toString())
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

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
    FloatingActionButton fab;
    DrawerLayout drawer;
    private void setup() {
        fab =  findViewById(R.id.fab);
        txtCount =  findViewById(R.id.txtCount);
        txtDate =  findViewById(R.id.txtDate);
        recyclerView =  findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab =  findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getHistorianRecords();
            }
        });

         drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
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
        getMenuInflater().inflate(R.menu.historian, menu);
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
}
