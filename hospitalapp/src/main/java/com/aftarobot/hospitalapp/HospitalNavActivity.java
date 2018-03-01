package com.aftarobot.hospitalapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.aftarobot.hospitalapp.services.FCMMessagingService;
import com.aftarobot.mlibrary.api.ChainDataAPI;
import com.aftarobot.mlibrary.api.ChainListAPI;
import com.aftarobot.mlibrary.api.FBApi;
import com.aftarobot.mlibrary.data.Burial;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.DeathCertificate;
import com.aftarobot.mlibrary.data.DeathCertificateRequest;
import com.aftarobot.mlibrary.data.Hospital;
import com.aftarobot.mlibrary.util.MyDialogFragment;
import com.aftarobot.mlibrary.util.SharedPrefUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HospitalNavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ChainDataAPI chainDataAPI;
    private ChainListAPI chainListAPI;
    private FBApi fbApi;
    private List<Client> clients;
    private Hospital hospital;
    private AutoCompleteTextView auto;
    private TextView txtName, txtId;
    private Toolbar toolbar;
    private Button btn;
    private String cause;
    private Spinner spinner;
    private DeathCertificate certificate;
    private Burial burial;
    private Claim claim;
    private MyDialogFragment dialogFragment;
    private FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navig);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        certificate= (DeathCertificate) getIntent().getSerializableExtra("cert");
        burial= (Burial) getIntent().getSerializableExtra("burial");
        claim= (Claim) getIntent().getSerializableExtra("claim");

        hospital = SharedPrefUtil.getHospital(this);
        chainDataAPI = new ChainDataAPI(this);
        chainListAPI = new ChainListAPI(this);
        fbApi = new FBApi();
        fm = getFragmentManager();
        dialogFragment = new MyDialogFragment();
        listen();
        getClients();

        setup();
        getSupportActionBar().setTitle("Death Certificate Requests");
        getSupportActionBar().setSubtitle(hospital.getName());
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
            }
        });
        if (count > 0) {
            dialogFragment.show(fm,"mydiagfragment");
        }
    }
    FloatingActionButton fab;

    private void setup() {
        fab = findViewById(R.id.fab);
        txtName = findViewById(R.id.name);
        txtId = findViewById(R.id.idNumber);
        auto = findViewById(R.id.auto);
        btn = findViewById(R.id.btnCreate);
        spinner = findViewById(R.id.spinner);
        btn.setEnabled(false);
        btn.setAlpha(0.4f);
        txtId.setText("");
        txtName.setText("");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCertificateRequest();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setEnabled(false);
                fab.setAlpha(0.3f);
                getClients();
                Snackbar.make(view, "Refreshing clients", Snackbar.LENGTH_LONG)
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
        setSpinner();
        spinner.setVisibility(View.GONE);
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    private void addCertificateRequest() {
        if (cause == null) {
            showError("Please select cause of death");
            return;
        }
        showSnack("Requesting Certificate from Home Affairs...","OK","yellow");
        final DeathCertificateRequest dc = new DeathCertificateRequest();
        dc.setCauseOfDeath(cause);
        dc.setHospital("resource:com.oneconnect.insurenet.Hospital#".concat(hospital.getHospitalId()));
        dc.setClient("resource:com.oneconnect.insurenet.Client#".concat(client.getIdNumber()));
        dc.setDateTime(sdf.format(new Date()));
        dc.setIdNumber(client.getIdNumber());
        dc.setHospitalId(hospital.getHospitalId());
        dc.setIssued(false);

        Log.d(TAG, "DeathCertificateRequest adding shit to blockchain: ".concat(GSON.toJson(dc)));

        chainDataAPI.addDeathCertificateRequestByTranx(dc, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(final Data data) {
                DeathCertificateRequest x = (DeathCertificateRequest)data;
                Log.i(TAG, "addCertificateRequest onResponse: ".concat(GSON.toJson(x)));
                showSnack("Death Certificate requested from Home Affairs", "OK", "green");
                reset();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        writetoFirebase((DeathCertificateRequest) data);
                    }
                });

            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    private void writetoFirebase(final DeathCertificateRequest dc) {
        fbApi.addDeathCertRequest(dc, new FBApi.FBListener() {
            @Override
            public void onResponse(Data data) {
                Log.i(TAG, "onResponse: DeathCertificateRequest added to Firebase: ".concat(dc.getIdNumber()));

            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }


    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private void reset() {
        btn.setAlpha(0.4f);
        btn.setEnabled(false);
        spinner.setSelection(0, true);
        txtId.setText("");
        txtName.setText("");
        spinner.setVisibility(View.GONE);
        clients.remove(client);

        setAuto();
    }

    private void getClients() {
        showSnack("Getting client list ...","wait","yellow");
        fab.setEnabled(false);
        fab.setAlpha(0.3f);
        chainListAPI.getClients(new ChainListAPI.ClientListener() {
            @Override
            public void onResponse(List<Client> list) {
                clients = list;
                snackbar.dismiss();
                fab.setEnabled(true);
                fab.setAlpha(1.0f);
                if (clients.isEmpty()) {
                    showError("There are no patients found, do something!");
                    auto.setVisibility(View.GONE);
                    return;
                }
                Snackbar.make(toolbar,"Patient list found: ".concat(String.valueOf(clients.size())),Snackbar.LENGTH_SHORT).show();
                auto.setVisibility(View.VISIBLE);
                setAuto();
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    private void setSpinner() {
        final List<String> list = new ArrayList<>();
        list.add("Natural Causes");
        list.add("Cardiac Arrest");
        list.add("Cancer");
        list.add("Tuberculosis");
        list.add("Pnuemonia");
        list.add("Accident Trauma");
        Collections.sort(list);
        list.add(0, "Select Cause of Death");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.simple_list, list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    cause = null;
                } else {
                    cause = list.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private Client client;

    private void setAuto() {
        final List<String> list = new ArrayList<>();
        for (Client c : clients) {
            list.add(c.getIdNumber().concat(" - ".concat(c.getFullName())));
        }
        list.add(0, "Select Patient");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        auto.setAdapter(adapter);
        auto.setThreshold(1);
        auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "onItemClick: position: " + position);
                setClient(auto.getText().toString());
                hideKeyboard();
            }
        });

    }

    private void setClient(String item) {
        String[] strings = item.split("-");
        String idNumber = strings[0];
        for (Client c : clients) {
            if (idNumber.trim().equalsIgnoreCase(c.getIdNumber())) {
                client = c;
                if (client.isDeceased()) {
                    showError("A Certificate already exists on the chain");
                    return;
                }
                txtName.setText(client.getFullName());
                txtId.setText(client.getIdNumber());
                btn.setAlpha(1f);
                btn.setEnabled(true);
                auto.setText("");
                spinner.setVisibility(View.VISIBLE);
                break;
            }
        }
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
        getMenuInflater().inflate(R.menu.navig, menu);
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

    void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(auto.getWindowToken(), 0);
    }
    private void listen() {
        IntentFilter filterCert = new IntentFilter(FCMMessagingService.BROADCAST_CERT);

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.registerReceiver(new CertReceiver(),filterCert );


    }
    private class CertReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context ctx, Intent m) {
            final DeathCertificate sentDC = (DeathCertificate) m.getSerializableExtra("data");
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

    private void showCert(final DeathCertificate dc) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
        });

    }

    public static final String TAG = HospitalNavActivity.class.getSimpleName();
}
