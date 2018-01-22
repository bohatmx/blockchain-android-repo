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
import com.aftarobot.insurancecompany.adapters.ClientAdapter;
import com.aftarobot.insurancecompany.adapters.ListUtil;
import com.aftarobot.mlibrary.SharedPrefUtil;
import com.aftarobot.mlibrary.api.ChainDataAPI;
import com.aftarobot.mlibrary.api.ChainListAPI;
import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.InsuranceCompany;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Client Management");
        InsuranceCompany company = SharedPrefUtil.getCompany(this);
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
        txtTitle.setText("Clients");
        txtCount.setText("0");
        imgAdd = findViewById(R.id.icon);
        recyclerView = findViewById(R.id.recycler);
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(toolbar, "Generating random clients ...", Snackbar.LENGTH_SHORT).show();
                control();
            }
        });
    }

    private Client randomClient;
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private int count;
    private void control() {
        if (count < 10) {
            addRandomClient();
        } else {
            addRandomBennies();
        }

    }
    private void addRandomClient() {
        Client client = ListUtil.getRandomClient();
        chainDataAPI.addClient(client, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                randomClient = (Client) data;
                Log.w(TAG, "onResponse: random client added: ".concat(GSON.toJson(randomClient)) );
                clients.add(0,randomClient);
                setList();
                recyclerView.smoothScrollToPosition(0);
                count++;
                control();

            }

            @Override
            public void onError(String message) {
                Log.e(TAG, "onError: ".concat(message) );
            }
        });


    }
    private Random random = new Random(System.currentTimeMillis());
    private void addRandomBennies() {
        int count = random.nextInt(20);
        if (count < 10) {
            count = 10;
        }
        showSnack("Generating random beneficiaries: " + count,"ok","yellow");
        for (int i = 0; i < count; i++) {
            Beneficiary beneficiary = ListUtil.getRandomBeneficiary();
            chainDataAPI.addBeneficiary(beneficiary, new ChainDataAPI.Listener() {
                @Override
                public void onResponse(Data data) {
                    Beneficiary v = (Beneficiary)data;
                    Log.i(TAG, "onResponse: bennie added to blockchain".concat(GSON.toJson(v)));
                }

                @Override
                public void onError(String message) {
                    showError(message);
                }
            });
        }
    }
    private void setList() {
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

    private void processClient(Client client) {
        Snackbar.make(toolbar, "Client: ".concat(client.getFirstName()), Snackbar.LENGTH_LONG).show();
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
            }

            @Override
            public void onError(String message) {

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

    public static final String TAG = ClientsActivity.class.getSimpleName();
}
