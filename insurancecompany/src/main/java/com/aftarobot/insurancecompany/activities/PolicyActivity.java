package com.aftarobot.insurancecompany.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aftarobot.insurancecompany.R;
import com.aftarobot.insurancecompany.adapters.ListUtil;
import com.aftarobot.insurancecompany.adapters.PolicyAdapter;
import com.aftarobot.mlibrary.SharedPrefUtil;
import com.aftarobot.mlibrary.api.ChainDataAPI;
import com.aftarobot.mlibrary.api.ChainListAPI;
import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.InsuranceCompany;
import com.aftarobot.mlibrary.data.Policy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PolicyActivity extends AppCompatActivity {

    private TextView txtTitle, txtCount;
    private ImageView imgAdd;
    private RecyclerView recyclerView;
    private ChainListAPI chainListAPI;
    private List<Policy> policies;
    private PolicyAdapter policyAdapter;
    private Toolbar toolbar;
    private ChainDataAPI chainDataAPI;
    private InsuranceCompany company;
    private List<Client> clients;
    private List<Beneficiary> beneficiaries;

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
        getClients();

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
        txtCount = findViewById(R.id.txtCount);
        txtTitle.setText("Policies");
        txtCount.setText("0");
        imgAdd = findViewById(R.id.icon);
        recyclerView = findViewById(R.id.recycler);
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(toolbar, "Generating random policy?", Snackbar.LENGTH_SHORT).show();
                count = 0;
                addRandomPolicy();
            }
        });
    }
    int count;
    List<Beneficiary> policyBeneficiaries;
    private void addRandomPolicy() {
        if (count > 100) {
            showError("Seems all clients have policies");
            return;
        }
        Snackbar.make(toolbar,"Generating policy ...",Snackbar.LENGTH_LONG).show();
        int clientIndex = random.nextInt(clients.size() - 1);
        Client client = clients.get(clientIndex);
        for (Policy p: policies) {
            if (p.getClient().contains(client.getIdNumber())) {
                count++;
                addRandomPolicy();
            }
        }
        int benCount = random.nextInt(5);
        if (benCount == 0) {
            benCount = 2;
        }
        policyBeneficiaries = new ArrayList<>();
        HashMap<String, Beneficiary> map = new HashMap<>();
        for (int i = 0; i < benCount; i++) {
            int index = random.nextInt(beneficiaries.size() - 1);
            Beneficiary beneficiary = beneficiaries.get(index);
            map.put(beneficiary.getIdNumber(),beneficiary);
        }

        for (Map.Entry<String,Beneficiary> b: map.entrySet()) {
            policyBeneficiaries.add(b.getValue());
        }

        Policy policy = new Policy();
        policy.setInsuranceCompany("resource:com.oneconnect.insurenet.InsuranceCompany#".concat(company.getInsuranceCompanyID()));
        policy.setPolicyNumber(ListUtil.getRandomPolicyNumber());
        policy.setClient("resource:com.oneconnect.insurenet.Client#".concat(client.getIdNumber()));
        policy.setDescription(ListUtil.getRandomDescription());
        policy.setAmount(ListUtil.getRandomPolicyAmount());

        List<String> list = new ArrayList<>();
        for (Beneficiary b: policyBeneficiaries) {
            list.add("resource:com.oneconnect.insurenet.Beneficiary#".concat(b.getIdNumber()));
        }
        policy.setBeneficiaries(list);

        chainDataAPI.addPolicy(policy, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                Policy p = (Policy)data;
                Log.w(TAG, "onResponse: we seem to have a fucking policy: ".concat(GSON.toJson(p)) );
                policies.add(0,p);
                setList();
                recyclerView.smoothScrollToPosition(0);
                showSnack("Policy added to blockchain","ok","green");
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

    private void processPolicy(Policy client) {
        Snackbar.make(toolbar, "Policy: ".concat(client.getPolicyNumber()), Snackbar.LENGTH_LONG).show();
    }

    private void getClients() {
        chainListAPI.getClients(new ChainListAPI.ClientListener() {
            @Override
            public void onResponse(List<Client> list) {
                clients = list;
                Log.w(TAG, "onResponse: clients found on blockchain: " + list.size());
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
        chainListAPI.getPolicies(new ChainListAPI.PolicyListener() {
            @Override
            public void onResponse(List<Policy> list) {
                Log.d(TAG, "onResponse: policies found on blockchain" + list.size());

                policies = new ArrayList<>();
                for (Policy p: list) {
                    if (p.getInsuranceCompany().contains(company.getInsuranceCompanyID())) {
                        policies.add(p);
                    }
                }
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

    public static final String TAG = PolicyActivity.class.getSimpleName();
    public static final DecimalFormat df = new DecimalFormat("###,###,###,###");
    private Random random = new Random(System.currentTimeMillis());
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();


}
