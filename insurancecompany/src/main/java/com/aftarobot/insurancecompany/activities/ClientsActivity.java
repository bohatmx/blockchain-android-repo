package com.aftarobot.insurancecompany.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aftarobot.insurancecompany.R;
import com.aftarobot.insurancecompany.adapters.ClientAdapter;
import com.aftarobot.mlibrary.ListUtil;
import com.aftarobot.mlibrary.SharedPrefUtil;
import com.aftarobot.mlibrary.api.ChainDataAPI;
import com.aftarobot.mlibrary.api.ChainListAPI;
import com.aftarobot.mlibrary.api.FBApi;
import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.InsuranceCompany;
import com.aftarobot.mlibrary.data.Policy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ClientsActivity extends AppCompatActivity {

    private TextView txtTitle, txtCount;
    private ImageView imgAdd;
    private RecyclerView recyclerView;
    private ChainListAPI chainListAPI;
    private List<Client> clients;
    private ClientAdapter clientAdapter;
    private Toolbar toolbar;
    private ChainDataAPI chainDataAPI;
    private InsuranceCompany company;
    private List<Beneficiary> beneficiaries, policyBeneficiaries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Client Management");
        company = SharedPrefUtil.getCompany(this);
        if (company != null) {
            getSupportActionBar().setSubtitle(company.getName());
        }
        chainListAPI = new ChainListAPI(this);
        chainDataAPI = new ChainDataAPI(this);
        getClients();
        getBennies();

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
        txtTitle.setText("Clients");
        txtCount.setText("0");
        imgAdd = findViewById(R.id.icon);
        recyclerView = findViewById(R.id.recycler);
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSnack("Generating random clients ...","ok","yellow");
                count = 0;
                control();
            }
        });
    }

    private Client randomClient;
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final int MAX_CLIENTS = 50, MAX_BENNIES = 100;
    private int count;

    private void getBennies() {
        chainListAPI.getBeneficiaries(new ChainListAPI.BeneficiaryListener() {
            @Override
            public void onResponse(List<Beneficiary> list) {
                beneficiaries = list;
            }

            @Override
            public void onError(String message) {

            }
        });
    }
    private void control() {
        if (count < MAX_CLIENTS) {
            addRandomClient();
        } else {

            count = 0;
            controlBennies();
        }

    }

    private void addRandomClient() {
        Client client = ListUtil.getRandomClient();
        chainDataAPI.addClient(client, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                randomClient = (Client) data;
                Log.w(TAG, "onResponse: random client added: "
                        .concat(GSON.toJson(randomClient)));
                clients.add(0, randomClient);
                Log.e(TAG, "onResponse: total clients:".concat(String.valueOf(clients.size())) );
                setList();
                recyclerView.smoothScrollToPosition(0);
                count++;
                control();

            }

            @Override
            public void onError(String message) {
                Log.e(TAG, "onError: ".concat(message));
                showError(message);
            }
        });
    }

    private void controlBennies() {
        if (count < MAX_BENNIES) {
            addRandomBenny();
        } else {
            showSnack("Clients and Beneficiaries generated", "OK", "green");
        }

    }

    private void addRandomBenny() {

        Beneficiary beneficiary = ListUtil.getRandomBeneficiary();
        chainDataAPI.addBeneficiary(beneficiary, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                Beneficiary v = (Beneficiary) data;
                count++;
                Log.i(TAG, "onResponse: bennie added to blockchain".concat(GSON.toJson(v)));
                Log.e(TAG, "onResponse: total beneficiaries:".concat(String.valueOf(count)) );

                controlBennies();
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });

    }

    private void setList() {
        Collections.sort(clients);
        txtCount.setText(String.valueOf(clients.size()));
        if (clientAdapter == null) {
            clientAdapter = new ClientAdapter(clients, new ClientAdapter.ClientListener() {
                @Override
                public void onClientTapped(Client client) {
                    processClient(client);
                }

                @Override
                public void onEmailTapped(Client client) {
                    processEmail(client);
                }
            });
            recyclerView.setAdapter(clientAdapter);
        } else {
            clientAdapter.notifyDataSetChanged();
        }

    }

    private void processClient(final Client client) {
        AlertDialog.Builder x = new AlertDialog.Builder(this);
        x.setTitle("Policy Sale")
                .setMessage("Do you want to buy a Policy?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addPolicy(client);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private Random random = new Random(System.currentTimeMillis());
    private void addPolicy(Client client) {
        showSnack("Adding policy to blockchain...","ok","yellow");
        Policy policy = new Policy();
        policy.setInsuranceCompany("resource:com.oneconnect.insurenet.InsuranceCompany#".concat(company.getInsuranceCompanyID()));
        policy.setPolicyNumber(ListUtil.getRandomPolicyNumber());
        policy.setClient("resource:com.oneconnect.insurenet.Client#".concat(client.getIdNumber()));
        policy.setDescription(ListUtil.getRandomDescription());
        policy.setAmount(ListUtil.getRandomPolicyAmount());

        policyBeneficiaries = new ArrayList<>();
        HashMap<String, Beneficiary> map = new HashMap<>();
        int benCount = random.nextInt(4);
        if (benCount == 0) {
            benCount = 1;
        }
        for (int i = 0; i < benCount; i++) {
            int index = random.nextInt(beneficiaries.size() - 1);
            Beneficiary beneficiary = beneficiaries.get(index);
            map.put(beneficiary.getIdNumber(), beneficiary);
        }

        for (Map.Entry<String, Beneficiary> b : map.entrySet()) {
            policyBeneficiaries.add(b.getValue());
        }

        List<String> list = new ArrayList<>();
        for (Beneficiary b : policyBeneficiaries) {
            list.add("resource:com.oneconnect.insurenet.Beneficiary#".concat(b.getIdNumber()));
        }
        policy.setBeneficiaries(list);

        chainDataAPI.addPolicy(policy, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                Policy p = (Policy) data;
                Log.w(TAG, "onResponse: we seem to have sold a fucking policy: ".concat(GSON.toJson(p)));

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
    private void processEmail(Client client) {
        Snackbar.make(toolbar, "Client: ".concat(client.getEmail()), Snackbar.LENGTH_LONG).show();
    }

    private void getClients() {
        Snackbar.make(toolbar, "Refreshing client list ...", Snackbar.LENGTH_LONG).show();
        chainListAPI.getClients(new ChainListAPI.ClientListener() {
            @Override
            public void onResponse(List<Client> list) {
                Log.d(TAG, "onResponse: clients found on blockchain" + list.size());
                clients = list;
                setList();
                if (clients.isEmpty()) {
                    showSnack("Generating client list", "ok", "cyan");
                    count = 0;
                    control();
                }
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

    public static final String TAG = ClientsActivity.class.getSimpleName();
}
