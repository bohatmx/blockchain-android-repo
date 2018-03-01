package com.aftarobot.beneficiary;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aftarobot.beneficiary.services.FCMMessagingService;
import com.aftarobot.mlibrary.api.ChainDataAPI;
import com.aftarobot.mlibrary.api.ChainListAPI;
import com.aftarobot.mlibrary.api.FBApi;
import com.aftarobot.mlibrary.data.BankAccount;
import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.BeneficiaryClaimMessage;
import com.aftarobot.mlibrary.data.BeneficiaryFunds;
import com.aftarobot.mlibrary.data.BeneficiaryThanks;
import com.aftarobot.mlibrary.data.Burial;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.DeathCertificate;
import com.aftarobot.mlibrary.data.InsuranceCompany;
import com.aftarobot.mlibrary.data.Policy;
import com.aftarobot.mlibrary.util.ListUtil;
import com.aftarobot.mlibrary.util.PolicyBag;
import com.aftarobot.mlibrary.util.SharedPrefUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class BeneficiaryNavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Beneficiary beneficiary;
    Toolbar toolbar;
    List<Policy> policies = new ArrayList<>();
    ChainListAPI chainListAPI;
    TextView txtCount;
    RecyclerView recyclerView;
    ImageView icon;

    public static final String TAG = BeneficiaryNavActivity.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        icon = findViewById(R.id.icon1);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        txtCount = findViewById(R.id.txtCount);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        beneficiary = SharedPrefUtil.getBeneficiary(this);
        Log.e(TAG, "onCreate: beneficiary: ".concat(GSON.toJson(beneficiary)));
        if (beneficiary == null) {
            Intent m = new Intent(this, LoginActivity.class);
            startActivity(m);
            finish();
            return;
        }
        Objects.requireNonNull(getSupportActionBar()).setTitle("Beneficiary Services");
        getSupportActionBar().setSubtitle(beneficiary.getFullName());

        chainListAPI = new ChainListAPI(this);
        setup();
        index = 0;
        getChainBeneficiary();
        listen();
    }

    Beneficiary chainBeneficiary;
    int index;

    private void getChainBeneficiary() {
        showSnackbar("Loading beneficiary policies", "ok", "yellow");
        chainListAPI.getBeneficiary(beneficiary.getIdNumber(), new ChainListAPI.BeneficiaryListener() {
            @Override
            public void onResponse(List<Beneficiary> beneficiaries) {
                snackbar.dismiss();
                if (!beneficiaries.isEmpty()) {
                    chainBeneficiary = beneficiaries.get(0);
                    index = 0;
                    controlPolicies();
                } else {
                    showError("Beneficiary not found for policies");
                }
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    private void controlPolicies() {
        if (chainBeneficiary.getPolicies() != null) {
            if (index < chainBeneficiary.getPolicies().size()) {
                String[] strings = chainBeneficiary.getPolicies().get(index).split("#");
                getPolicy(strings[1]);
            } else {
                setList();
                showSnackbar("Found details for ".concat(String.valueOf(
                        chainBeneficiary.getPolicies().size()).concat(" policies")), "Ok", "green");
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
        for (String policyNumber : mset) {
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
        txtCount.setText(String.valueOf(chainBeneficiary.getPolicies().size()));

    }

    Claim claim;

    private void checkClaim(final PolicyBag bag) {
        chainListAPI.getClaims(new ChainListAPI.ClaimsListener() {
            @Override
            public void onResponse(List<Claim> claims) {
                if (!claims.isEmpty()) {
                    boolean isFound = false;

                    for (Claim c : claims) {
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
                        showSnackbar("No Claims found for ".concat(bag.getPolicy().getPolicyNumber()), "close", "cyan");
                    }

                } else {
                    showSnackbar("No Claims found on the Blockhain", "close", "cyan");
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
        showSnackbar("Claim found: ".concat(claim.getClaimId()), "OK", "green");

    }

    private void getPolicy(String policyNumber) {
        showSnackbar("Getting policy details: ".concat(policyNumber), "OK", "yellow");
        chainListAPI.getPolicy(policyNumber, new ChainListAPI.PolicyListener() {
            @Override
            public void onResponse(List<Policy> list) {
                if (!list.isEmpty()) {
                    policies.add(list.get(0));
                    Log.w(TAG, "onResponse: policy found: ".concat(GSON.toJson(list.get(0))));
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
                    Log.w(TAG, "onResponse: client found: ".concat(GSON.toJson(client)));
                    chainListAPI.getInsuranceCompany(companyID, new ChainListAPI.CompanyListener() {
                        @Override
                        public void onResponse(List<InsuranceCompany> companies) {
                            if (!companies.isEmpty()) {
                                InsuranceCompany x = companies.get(0);
                                Log.e(TAG, "onResponse: company found: ".concat(GSON.toJson(x)));
                                PolicyBag bag = new PolicyBag(policy, client, x);
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
            getBankAccount();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    BankAccount bankAccount;

    private void getBankAccount() {
        if (chainBeneficiary == null) {
            getChainBeneficiary();
            return;
        }
        showSnackbar("Loading bank account", "ok", "cyan");
        String accountNumber = chainBeneficiary.getBankAccounts().get(0).split("#")[1];
        chainListAPI.getBankAccount(accountNumber, new ChainListAPI.BankAccountListener() {
            @Override
            public void onResponse(List<BankAccount> bankAccounts) {
                if (!bankAccounts.isEmpty()) {
                    bankAccount = bankAccounts.get(0);
                    showSnackbar("Bank Account Balance: ".concat(df.format(bankAccount.getBalance())),
                            "ok", "green");
                } else {

                }
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###,###,###,##0.00");

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

    private void listen() {
        IntentFilter claim = new IntentFilter(FCMMessagingService.BROADCAST_BENEFICIARY_CLAIM);
        IntentFilter funds = new IntentFilter(FCMMessagingService.BROADCAST_BENEFICIARY_FUNDS);
        IntentFilter thanks = new IntentFilter(FCMMessagingService.BROADCAST_BENEFICIARY_THANKS);
        IntentFilter burial = new IntentFilter(FCMMessagingService.BROADCAST_BURIAL);
        IntentFilter cert = new IntentFilter(FCMMessagingService.BROADCAST_CERT);

        LocalBroadcastManager.getInstance(this).registerReceiver(new ClaimReceiver(), claim);
        LocalBroadcastManager.getInstance(this).registerReceiver(new FundsReceiver(), funds);
        LocalBroadcastManager.getInstance(this).registerReceiver(new Certceiver(), cert);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BurialReceiver(), burial);
        LocalBroadcastManager.getInstance(this).registerReceiver(new ThanksReceiver(), thanks);
    }

    private class ClaimReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            BeneficiaryClaimMessage msg = (BeneficiaryClaimMessage) intent.getSerializableExtra("data");
            showClaim(msg);


        }
    }

    private void showClaim(BeneficiaryClaimMessage msg) {
        String title = "A claim was approved: ".concat(msg.getStringDate());
        showSnackbar(title, "ok", "green");

    }

    private class FundsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            BeneficiaryFunds msg = (BeneficiaryFunds) intent.getSerializableExtra("data");
            showFunds(msg);


        }
    }

    private void showFunds(BeneficiaryFunds msg) {
        String title = "Funds were transferred to your account: ".concat(msg.getStringDate());
        showSnackbar(title, "ok", "green");

    }

    private class Certceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            DeathCertificate msg = (DeathCertificate) intent.getSerializableExtra("data");
            showCert(msg);


        }
    }

    private void showCert(DeathCertificate msg) {
        //check if this is your cert -
        chainListAPI.getClient(msg.getIdNumber(), new ChainListAPI.ClientListener() {
            @Override
            public void onResponse(List<Client> clients) {
                if (!clients.isEmpty()) {
                    Client c = clients.get(0);
                    processPolicies(c);
                }
            }

            @Override
            public void onError(String message) {

            }
        });
        String title = "A Death Certicate has been issued: ".concat(msg.getIdNumber());
        showSnackbar(title, "ok", "grey");

    }

    private void processPolicies(Client client) {
        if (client.getPolicies() != null && !client.getPolicies().isEmpty()) {
            String policyNumber = client.getPolicies().get(0).split("#")[1];
            chainListAPI.getPolicy(policyNumber, new ChainListAPI.PolicyListener() {
                @Override
                public void onResponse(List<Policy> policies) {
                    if (!policies.isEmpty()) {
                        Policy policy = policies.get(0);
                        boolean isFound = false;
                        for (String s : policy.getBeneficiaries()) {
                            if (beneficiary.getIdNumber().equalsIgnoreCase(s.split("#")[1])) {
                                isFound = true;
                                break;
                            }
                        }
                        if (isFound) {
                            makeClaim(policy);
                        }
                    }
                }

                @Override
                public void onError(String message) {

                }
            });
        }
    }

    Policy policy;

    private void makeClaim(final Policy policy) {
        this.policy = policy;
        AlertDialog.Builder x = new AlertDialog.Builder(this);
        x.setTitle("Make a Claim")
                .setMessage(("A death certificate has been issued and a policy including you " +
                        "as a Beneficiary is on record. Do you want to register a claim?\n\n")
                        .concat("Policy Number: ".concat(policy.getPolicyNumber())))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        registerClaim(policy);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();

    }

    private void registerClaim(Policy policy) {
        showSnackbar("Registering claim on the blockchain", "ok", "yellow");
        claim = new Claim();
        String[] strings = policy.getInsuranceCompany().split("#");
        claim.setCompanyId(strings[1]);
        claim.setDateTime(sdf.format(new Date()));
        claim.setClaimId(policy.getPolicyNumber());
        claim.setAmount(policy.getAmount());
        claim.setApproved(false);
        claim.setPolicy("resource:com.oneconnect.insurenet.Policy#".concat(policy.getPolicyNumber()));
        claim.setInsuranceCompany("resource:com.oneconnect.insurenet.InsuranceCompanyy#".concat(strings[1]));
        claim.setPolicyNumber(policy.getPolicyNumber());
        ChainDataAPI chainDataAPI = new ChainDataAPI(this);
        final FBApi fbApi = new FBApi();
        chainDataAPI.submitClaim(claim, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                final Claim x = (Claim) data;
                showSnackbar("Claim registered: ".concat(x.getClaimId()), "OK", "green");
                fbApi.addClaim(x, new FBApi.FBListener() {
                    @Override
                    public void onResponse(Data data) {
                        Log.e(TAG, "onResponse: claim added to FB".concat(GSON.toJson(x)));
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

    private class BurialReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Burial msg = (Burial) intent.getSerializableExtra("data");
            showBurial(msg);


        }
    }

    private void showBurial(Burial msg) {
        String title = "The Burial was registered: ".concat(msg.getIdNumber());
        showSnackbar(title, "ok", "grey");

    }

    private class ThanksReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            BeneficiaryThanks msg = (BeneficiaryThanks) intent.getSerializableExtra("data");
            showThanks(msg);


        }
    }

    private void showThanks(BeneficiaryThanks msg) {
        showSnackbar(msg.getMessage(), "ok", "green");

    }

    public static final SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");

}
