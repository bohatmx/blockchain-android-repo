package com.aftarobot.insurancecompany.activities;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.aftarobot.insurancecompany.R;
import com.aftarobot.insurancecompany.adapters.PolicyAdapter;
import com.aftarobot.insurancecompany.services.FCMMessagingService;
import com.aftarobot.mlibrary.api.ChainDataAPI;
import com.aftarobot.mlibrary.api.ChainListAPI;
import com.aftarobot.mlibrary.api.FBApi;
import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.Burial;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.InsuranceCompany;
import com.aftarobot.mlibrary.data.Policy;
import com.aftarobot.mlibrary.util.ListUtil;
import com.aftarobot.mlibrary.util.MyDialogFragment;
import com.aftarobot.mlibrary.util.SharedPrefUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PolicyActivity extends AppCompatActivity {

    private TextView txtTitle, txtCount;
    private RecyclerView recyclerView;
    private ChainListAPI chainListAPI;
    private List<Policy> policies;
    private PolicyAdapter policyAdapter;
    private Toolbar toolbar;
    private ChainDataAPI chainDataAPI;
    private InsuranceCompany company;
    private List<Client> clients;
    private List<Beneficiary> beneficiaries;
    private AutoCompleteTextView auto;
    private FloatingActionButton btnPolicy;
    private Client client;
    private MyDialogFragment dialogFragment;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Policy Management");
        company = SharedPrefUtil.getCompany(this);
        if (company != null) {
            getSupportActionBar().setSubtitle(company.getName());
        }
        chainListAPI = new ChainListAPI(this);
        chainDataAPI = new ChainDataAPI(this);
        fm = getFragmentManager();
        dialogFragment = new MyDialogFragment();

        getClients();
        listen();
        setup();
    }

    private void setup() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Refreshing client list", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                getClients();
            }
        });
        txtTitle = findViewById(R.id.txtTitle);
        auto = findViewById(R.id.auto);
        auto.setThreshold(1);
        btnPolicy = findViewById(R.id.btnPolicy);
        txtCount = findViewById(R.id.txtCount);
        txtTitle.setText("Policies");
        txtCount.setText("0");

        recyclerView = findViewById(R.id.recycler);
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);

        btnPolicy.setVisibility(View.GONE);
        btnPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (client == null) {
                    Toast.makeText(getApplicationContext(), "Find client first", Toast.LENGTH_SHORT).show();
                    return;
                }
                confirm(client);
            }
        });

    }

    int count;
    List<Beneficiary> policyBeneficiaries;

    private void setAuto() {
        List<String> list = new ArrayList<>();
        for (Client c : clients) {
            list.add(c.getFullName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, list);
        auto.setAdapter(adapter);
        auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                setClient(auto.getText().toString());
            }
        });

    }

    private void setClient(String s) {
        for (Client c : clients) {
            if (c.getFullName().equalsIgnoreCase(s)) {
                client = c;
                hideKeyboard();
                btnPolicy.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    private void confirm(final Client client) {
        AlertDialog.Builder x = new AlertDialog.Builder(this);
        x.setTitle("Confirm New Policy")
                .setMessage("Do you want to register a new Policy for \n\n".concat(client.getFullName()).concat(" ?"))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addSinglePolicy(client);
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private void addSinglePolicy(Client client) {
        showSnack("Registering policy ...", "ok", "yellow");
        int benCount = random.nextInt(3);
        if (benCount == 0) {
            benCount = 2;
        }
        policyBeneficiaries = new ArrayList<>();
        HashMap<String, Beneficiary> map = new HashMap<>();
        for (int i = 0; i < benCount; i++) {
            int index = random.nextInt(beneficiaries.size() - 1);
            Beneficiary beneficiary = beneficiaries.get(index);
            map.put(beneficiary.getIdNumber(), beneficiary);
        }

        for (Map.Entry<String, Beneficiary> b : map.entrySet()) {
            policyBeneficiaries.add(b.getValue());
        }

        Policy policy = new Policy();
        policy.setInsuranceCompany("resource:com.oneconnect.insurenet.InsuranceCompany#".concat(company.getInsuranceCompanyId()));
        policy.setInsuranceCompanyId(company.getInsuranceCompanyId());
        policy.setIdNumber(client.getIdNumber());
        policy.setPolicyNumber(ListUtil.getRandomPolicyNumber());
        policy.setClient("resource:com.oneconnect.insurenet.Client#".concat(client.getIdNumber()));
        policy.setDescription(ListUtil.getRandomDescription());
        policy.setAmount(ListUtil.getRandomPolicyAmount());
        policy.setClaimSubmitted(false);

        List<String> list = new ArrayList<>();
        for (Beneficiary b : policyBeneficiaries) {
            list.add("resource:com.oneconnect.insurenet.Beneficiary#".concat(b.getIdNumber()));
        }
        policy.setBeneficiaries(list);
        Log.w(TAG, "addSinglePolicy: beneficiaries:".concat(GSON.toJson(policyBeneficiaries)));

        chainDataAPI.registerPolicyViaTransaction(policy, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                Policy p = (Policy) data;
                Log.w(TAG, "onResponse: we seem to have a fucking policy: ".concat(GSON.toJson(p)));
                policies.add(0, p);
                setList();
                recyclerView.smoothScrollToPosition(0);
                btnPolicy.setVisibility(View.GONE);
                showSnack("Policy added to blockchain: ".concat(p.getPolicyNumber()), "ok", "green");
                FBApi api = new FBApi();
                api.addPolicy(p, new FBApi.FBListener() {
                    @Override
                    public void onResponse(Data data) {
                        Log.i(TAG, "onResponse: policy added to FB");
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

    private void setList() {
        txtCount.setText(String.valueOf(policies.size()));
        if (policyAdapter == null) {
            policyAdapter = new PolicyAdapter(policies, new PolicyAdapter.PolicyListener() {
                @Override
                public void onPolicyTapped(Policy p) {
                    processPolicy(p);
                }


            });
            recyclerView.setAdapter(policyAdapter);
        } else {
            policyAdapter.notifyDataSetChanged();
        }

    }

    private void processPolicy(final Policy policy) {
        AlertDialog.Builder x = new AlertDialog.Builder(this);
        x.setTitle("Claims Processing")
                .setMessage("Do you want to register a claim on this policy?")
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

    private FBApi fbApi = new FBApi();

    private Claim claim;

    private void registerClaim(Policy policy) {
        showSnack("Registering claim on the blockchain", "ok", "yellow");
        claim = new Claim();
        String[] strings = policy.getInsuranceCompany().split("#");
        claim.setCompanyId(strings[1]);
        claim.setDateTime(sdf.format(new Date()));
        claim.setClaimId(ListUtil.getRandomClaimId());
        claim.setPolicy("resource:com.oneconnect.insurenet.Policy#".concat(policy.getPolicyNumber()));
        claim.setInsuranceCompany("resource:com.oneconnect.insurenet.InsuranceCompanyy#".concat(strings[1]));
        claim.setPolicyNumber(policy.getPolicyNumber());
        chainDataAPI.submitClaim(claim, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                final Claim x = (Claim) data;
                showSnack("Claim registered: ".concat(x.getClaimId()), "OK", "green");
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


    private static Random random = new Random(System.currentTimeMillis());

    private void getClients() {
        chainListAPI.getClients(new ChainListAPI.ClientListener() {
            @Override
            public void onResponse(List<Client> list) {
                clients = list;
                Log.w(TAG, "onResponse: clients found on blockchain: " + list.size());
                setAuto();
                getBeneficiaries();

            }

            @Override
            public void onError(String message) {
                //showError(message);
            }
        });
    }

    private void getBeneficiaries() {
        chainListAPI.getBeneficiaries(new ChainListAPI.BeneficiaryListener() {
            @Override
            public void onResponse(List<Beneficiary> list) {
                beneficiaries = list;
                Log.w(TAG, "onResponse: beneficiaries found on blockchain: " + list.size());
                getPolicies();
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    private void getPolicies() {
        Snackbar.make(toolbar, "Refreshing policy list ...", Snackbar.LENGTH_LONG).show();
        chainListAPI.getCompanyPolicies(company.getInsuranceCompanyId(), new ChainListAPI.PolicyListener() {
            @Override
            public void onResponse(List<Policy> list) {
                Log.d(TAG, "onResponse: policies found on blockchain" + list.size());
                policies = list;
                txtCount.setText(df.format(policies.size()));
                setList();
            }

            @Override
            public void onError(String message) {
                showError(message);
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

    void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(auto.getWindowToken(), 0);
    }

    public static final String TAG = PolicyActivity.class.getSimpleName();
    public static final DecimalFormat df = new DecimalFormat("###,###,###,###");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    private void listen() {
        IntentFilter filterBurial = new IntentFilter(FCMMessagingService.BROADCAST_BURIAL);
        IntentFilter filterCert = new IntentFilter(FCMMessagingService.BROADCAST_CERT);
        IntentFilter filterClaim = new IntentFilter(FCMMessagingService.BROADCAST_CLAIM);
        IntentFilter filterPolicy = new IntentFilter(FCMMessagingService.BROADCAST_POLICY);

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.registerReceiver(new ClaimReceiver(), filterClaim);
        broadcastManager.registerReceiver(new BurialReceiver(), filterBurial);

    }


    private class ClaimReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "ClaimReceiver onReceive: @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            final Claim claimMsg = (Claim) intent.getSerializableExtra("data");
            if (claim.getClaimId().equalsIgnoreCase(claimMsg.getClaimId())) {
                Log.w(TAG, "onReceive: Claim that was issued HERE... so, no sweat!".concat(GSON.toJson(claimMsg)));
            } else {
                snackbar = Snackbar.make(toolbar, "Claim received: ".concat(claimMsg.getClaimId()), Snackbar.LENGTH_INDEFINITE);
                snackbar.setActionTextColor(Color.parseColor("yellow"));
                snackbar.setAction("Process", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        processClaim(claimMsg);
                    }
                });
                snackbar.show();
            }

        }
    }

    private void processClaim(Claim claim) {

        Intent m = new Intent(this,ClaimsActivity.class);
        m.putExtra("claim",claim);
        startActivity(m);
    }
    private class BurialReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "BurialReceiver onReceive: @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            Burial burial = (Burial) intent.getSerializableExtra("data");
            showSnack("Burial record received: ".concat(burial.getIdNumber()), "ok", "green");

        }
    }
}
