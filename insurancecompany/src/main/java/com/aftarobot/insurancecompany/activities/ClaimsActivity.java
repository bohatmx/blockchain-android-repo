package com.aftarobot.insurancecompany.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
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
import com.aftarobot.insurancecompany.services.FCMMessagingService;
import com.aftarobot.insurancecompany.services.NotifyBeneficiaryClaim;
import com.aftarobot.insurancecompany.services.NotifyBeneficiaryFunds;
import com.aftarobot.mlibrary.api.ChainDataAPI;
import com.aftarobot.mlibrary.api.ChainListAPI;
import com.aftarobot.mlibrary.api.FBApi;
import com.aftarobot.mlibrary.api.FBListApi;
import com.aftarobot.mlibrary.data.Bank;
import com.aftarobot.mlibrary.data.BankAccount;
import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.BeneficiaryThanks;
import com.aftarobot.mlibrary.data.Burial;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.ClaimApproval;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.DeathCertificate;
import com.aftarobot.mlibrary.data.FundsTransfer;
import com.aftarobot.mlibrary.data.FundsTransferRequest;
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
        listen();
        fm = getFragmentManager();

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

    private void approve(final Claim c, final boolean approved) {
        claim = c;
        if (approved) {
            showSnackbar("Submitting claim approval: YES", "ok", "yellow");
        } else {
            showSnackbar("Submitting claim declined: NO", "ok", "red");
        }
        final ClaimApproval claimApproval = getClaimApproval(claim, approved);

        chainDataAPI.processClaimApproval(claimApproval, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                Log.i(TAG, "onResponse: claim submission done. Yay!");

                if (approved) {
                    showSnackbar("Claim Approval processed. Starting beneficiary notification", "ok", "yellow");
                    NotifyBeneficiaryClaim.notify(getApplicationContext(), claim, new NotifyBeneficiaryClaim.NotifyListener() {
                        @Override
                        public void onNotified() {
                            requestFundsTransfer();
                        }

                        @Override
                        public void onProgress(String message) {
                            showSnackbar(message, "ok", "green");
                        }

                        @Override
                        public void onError(String message) {
                            showError(message);
                        }
                    });


                } else {
                    showSnackbar("Claim Declined processed successfully", "ok", "yellow");
                }
                claims.remove(claim);
                setList();
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }


    private void requestFundsTransfer() {
        showSnackbar("Requesting Policy ...", "ok", "cyan");
        chainListAPI.getPolicy(claim.getPolicyNumber(), new ChainListAPI.PolicyListener() {
            @Override
            public void onResponse(List<Policy> policies) {
                if (!policies.isEmpty()) {
                    policy = policies.get(0);
                    index = 0;
                    processBeneficiary();
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

    private void processBeneficiary() {
        if (!policy.getBeneficiaries().isEmpty()) {
            String ben = policy.getBeneficiaries().get(0);
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
    }

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private void addFundsTransferRequest(String toAccountNumber) {
        final FundsTransferRequest fundsTransferRequest = new FundsTransferRequest();
        fundsTransferRequest.setAmount(policy.getAmount());
        String bankId;
        if (bank == null) {
            bankId = "BANK_001";
        } else {
            bankId = bank.getBankId();
        }

        fundsTransferRequest.setBank("resource:com.oneconnect.insurenet.Bank#".concat(bankId));
        fundsTransferRequest.setClaim("resource:com.oneconnect.insurenet.Claim#".concat(claim.getClaimId()));
        fundsTransferRequest.setBankId(bankId);
        fundsTransferRequest.setInsuranceCompanyId(company.getInsuranceCompanyId());
        fundsTransferRequest.setDateTime(sdf.format(new Date()));
        fundsTransferRequest.setFundsTransferRequestId(ListUtil.getRandomFTRid());
        fundsTransferRequest.setInsuranceCompany("resource:com.oneconnect.insurenet.InsuranceCompany#"
                .concat(company.getInsuranceCompanyId()));
        fundsTransferRequest.setToAccount("resource:com.oneconnect.insurenet.BankAccount#"
                .concat(toAccountNumber));
        fundsTransferRequest.setFromAccount("resource:com.oneconnect.insurenet.BankAccount#"
                .concat(company.getInsuranceCompanyId()));

        Log.d(TAG, "addFundsTransferRequest: fundsTransferRequest: ".concat(GSON.toJson(fundsTransferRequest)));
        if (fundsTransferRequest.getBankId() == null) {
            throw new RuntimeException("bankId is NULL ########################");
        } else {
            Log.w(TAG, "addFundsTransferRequest: bankId: ".concat(fundsTransferRequest.getBankId()));
        }
        showSnackbar("Requesting funds transfers", "ok", "white");
        chainDataAPI.requestFundsTransfer(fundsTransferRequest, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                showSnackbar("Funds Transfer Request processed", "ok", "green");
                fbApi.addFundsTransferRequest(fundsTransferRequest, new FBApi.FBListener() {
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

    private void listen() {
        IntentFilter filterBurial = new IntentFilter(FCMMessagingService.BROADCAST_BURIAL);
        IntentFilter filterCert = new IntentFilter(FCMMessagingService.BROADCAST_CERT);
        IntentFilter filterClaim = new IntentFilter(FCMMessagingService.BROADCAST_CLAIM);
        IntentFilter filterFundsTransfer = new IntentFilter(FCMMessagingService.BROADCAST_FUNDS_TRANSFER);

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);

        ClaimReceiver receiver = new ClaimReceiver();
        broadcastManager.registerReceiver(receiver, filterClaim);

        CertReceiver receiverC = new CertReceiver();
        broadcastManager.registerReceiver(receiverC, filterCert);

        BurialReceiver receiverB = new BurialReceiver();
        broadcastManager.registerReceiver(receiverB, filterBurial);

        FundsTransferReceiver fundsTransferReceiver = new FundsTransferReceiver();
        broadcastManager.registerReceiver(fundsTransferReceiver, filterFundsTransfer);

    }

    private DeathCertificate sentDC;
    private Burial sentBurial;
    private Claim sentClaim;

    private class ClaimReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context ctx, Intent m) {
            sentClaim = (Claim) m.getSerializableExtra("data");
            getClaims();
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
            sentBurial = (Burial) m.getSerializableExtra("data");
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
            sentDC = (DeathCertificate) m.getSerializableExtra("data");
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

        Intent m = new Intent(this, ClaimsActivity.class);
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

    android.app.FragmentManager fm;

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

    private class FundsTransferReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive: FundsTransferReceiver ###########");
            FundsTransfer data = (FundsTransfer) intent.getSerializableExtra("data");
            Log.i(TAG, "FundsTransferReceiver onReceive: "
                    .concat(GSON.toJson(data)));
            showTransfer(data);
        }
    }

    public static final DecimalFormat fmt = new DecimalFormat("###,###,###,###,###,###,###,###,###,###,##0.00");

    private void showTransfer(final FundsTransfer transfer) {

        Snackbar.make(toolbar, "Funds Transfer arrived: "
                .concat(fmt.format(transfer.getAmount())), Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(Color.parseColor("green"))
                .setAction("Thank Beneficiary", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        thanks(transfer);
                    }
                }).show();


    }

    private FBListApi fbListApi;

    private void thanks(final FundsTransfer fundsTransfer) {
        fbListApi = new FBListApi();
        String accountNumber = fundsTransfer.getToAccount().split("#")[1];
        chainListAPI.getBankAccount(accountNumber, new ChainListAPI.BankAccountListener() {
            @Override
            public void onResponse(List<BankAccount> bankAccounts) {
                if (!bankAccounts.isEmpty()) {
                    BankAccount bacct = bankAccounts.get(0);
                    Log.i(TAG, "onResponse: bankAccount: ".concat(GSON.toJson(bacct)));
                    final String idNumber = bacct.getBeneficiary().split("#")[1];
                    chainListAPI.getBeneficiary(idNumber, new ChainListAPI.BeneficiaryListener() {
                        @Override
                        public void onResponse(List<Beneficiary> beneficiaries) {
                            if (!beneficiaries.isEmpty()) {
                                Beneficiary m = beneficiaries.get(0);
                                fbListApi.getBeneficiaryByIDnumber(idNumber, new FBListApi.BeneficiaryListener() {
                                    @Override
                                    public void onResponse(List<Beneficiary> beneficiaries) {
                                        if (!beneficiaries.isEmpty()) {
                                            Beneficiary fbBeneficiary = beneficiaries.get(0);
                                            Log.w(TAG, "onResponse: fbBeneficiary: ".concat(GSON.toJson(fbBeneficiary)) );
                                            //we have an fcmToken
                                            BeneficiaryThanks thanks = new BeneficiaryThanks();
                                            thanks.setFcmToken(fbBeneficiary.getFcmToken());
                                            thanks.setFundsTransfer(fundsTransfer);
                                            thanks.setName(fbBeneficiary.getFullName());
                                            thanks.setDate(new Date().getTime());
                                            thanks.setMessage("Thank you very much for allowing "
                                                    .concat(company.getName().concat(" to serve you. We wish you the best of luck and prosperity!")));
                                            Log.d(TAG, "onResponse: thanks: ".concat(GSON.toJson(thanks)));
                                            fbApi.addBeneficiaryThanks(thanks, new FBApi.FBListener() {
                                                @Override
                                                public void onResponse(Data data) {
                                                    Log.e(TAG, "onResponse: beneficiary THANKED via Firebase!");
                                                }

                                                @Override
                                                public void onError(String message) {
                                                    showError(message);
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
                        }

                        @Override
                        public void onError(String message) {
                            showError(message);
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


}
