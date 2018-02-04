package com.aftarobot.homeaffairs;

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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.aftarobot.mlibrary.api.ChainDataAPI;
import com.aftarobot.mlibrary.api.ChainListAPI;
import com.aftarobot.mlibrary.api.FBApi;
import com.aftarobot.mlibrary.api.FBListApi;
import com.aftarobot.mlibrary.data.BeneficiaryClaimMessageDTO;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.DeathCertificate;
import com.aftarobot.mlibrary.data.DeathCertificateRequest;
import com.aftarobot.mlibrary.data.Policy;
import com.aftarobot.mlibrary.data.UserDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class NavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    ChainListAPI chainListAPI;
    ChainDataAPI chainDataAPI;
    RecyclerView recyclerView;
    TextView txtCount;
    CertRequestAdapter adapter;
    List<DeathCertificateRequest> requests;
    FBApi fbApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home Affairs");
        getSupportActionBar().setSubtitle("Death Certificate Requests");
        setup();

        chainDataAPI = new ChainDataAPI(this);
        chainListAPI = new ChainListAPI(this);
        fbApi = new FBApi();

        getCertRequests();

    }

    private void setup() {
        recyclerView = findViewById(R.id.recyclerView);
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
                requests = list;
                setList();
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    private void setList() {
        if (adapter == null) {
            adapter = new CertRequestAdapter(requests, new CertRequestAdapter.RequestListener() {
                @Override
                public void onRequestTapped(DeathCertificateRequest request) {
                    Log.d(TAG, "onRequestTapped: ".concat(GSON.toJson(request)));
                    confirm(request);
                }
            });
        } else {
            adapter.notifyDataSetChanged();
        }
        txtCount.setText(String.valueOf(requests.size()));
    }

    private void confirm(final DeathCertificateRequest request) {
        AlertDialog.Builder x = new AlertDialog.Builder(this);
        x.setTitle("Issue Death Certificate")
                .setMessage("Do you want to issue a Death Certificate for ".concat(request.getIdNumber()
                        .concat("?")))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        issueCertificate(request);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    DeathCertificateRequest deathCertificateRequest;

    private void issueCertificate(final DeathCertificateRequest request) {
        deathCertificateRequest = request;
        DeathCertificate x = new DeathCertificate();
        x.setIdNumber(request.getIdNumber());
        x.setCauseOfDeath(request.getCauseOfDeath());
        x.setClient(request.getClient());
        x.setDateTime(request.getDateTime());
        x.setDoctor(request.getDoctor());
        x.setHospital(request.getHospital());

        showSnackbar("Issuing Death Certificate ...", "Wait", "yellow");
        chainDataAPI.registerDeathCertificate(x, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                final DeathCertificate x = (DeathCertificate) data;
                Log.i(TAG, "issueCertificate done: ".concat(GSON.toJson(x)));
                requests.remove(request);
                setList();
                showSnackbar("Death Certificate issued on blockchain", "OK", "green");
                fbApi.addDeathCert(x, new FBApi.FBListener() {
                    @Override
                    public void onResponse(Data data) {
                        Log.i(TAG, "onResponse: certificate added to Firebase: ".concat(x.getIdNumber()));
                        findPolicy(x.getIdNumber());
                    }

                    @Override
                    public void onError(String message) {
                        showError(message);
                    }
                });
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private Policy policy;

    private void findPolicy(final String idNumber) {
        chainListAPI.getPolicies(new ChainListAPI.PolicyListener() {
            @Override
            public void onResponse(List<Policy> policies) {
                Log.i(TAG, "onResponse: policies found: ".concat(String.valueOf(policies.size())));
                policy = null;
                for (Policy p : policies) {
                    int i = p.getClient().indexOf("#");
                    if (i > -1) {
                        String policyIdNumber = p.getClient().substring(i + 1);
                        if (policyIdNumber.equalsIgnoreCase(idNumber)) {
                            policy = p;
                            writeClaim();
                        }
                    }
                }
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    private void writeClaim() {
        showSnackbar("Adding Claim to blockchain", "OK", "cyan");
        final Claim claim = new Claim();
        String[] strings = policy.getInsuranceCompany().split("#");
        claim.setCompanyId(strings[1]);
        claim.setDateTime(sdf.format(new Date()));
        claim.setClaimId(getRandomClaimId());
        claim.setPolicy("resource:com.oneconnect.insurenet.Policy#".concat(policy.getPolicyNumber()));
        claim.setHospital(deathCertificateRequest.getHospital());
        chainDataAPI.addClaim(claim, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                final Claim x = (Claim) data;
                //todo send beneficiaries a message that claim has been recorded
                fbApi.addClaim(x, new FBApi.FBListener() {
                    @Override
                    public void onResponse(Data data) {
                        Log.e(TAG, "onResponse: claim added to FB".concat(GSON.toJson(x)));
                        showSnackbar("Claims registration process started", "ok", "green");
                        sendBeneficiaryMessage(claim);
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

    FBListApi fbListApi = new FBListApi();
    int count;

    private void sendBeneficiaryMessage(final Claim claim) {

        final List<String> idNumbers = new ArrayList<>();
        chainListAPI.getPolicy(policy.getPolicyNumber(), new ChainListAPI.PolicyListener() {
            @Override
            public void onResponse(List<Policy> policies) {
                if (!policies.isEmpty()) {
                    Policy policy = policies.get(0);
                    for (String b : policy.getBeneficiaries()) {
                        String[] strings = b.split("#");
                        if (strings.length == 2) {
                            String idNumber = strings[1];
                            idNumbers.add(idNumber);
                        }
                    }
                    final List<String> tokens = new ArrayList<>();

                    for (String id : idNumbers) {
                        fbListApi.getUserByIDnumber(id, new FBListApi.UserListener() {
                            @Override
                            public void onResponse(List<UserDTO> users) {
                                if (!users.isEmpty()) {
                                    UserDTO b = users.get(0);
                                    if (b.getFcmToken() != null) {
                                        tokens.add(b.getFcmToken());
                                    }
                                }
                                count++;
                                if (count > idNumbers.size()) {
                                    //send message to tokens ...
                                    if (!tokens.isEmpty()) {
                                        sendToTokens(tokens, claim);
                                    }
                                }
                            }

                            @Override
                            public void onError(String message) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void sendToTokens(List<String> tokens, Claim claim) {
        showSnackbar("Sending messages to beneficiaries: ".concat(String.valueOf(tokens.size())), "OK", "yellow");
        for (String token : tokens) {
            BeneficiaryClaimMessageDTO message = new BeneficiaryClaimMessageDTO();
            message.setFcmToken(token);
            message.setClaim(claim);
            fbApi.addBeneficiaryClaimMessage(message, new FBApi.FBListener() {
                @Override
                public void onResponse(Data data) {
                    Log.i(TAG, "onResponse: beneficiary message record added to FireBase");
                }

                @Override
                public void onError(String message) {
                    showError(message);
                }
            });
        }
    }

    public static String getRandomClaimId() {
        StringBuilder sb = new StringBuilder();
        sb.append("C");
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append("-");
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append("-");
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        Log.d("ListUtil", "getRandomClaimNumber: ".concat(sb.toString()));

        return sb.toString();
    }

    private static Random random = new Random(System.currentTimeMillis());

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

    public static final String TAG = NavActivity.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

}
