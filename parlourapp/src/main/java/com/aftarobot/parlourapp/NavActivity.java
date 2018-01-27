package com.aftarobot.parlourapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;

import com.aftarobot.mlibrary.SharedPrefUtil;
import com.aftarobot.mlibrary.api.ChainDataAPI;
import com.aftarobot.mlibrary.api.ChainListAPI;
import com.aftarobot.mlibrary.api.FBApi;
import com.aftarobot.mlibrary.data.Burial;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.DeathCertificate;
import com.aftarobot.mlibrary.data.FuneralParlour;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private List<DeathCertificate> certificates;
    private DeathCertificate certificate;
    private AutoCompleteTextView auto;
    private TextView txtName, txtIdNumber;
    private Button btn;
    private FuneralParlour parlour;
    private ChainDataAPI chainDataAPI;
    private ChainListAPI chainListAPI;
    private FBApi fbApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        chainDataAPI = new ChainDataAPI(this);
        chainListAPI = new ChainListAPI(this);
        fbApi = new FBApi();
        parlour = SharedPrefUtil.getParlour(this);
        getSupportActionBar().setTitle("Burial Services");
        getSupportActionBar().setSubtitle(parlour.getName());

        setup();
        getDeathCerts();
    }

    private void getDeathCerts() {
        chainListAPI.getDeathCertificates(new ChainListAPI.DeathCertListener() {
            @Override
            public void onResponse(List<DeathCertificate> list) {
                certificates = list;
                Log.i(TAG, "onResponse: getDeathCertificates: " + list.size());
                setAuto();
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private void addBurial() {

        if (certificate == null) {
            showError("Please find death certificate");
            return;
        }

        Burial burial = new Burial();
        burial.setIdNumber(certificate.getIdNumber());
        burial.setFuneralParlour("resource:com.oneconnect.insurenet.FuneralParlour#".concat(parlour.getFuneralParlourId()));
        burial.setDeathCertificate("resource:com.oneconnect.insurenet.DeathCertificate#".concat(certificate.getIdNumber()));
        burial.setDateTime(sdf.format(new Date()));

        Log.d(TAG, "registering Burial: ".concat(GSON.toJson(burial)));

        Snackbar.make(toolbar,"Registering burial", Snackbar.LENGTH_LONG).show();

        chainDataAPI.addBurial(burial, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                showSnack("Burial registered: ".concat(certificate.getIdNumber()),"close","yellow");
                txtIdNumber.setText("");
                txtName.setText("");
                certificates.remove(certificate);
                setAuto();
                Burial x = (Burial) data;
                fbApi.addBurial(x, new FBApi.FBListener() {
                    @Override
                    public void onResponse(Data data) {
                        Log.i(TAG, "onResponse: burial added to FB");
                        btn.setAlpha(0.3f);
                        btn.setEnabled(false);
                        certificate = null;
                        auto.setText("");
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

    private void setAuto() {
        List<String> list = new ArrayList<>();
        for (DeathCertificate dc: certificates) {
            list.add(dc.getIdNumber());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.large_list_item,list);
        auto.setAdapter(adapter);
        auto.setThreshold(1);
        auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setCertificate(auto.getText().toString());
            }
        });
    }
    private static final Locale loc = Locale.getDefault();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", loc);
    private void setCertificate(String id) {
        for (DeathCertificate dc: certificates) {
            if (dc.getIdNumber().equalsIgnoreCase(id)) {
                certificate = dc;
                txtIdNumber.setText(certificate.getIdNumber());

                auto.setText("");
                hideKeyboard();
                findClient();

            }
        }
    }
    private void findClient() {
        showSnack("Finding client ...","wait","yellow");
        chainListAPI.getClient(certificate.getIdNumber(), new ChainListAPI.ClientListener() {
            @Override
            public void onResponse(List<Client> clients) {
                if (clients.isEmpty()) {
                    showError("Client cannot be found");
                    return;
                }
                Client client = clients.get(0);
                txtName.setText(client.getFullName());
                btn.setAlpha(1.0f);
                btn.setEnabled(true);
                snackbar.dismiss();
            }

            @Override
            public void onError(String message) {

            }
        });
    }
    private void setup() {
        auto = findViewById(R.id.auto);
        btn = findViewById(R.id.btnBurial);
        txtName = findViewById(R.id.txtName);
        txtIdNumber = findViewById(R.id.txtIdnumber);
        txtIdNumber.setText("");
        btn.setAlpha(0.3f);
        btn.setEnabled(false);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBurial();
            }
        });

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
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
    void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(auto.getWindowToken(), 0);
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

    public static final String TAG = NavActivity.class.getSimpleName();
}
