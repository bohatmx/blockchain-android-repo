package com.aftarobot.homeaffairs;

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
import android.widget.Toast;

import com.aftarobot.homeaffairs.services.FCMMessagingService;
import com.aftarobot.mlibrary.api.ChainDataAPI;
import com.aftarobot.mlibrary.api.ChainListAPI;
import com.aftarobot.mlibrary.api.FBApi;
import com.aftarobot.mlibrary.api.FBListApi;
import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.BeneficiaryClaimMessage;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.DeathCertificate;
import com.aftarobot.mlibrary.data.DeathCertificateRequest;
import com.aftarobot.mlibrary.data.Policy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class HomeAffairsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    ChainListAPI chainListAPI;
    ChainDataAPI chainDataAPI;
    RecyclerView recyclerView;
    TextView txtCount;
    CertRequestAdapter adapter;
    List<DeathCertificateRequest> requests;
    DeathCertificateRequest request;
    FBApi fbApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Home Affairs");
        getSupportActionBar().setSubtitle("Death Certificate Requests");
        chainDataAPI = new ChainDataAPI(this);
        chainListAPI = new ChainListAPI(this);
        fbApi = new FBApi();
        setup();
        String json = getIntent().getStringExtra("json");
        if (json != null) {
            request = GSON.fromJson(json, DeathCertificateRequest.class);
            Log.d(TAG, "onCreate ####: ".concat(GSON.toJson(request)));
            confirm(request, "Request Arrived from Blockchain");
            getCertRequests();
            return;
        }

        request = (DeathCertificateRequest) getIntent().getSerializableExtra("request");
        if (request != null) {
            confirm(request, "Request Arrived");
        }
        listen();
        getCertRequests();

    }

    private void setup() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        txtCount = findViewById(R.id.txtCount);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCertRequests();
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

    private void getCertRequests() {
        showSnackbar("Loading certificate requests ...", "wait", "yellow");
        chainListAPI.getDeathCertificateRequests(new ChainListAPI.DeathCertRequestListener() {
            @Override
            public void onResponse(List<DeathCertificateRequest> list) {
                snackbar.dismiss();
                requests = new ArrayList<>();
                for (DeathCertificateRequest x : list) {
                    if (!x.isIssued()) {
                        requests.add(x);
                    }
                }
                setList();
                if (requests.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "No requests found on the blockchain", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    private void setList() {
        adapter = new CertRequestAdapter(requests, new CertRequestAdapter.RequestListener() {
            @Override
            public void onRequestTapped(DeathCertificateRequest request) {
                Log.d(TAG, "onRequestTapped: ".concat(GSON.toJson(request)));
                confirm(request, "Issue Death Certificate");
            }
        });
        recyclerView.setAdapter(adapter);
        txtCount.setText(String.valueOf(requests.size()));
    }

    private void confirm(final DeathCertificateRequest request, String title) {
        AlertDialog.Builder x = new AlertDialog.Builder(this);
        x.setTitle(title)
                .setMessage("Do you want to issue a Death Certificate for ".concat(request.getIdNumber()
                        .concat("?\n\n").concat(GSON.toJson(request))))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        issueCertificate(request);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getCertRequests();
                    }
                })
                .show();
    }

    DeathCertificateRequest deathCertificateRequest;

    private void issueCertificate(final DeathCertificateRequest request) {
        deathCertificateRequest = request;
        DeathCertificate certificate = new DeathCertificate();
        certificate.setIdNumber(request.getIdNumber());
        certificate.setCauseOfDeath(request.getCauseOfDeath());
        certificate.setClient(request.getClient());
        certificate.setDateTime(request.getDateTime());



        if (request.getHospital() != null) {
            certificate.setHospital(request.getHospital());
            certificate.setHospitalId(request.getHospital().split("#")[1]);
        }
        if (request.getDoctor() != null) {
            certificate.setDoctor(request.getDoctor());
            certificate.setDoctorId(request.getDoctor().split("#")[1]);
        }

        showSnackbar("Issuing Death Certificate ...", "Wait", "yellow");
        chainDataAPI.registerDeathCertificate(certificate, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                final DeathCertificate chainCert = (DeathCertificate) data;
                Log.i(TAG, "issueCertificate done: ".concat(GSON.toJson(chainCert)));
                requests.remove(request);
                setList();

                fbApi = new FBApi();
                fbApi.addDeathCertificate(chainCert, new FBApi.FBListener() {
                    @Override
                    public void onResponse(Data data) {
                        Log.e(TAG, "onResponse: certificate added to Firebase: ".concat(chainCert.getIdNumber()));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showSnackbar("Death Certificate issued on blockchain", "OK", "green");
                            }
                        });
                    }

                    @Override
                    public void onError(String message) {
                        showError(message);
                    }
                });

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

    public static final String TAG = HomeAffairsActivity.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    private void listen() {
        IntentFilter m = new IntentFilter(FCMMessagingService.BROADCAST_CERT_REQ);
        LocalBroadcastManager.getInstance(this).registerReceiver(new RequestReceiver(), m);
    }

    private class RequestReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.w(TAG, "################# RequestReceiver onReceive: ***************: ");
            DeathCertificateRequest f = (DeathCertificateRequest) intent.getSerializableExtra("data");
            getCertRequests();
            showRequestArrived(f);

        }
    }

    private void showRequestArrived(final DeathCertificateRequest request) {
        snackbar = Snackbar.make(toolbar, "Certificate request has arrived", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor("yellow"));
        snackbar.setAction("Issue", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm(request, "Process Certificate Request");
            }
        });
        snackbar.show();
    }

}
