package com.aftarobot.insurancecompany.activities;

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
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
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
import com.aftarobot.insurancecompany.services.FCMMessagingService;
import com.aftarobot.mlibrary.api.ChainDataAPI;
import com.aftarobot.mlibrary.api.ChainListAPI;
import com.aftarobot.mlibrary.api.FBApi;
import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.Burial;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.DeathCertificate;
import com.aftarobot.mlibrary.data.InsuranceCompany;
import com.aftarobot.mlibrary.data.Policy;
import com.aftarobot.mlibrary.util.ListUtil;
import com.aftarobot.mlibrary.util.MyDialogFragment;
import com.aftarobot.mlibrary.util.SharedPrefUtil;
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
    private MyDialogFragment dialogFragment;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Client Management");
        company = SharedPrefUtil.getCompany(this);
        if (company != null) {
            getSupportActionBar().setSubtitle(company.getName());
        }
        chainListAPI = new ChainListAPI(this);
        chainDataAPI = new ChainDataAPI(this);
        fm = getFragmentManager();
        dialogFragment = new MyDialogFragment();

        getClients();
        getBennies();
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
        txtCount = findViewById(R.id.txtCount);
        txtTitle.setText("Clients");
        txtCount.setText("0");
        recyclerView = findViewById(R.id.recycler);
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);

    }

    private Client randomClient;
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final int MAX_CLIENTS = 5, MAX_BENNIES = 10;
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
        x.setTitle("Policy Registration ")
                .setMessage("Do you want to register a Policy for \n\n".concat(client.getFullName()
                        .concat(" ?\n\nThis policy will be registered on the blockchain")))
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
        showSnack("Registering policy to the blockchain ...","ok","yellow");
        Policy policy = new Policy();
        policy.setInsuranceCompany("resource:com.oneconnect.insurenet.InsuranceCompany#"
                .concat(company.getInsuranceCompanyId()));
        policy.setInsuranceCompanyId(company.getInsuranceCompanyId());
        policy.setIdNumber(client.getIdNumber());
        policy.setPolicyNumber(ListUtil.getRandomPolicyNumber());
        policy.setClient("resource:com.oneconnect.insurenet.Client#".concat(client.getIdNumber()));
        policy.setDescription(ListUtil.getRandomDescription());
        policy.setAmount(ListUtil.getRandomPolicyAmount());
        policy.setClaimSubmitted(false);

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
        showSnack("Registering policy on the Blockchain","ok","yellow");
        chainDataAPI.registerPolicyViaTransaction(policy, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                Policy p = (Policy) data;
                Log.e(TAG, "onResponse: we seem to have registered a fucking policy with a trabsaction!!!: ".concat(GSON.toJson(p)));

                showSnack("Policy registered: ".concat(p.getPolicyNumber()), "ok", "green");
                FBApi api = new FBApi();
                api.addPolicy(p, new FBApi.FBListener() {
                    @Override
                    public void onResponse(Data data) {
                        Log.i(TAG, "onResponse: policy added to Firebase");
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
    private void listen() {
        IntentFilter filterBurial = new IntentFilter(FCMMessagingService.BROADCAST_BURIAL);
        IntentFilter filterCert = new IntentFilter(FCMMessagingService.BROADCAST_CERT);
        IntentFilter filterClaim = new IntentFilter(FCMMessagingService.BROADCAST_CLAIM);
        IntentFilter filterPolicy = new IntentFilter(FCMMessagingService.BROADCAST_POLICY);

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.registerReceiver(new CertReceiver(),filterCert);
        broadcastManager.registerReceiver(new ClaimReceiver(),filterClaim);
        broadcastManager.registerReceiver(new BurialReceiver(),filterBurial);

    }
    private class ClaimReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context ctx, Intent m) {
            final Claim sentClaim = (Claim) m.getSerializableExtra("data");
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
            final Burial sentBurial = (Burial) m.getSerializableExtra("data");
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

}
