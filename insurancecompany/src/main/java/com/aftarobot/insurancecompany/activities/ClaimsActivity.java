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

import com.aftarobot.insurancecompany.R;
import com.aftarobot.insurancecompany.adapters.ClaimAdapter;
import com.aftarobot.mlibrary.api.ChainDataAPI;
import com.aftarobot.mlibrary.api.ChainListAPI;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.ClaimApproval;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.InsuranceCompany;
import com.aftarobot.mlibrary.util.ListUtil;
import com.aftarobot.mlibrary.util.SharedPrefUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ClaimsActivity extends AppCompatActivity {

    ChainListAPI chainListAPI;
    ChainDataAPI chainDataAPI;
    InsuranceCompany company;
    List<Claim> claims;
    Claim claim;
    Toolbar toolbar;
    RecyclerView recyclerView;
    ClaimAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claims);
        toolbar =  findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Claims Approval Process");

        chainListAPI = new ChainListAPI(this);
        chainDataAPI = new ChainDataAPI(this);
        company = SharedPrefUtil.getCompany(this);
        Objects.requireNonNull(getSupportActionBar()).setSubtitle(company.getName());
        getClaims();

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getClaims();
            }
        });
        claim = (Claim) getIntent().getSerializableExtra("claim");
        if (claim != null) {
            confirm(claim);
        }
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
            public void onClaimTapped(Claim claim) {
                confirm(claim);
            }
        });
        recyclerView.setAdapter(adapter);
    }
    private void confirm(final Claim claim) {
        AlertDialog.Builder x = new AlertDialog.Builder(this);
        x.setTitle("Claim Approval")
                .setMessage("Do you want to approve the payout for this claim? \n\n".concat(claim.getClaimId().concat(" ?")))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        approve(claim);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
    Snackbar snackbar;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private void approve(Claim claim) {

        ClaimApproval claimApproval = new ClaimApproval();
        claimApproval.setClaimApprovalId(ListUtil.getRandomClaimApprovalId());
        claimApproval.setClaim("resource:com.oneconnect.insurenet.Claim#".concat(claim.getClaimId()));
        claimApproval.setInsuranceCompany("resource:com.oneconnect.insurenet.InsuranceCompany#".concat(company.getInsuranceCompanyId()));
        claimApproval.setDateTime(sdf.format(new Date()));
        chainDataAPI.processClaimApproval(claimApproval, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                Log.i(TAG, "onResponse: claim approval done. Yay!");
                showSnackbar("Claim Approval processed successfully","ok","green" );
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
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
