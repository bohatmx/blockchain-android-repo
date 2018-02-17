package com.aftarobot.insurancecompany.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.aftarobot.insurancecompany.R;
import com.aftarobot.insurancecompany.adapters.ClaimAdapter;
import com.aftarobot.mlibrary.api.ChainDataAPI;
import com.aftarobot.mlibrary.api.ChainListAPI;
import com.aftarobot.mlibrary.api.FBApi;
import com.aftarobot.mlibrary.data.Bank;
import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.ClaimApproval;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.FundsTransferRequest;
import com.aftarobot.mlibrary.data.InsuranceCompany;
import com.aftarobot.mlibrary.data.Policy;
import com.aftarobot.mlibrary.util.ListUtil;
import com.aftarobot.mlibrary.util.SharedPrefUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ClaimsActivity extends AppCompatActivity {

    ChainListAPI chainListAPI;
    ChainDataAPI chainDataAPI;
    FBApi fbApi;
    InsuranceCompany company;
    List<Claim> claims;
    Claim claim;
    Toolbar toolbar;
    RecyclerView recyclerView;
    ClaimAdapter adapter;
    Spinner spinner;
    TextView txtCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claims);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler);
        spinner = findViewById(R.id.spinner);
        txtCount = findViewById(R.id.txtCount);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Claims Approval Process");

        chainListAPI = new ChainListAPI(this);
        chainDataAPI = new ChainDataAPI(this);
        fbApi = new FBApi();
        company = SharedPrefUtil.getCompany(this);
        Objects.requireNonNull(getSupportActionBar()).setSubtitle(company.getName());
        getBanks();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getClaims();
            }
        });
        claim = (Claim) getIntent().getSerializableExtra("claim");
        if (claim != null) {
            confirm();
        }
    }

    List<Bank> banks;

    private void getBanks() {
        chainListAPI.getBanks(new ChainListAPI.BankListener() {
            @Override
            public void onResponse(List<Bank> list) {
                banks = list;
                getClaims();
                bankNames = new ArrayList<>(banks.size());
                for (Bank b : banks) {
                    bankNames.add(b.getName());
                }

            }

            @Override
            public void onError(String message) {
                getClaims();
                showError(message);
            }
        });
    }

    private void getClaims() {
        chainListAPI.getCompanyClaims(company.getInsuranceCompanyId(), new ChainListAPI.ClaimsListener() {
            @Override
            public void onResponse(List<Claim> list) {
                claims = list;
                setList();
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    private void setList() {
        adapter = new ClaimAdapter(claims, new ClaimAdapter.ClaimListener() {
            @Override
            public void onClaimTapped(Claim c) {
                claim = c;
                confirm();
            }
        });
        recyclerView.setAdapter(adapter);
        txtCount.setText(String.valueOf(claims.size()));
    }

    private MaterialDialog mdg;

    List<String> bankNames;
    Bank bank;

    private void confirm() {

        mdg = new MaterialDialog.Builder(this)
                .title("Select Bank")
                .items(bankNames)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        return true;
                    }
                })
                .positiveText("Approve Claim")
                .negativeText("Decline Claim")
                .neutralText("Cancel")
                .show();

        mdg.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_attach_money));
        mdg.getActionButton(DialogAction.POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selected = bankNames.get(mdg.getSelectedIndex());
                boolean isFound = false;
                for (Bank b : banks) {
                    if (b.getName().equalsIgnoreCase(selected)) {
                        bank = b;
                        isFound = true;
                        break;
                    }
                }
                mdg.dismiss();
                if (isFound) {
                    approve(claim, true);
                }

            }
        });
        mdg.getActionButton(DialogAction.NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approve(claim, false);
                mdg.dismiss();
            }
        });
    }

    Snackbar snackbar;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    private void approve(final Claim claim, final boolean approved) {

        if (approved) {
            showSnackbar("Submitting claim approval: YES", "ok", "yellow");
        } else {
            showSnackbar("Submitting claim declined: NO", "ok", "red");
        }
        ClaimApproval claimApproval = getClaimApproval(claim, approved);

        chainDataAPI.processClaimApproval(claimApproval, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                Log.i(TAG, "onResponse: claim submission done. Yay!");

                if (approved) {
                    showSnackbar("Claim Approval processed successfully", "ok", "green");
                    requestFundsTransfer(claim);
                } else {
                    showSnackbar("Claim Declined processed successfully", "ok", "yellow");
                }
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    private void requestFundsTransfer(Claim claim) {

        chainListAPI.getPolicy(claim.getPolicyNumber(), new ChainListAPI.PolicyListener() {
            @Override
            public void onResponse(List<Policy> policies) {
                if (!policies.isEmpty()) {
                    policy = policies.get(0);
                    controlBeneficiaries();
                }
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });


    }

    int index;
    Policy policy;

    private void controlBeneficiaries() {
        if (!policy.getBeneficiaries().isEmpty()) {
            String ben = policy.getBeneficiaries().get(0);
            processBeneficiary(ben);
        }
    }


    private void processBeneficiary(String ben) {
        String[] strings = ben.split("#");
        String idNumber = strings[1];
        chainListAPI.getBeneficiary(idNumber, new ChainListAPI.BeneficiaryListener() {
            @Override
            public void onResponse(List<Beneficiary> beneficiaries) {
                if (!beneficiaries.isEmpty()) {
                    Beneficiary beneficiary = beneficiaries.get(0);
                    if (beneficiary.getBankAccounts() != null && !beneficiary.getBankAccounts().isEmpty()) {
                        String acct = beneficiary.getBankAccounts().get(0);
                        String[] strings1 = acct.split("#");
                        addFundsTransferRequest(strings1[1]);
                    }
                }
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private void addFundsTransferRequest(String fromAccountNumber) {
        final FundsTransferRequest ftr = new FundsTransferRequest();
        ftr.setAmount(policy.getAmount());
        String bankId;
        if (bank == null) {
            bankId = "BANK_001";
        } else {
            bankId = bank.getBankId();
        }

        ftr.setBank("resource:com.oneconnect.insurenet.Bank#".concat(bankId));
        ftr.setClaim("resource:com.oneconnect.insurenet.Claim#".concat(claim.getClaimId()));
        ftr.setDateTime(sdf.format(new Date()));
        ftr.setFundsTransferRequestId(ListUtil.getRandomFTRid());
        ftr.setInsuranceCompany("resource:com.oneconnect.insurenet.InsuranceCompany#"
                .concat(company.getInsuranceCompanyId()));
        ftr.setToAccount("resource:com.oneconnect.insurenet.BankAccount#"
                .concat(fromAccountNumber));
        ftr.setFromAccount("resource:com.oneconnect.insurenet.BankAccount#"
                .concat(company.getInsuranceCompanyId()));

        Log.d(TAG, "addFundsTransferRequest: ftr: ".concat(GSON.toJson(ftr)));
        showSnackbar("Requesting funds transfers", "ok", "yellow");
        chainDataAPI.requestFundsTransfer(ftr, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                showSnackbar("Funds Transfer Request processed", "ok", "green");
                fbApi.addFundsTransferRequest(ftr, new FBApi.FBListener() {
                    @Override
                    public void onResponse(Data data) {
                        Log.i(TAG, "onResponse: Funds Transfer Request added to Firebase");
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

    @NonNull
    private ClaimApproval getClaimApproval(Claim claim, boolean approved) {
        ClaimApproval claimApproval = new ClaimApproval();
        claimApproval.setClaimApprovalId(ListUtil.getRandomClaimApprovalId());
        claimApproval.setClaim("resource:com.oneconnect.insurenet.Claim#".concat(claim.getClaimId()));
        claimApproval.setInsuranceCompany("resource:com.oneconnect.insurenet.InsuranceCompany#".concat(company.getInsuranceCompanyId()));
        claimApproval.setDateTime(sdf.format(new Date()));
        claimApproval.setApproved(approved);
        return claimApproval;
    }

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

    public static final String TAG = ClaimsActivity.class.getSimpleName();
}
