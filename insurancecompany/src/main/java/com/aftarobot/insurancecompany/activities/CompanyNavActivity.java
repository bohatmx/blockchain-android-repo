package com.aftarobot.insurancecompany.activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.aftarobot.insurancecompany.R;
import com.aftarobot.insurancecompany.services.FCMMessagingService;
import com.aftarobot.mlibrary.api.ChainListAPI;
import com.aftarobot.mlibrary.data.Burial;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.DeathCertificate;
import com.aftarobot.mlibrary.data.FundsTransfer;
import com.aftarobot.mlibrary.data.FundsTransferRequest;
import com.aftarobot.mlibrary.data.InsuranceCompany;
import com.aftarobot.mlibrary.data.Policy;
import com.aftarobot.mlibrary.util.MyDialogFragment;
import com.aftarobot.mlibrary.util.SharedPrefUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CompanyNavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private TextView txtPolicies, txtClaims, txtClients, txtDate;
    private View layout3, layout1, layout2;
    private ChainListAPI chainListAPI;
    private List<Client> clients;
    private List<Policy> policies;
    private List<Claim> claims;
    private InsuranceCompany company;
    private DeathCertificate certificate;
    private Burial burial;
    private Claim claim;
    private MyDialogFragment dialogFragment;
    private FragmentManager fm;

    public static final SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm");
    public static final String TAG = CompanyNavActivity.class.getSimpleName();
    public static final DecimalFormat df = new DecimalFormat("###,###,###,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Claims Business Network");

        certificate = (DeathCertificate) getIntent().getSerializableExtra("cert");
        burial = (Burial) getIntent().getSerializableExtra("burial");
        claim = (Claim) getIntent().getSerializableExtra("claim");


        setup();
        listen();
        getClients();

        checkMessage();
    }

    private void checkMessage() {
        fm = getFragmentManager();
        int count = 0;
        dialogFragment = new MyDialogFragment();
        if (certificate != null) {
            count++;
            dialogFragment.setData(certificate);
        }
        if (burial != null) {
            count++;
            dialogFragment.setData(burial);
        }
        if (claim != null) {
            count++;
            dialogFragment.setData(claim);
        }
        dialogFragment.setListener(new MyDialogFragment.Listener() {
            @Override
            public void onCloseButtonClicked() {
                dialogFragment.dismiss();
                certificate = null;
                burial = null;
                claim = null;
            }
        });
        if (count > 0) {
            dialogFragment.show(fm, "mydiagfragment");
        }
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    private void setup() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showSnack("Refreshing Dashboard ...", "ok", "yellow");
                getClients();
                getClaims();
                getPolicies();

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //drawer.openDrawer(GravityCompat.START);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        chainListAPI = new ChainListAPI(this);
        company = SharedPrefUtil.getCompany(this);
        Log.e(TAG, "setup: company: ".concat(GSON.toJson(company)));
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
        chainListAPI.getCompanyClaims(company.getInsuranceCompanyId(),new ChainListAPI.ClaimsListener() {
            @Override
            public void onResponse(List<Claim> list) {
                claims = list;
                Log.w(TAG, "onResponse: claims found on blockchain: " + list.size());
                txtClaims.setText(df.format(claims.size()));
                txtDate.setText(sdf.format(new Date()));
                getPolicies();

            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    private void getClients() {
        StringBuilder x = new StringBuilder();
        x.append("resource:com.oneconnect.insurenet.InsuranceCompany#");
        x.append(company.getInsuranceCompanyId());

        chainListAPI.getCompanyClients(x.toString(),new ChainListAPI.ClientListener() {
            @Override
            public void onResponse(List<Client> list) {
                clients = list;
                Log.w(TAG, "onResponse: clients found on blockchain: " + list.size());
                txtClients.setText(df.format(clients.size()));
                txtDate.setText(sdf.format(new Date()));
                getClaims();

            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }


    private void getPolicies() {

        chainListAPI.getCompanyPolicies(company.getInsuranceCompanyId(), new ChainListAPI.PolicyListener() {
            @Override
            public void onResponse(List<Policy> list) {
                Log.d(TAG, "getCompanyPolicies: policies found on blockchain: " + list.size() + " companyId:  "
                        + company.getInsuranceCompanyId());
                policies = list;
                txtPolicies.setText(df.format(policies.size()));
                Log.w(TAG, "getCompanyPolicies: company policies found on blockchain: " + policies.size());
                Snackbar.make(toolbar, "Dashboard refreshed", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private Snackbar snackbar;

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

    private void showSnack(String message, String action, String color) {
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
        IntentFilter filterFundsTransfer = new IntentFilter(FCMMessagingService.BROADCAST_FUNDS_TRANSFER);

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);

        ClaimReceiver receiver = new ClaimReceiver();
        broadcastManager.registerReceiver(receiver, filterClaim);

        CertReceiver receiverC = new CertReceiver();
        broadcastManager.registerReceiver(receiverC, filterCert);

        BurialReceiver receiverB = new BurialReceiver();
        broadcastManager.registerReceiver(receiverB, filterBurial);

        FundsTransferReceiver fundsTransferReceiver = new FundsTransferReceiver();
        broadcastManager.registerReceiver(fundsTransferReceiver, filterFundsTransfer);

    }

    private DeathCertificate sentDC;
    private Burial sentBurial;
    private Claim sentClaim;

    private class ClaimReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context ctx, Intent m) {
            sentClaim = (Claim) m.getSerializableExtra("data");
            getClaims();
            if (sentClaim != null) {
                snackbar = Snackbar.make(toolbar, "Claim Arrived: "
                        .concat(sentClaim.getClaimId()), Snackbar.LENGTH_INDEFINITE);
                snackbar.setActionTextColor(Color.CYAN);
                snackbar.setAction("Details", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showClaim(sentClaim);
                    }
                });
                snackbar.show();
            }

        }

    }

    private class BurialReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context ctx, Intent m) {
            sentBurial = (Burial) m.getSerializableExtra("data");
            if (sentBurial != null) {

                snackbar = Snackbar.make(toolbar, "Burial registered: "
                        .concat(sentBurial.getIdNumber()), Snackbar.LENGTH_INDEFINITE);
                snackbar.setActionTextColor(Color.CYAN);
                snackbar.setAction("Details", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBurial(sentBurial);
                    }
                });
                snackbar.show();
            }


        }

    }

    private class CertReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context ctx, Intent m) {
            sentDC = (DeathCertificate) m.getSerializableExtra("data");
            if (sentDC != null) {

                snackbar = Snackbar.make(toolbar, "Certificate Registered: "
                        .concat(sentDC.getIdNumber()), Snackbar.LENGTH_INDEFINITE);
                snackbar.setActionTextColor(Color.CYAN);
                snackbar.setAction("Details", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCert(sentDC);
                    }
                });
                snackbar.show();
            }

        }


    }

    private void showClaim(Claim claim) {

        Intent m = new Intent(this,ClaimsActivity.class);
        m.putExtra("claim", claim);
        startActivity(m);

//        FragmentTransaction ft = fm.beginTransaction();
//        Fragment prev = fm.findFragmentByTag("CLAIM_DIAG");
//        if (prev != null) {
//            ft.remove(prev);
//        }
//        ft.addToBackStack(null);
//        // Create and show the dialog.
//        final MyDialogFragment fragment = MyDialogFragment.newInstance();
//        fragment.setData(dc);
//        fragment.setListener(new MyDialogFragment.Listener() {
//            @Override
//            public void onCloseButtonClicked() {
//                fragment.dismiss();
//            }
//        });
//        fragment.show(ft, "CLAIM_DIAG");
    }

    private void showBurial(Burial dc) {

        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("BURIAL_DIAG");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        // Create and show the dialog.
        final MyDialogFragment fragment = MyDialogFragment.newInstance();
        fragment.setData(dc);
        fragment.setListener(new MyDialogFragment.Listener() {
            @Override
            public void onCloseButtonClicked() {
                fragment.dismiss();
            }
        });
        fragment.show(ft, "BURIAL_DIAG");
    }

    private void showCert(DeathCertificate dc) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("CERT_DIAG");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        // Create and show the dialog.
        final MyDialogFragment fragment = MyDialogFragment.newInstance();
        fragment.setData(dc);
        fragment.setListener(new MyDialogFragment.Listener() {
            @Override
            public void onCloseButtonClicked() {
                fragment.dismiss();
            }
        });
        fragment.show(ft, "CERT_DIAG");
    }
    private class FundsTransferReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive: FundsTransferReceiver ###########" );
            FundsTransfer data = (FundsTransfer)intent.getSerializableExtra("data");
            Log.i(TAG, "FundsTransferReceiver onReceive: "
                    .concat(GSON.toJson(data)));
            showRequest(data);
        }
    }
    private void showRequest(FundsTransfer transfer) {

        showSnack("Funds Transfer arrived: "
                .concat(transfer.getFundsTransferId()), "ok", "yellow");

        AlertDialog.Builder x = new AlertDialog.Builder(this);
        x.setTitle("Funds Transfer Arrived")
                .setMessage("A Funds Transfer message has arrived from the Bank. " +
                        "Do you want to notify the Beneficiary? \n\n".concat(transfer.getFundsTransferId()
                        ))
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        underConstruction();
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();



    }
    private void underConstruction() {
        Toast.makeText(this, "This feature still under construction", Toast.LENGTH_SHORT).show();
    }

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
}
