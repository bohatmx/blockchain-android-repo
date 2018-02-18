package com.aftarobot.bank;

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
import android.widget.TextView;

import com.aftarobot.bank.services.FCMMessagingService;
import com.aftarobot.mlibrary.api.ChainDataAPI;
import com.aftarobot.mlibrary.api.ChainListAPI;
import com.aftarobot.mlibrary.api.FBApi;
import com.aftarobot.mlibrary.data.Bank;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.FundsTransfer;
import com.aftarobot.mlibrary.data.FundsTransferRequest;
import com.aftarobot.mlibrary.util.ListUtil;
import com.aftarobot.mlibrary.util.SharedPrefUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BankMainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ChainListAPI chainListAPI;
    ChainDataAPI chainDataAPI;
    TextView txtCount;
    RecyclerView recyclerView;
    Bank bank;
    List<FundsTransferRequest> requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_main);
        toolbar = findViewById(R.id.toolbar);
        txtCount = findViewById(R.id.txtCount);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Funds Transfer Requests");

        chainListAPI = new ChainListAPI(this);
        chainDataAPI = new ChainDataAPI(this);
        bank = SharedPrefUtil.getBank(this);
        getSupportActionBar().setSubtitle(bank.getName());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFundsTransferRequests();
            }
        });
        getFundsTransferRequests();
        listen();
    }

    private void getFundsTransferRequests() {
        Log.d(TAG, "getFundsTransferRequests: ############################# bank: "
        .concat(GSON.toJson(bank)));
        showSnackbar("Loading funds transfer requests ...", "wait", "yellow");
        chainListAPI.getFundsTransferRequests(bank.getBankId(), new ChainListAPI.FundsTransferRequestListener() {
            @Override
            public void onResponse(List<FundsTransferRequest> list) {
                Log.i(TAG, "onResponse: getFundsTransferRequests: " + list.size());
                Snackbar.make(toolbar, "Funds Transfer Requests found: "
                        .concat(String.valueOf(list.size())), Snackbar.LENGTH_LONG).show();
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
        FTRAdapter adapter = new FTRAdapter(requests, new FTRAdapter.FundsTransferRequestListener() {
            @Override
            public void onFundsTransferRequestTapped(FundsTransferRequest fundsTransferRequest) {
                confirm(fundsTransferRequest);
            }
        });

        txtCount.setText(String.valueOf(requests.size()));
        recyclerView.setAdapter(adapter);
    }

    private void confirm(final FundsTransferRequest request) {
        AlertDialog.Builder x = new AlertDialog.Builder(this);
        x.setTitle("Funds Transfer Request")
                .setMessage("Do you want to release funds for this request?\n\n"
                .concat(request.getFundsTransferRequestId().concat("\n")
                .concat(df.format(request.getAmount()))))
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendFunds(request);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
    FBApi fbApi;
    private void sendFunds(final FundsTransferRequest request) {
        Log.d(TAG, "sendFunds: request: ".concat(GSON.toJson(request)));
        final FundsTransfer transfer = new FundsTransfer();
        transfer.setAmount(request.getAmount());
        transfer.setBank(request.getBank());
        transfer.setBankId(request.getBankId());
        transfer.setInsuranceCompanyId(request.getInsuranceCompanyId());
        transfer.setDateTime(sdf.format(new Date()));
        transfer.setFromAccount(request.getFromAccount());
        transfer.setToAccount(request.getToAccount());
        transfer.setFundsTransferRequest("resource:com.oneconnect.insurenet.FundsTransferRequest#"
            .concat(request.getFundsTransferRequestId()));
        transfer.setInsuranceCompany(request.getInsuranceCompany());
        transfer.setClaim(request.getClaim());
        transfer.setFundsTransferId(ListUtil.getRandomTransferId());
        Log.e(TAG, "sendFunds: transfer: ".concat(GSON.toJson(transfer)) );

        showSnackbar("Sending the money ...", "ok", "yellow");
        chainDataAPI.transferFunds(transfer, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                Log.i(TAG, "onResponse: ");
                showSnackbar("Funds transferred: "
                        .concat(df.format(request.getAmount())), "ok", "green");
                fbApi = new FBApi();
                fbApi.addFundsTransfer(transfer, new FBApi.FBListener() {
                    @Override
                    public void onResponse(Data data) {
                        Log.w(TAG, "onResponse: funds transfer added to Firebase: ".concat(GSON.toJson(transfer)) );
                        requests.remove(request);
                        setList();
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

    private void listen() {
        IntentFilter f = new IntentFilter(FCMMessagingService.BROADCAST_FUNDS_TRANSFER_REQUEST);
        LocalBroadcastManager.getInstance(this).registerReceiver(new FundsTransferRequestReceiver(), f);
    }
    private class FundsTransferRequestReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive: FundsTransferRequestReceiver ###########" );
            FundsTransferRequest data = (FundsTransferRequest)intent.getSerializableExtra("data");
            Log.i(TAG, "FundsTransferRequestReceiver onReceive: "
            .concat(GSON.toJson(data)));
            showRequest(data);
        }
    }
    private void showRequest(FundsTransferRequest request) {
        if (requests == null) requests = new ArrayList<>();
        requests.add(0,request);
        setList();
        showSnackbar("Funds Transfer Request arrived: "
                .concat(request.getFundsTransferRequestId()), "ok", "yellow");

    }
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
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
        snackbar.setAction("error", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    public static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###,##0.00");
    public static final String TAG = BankMainActivity.class.getSimpleName();

}
