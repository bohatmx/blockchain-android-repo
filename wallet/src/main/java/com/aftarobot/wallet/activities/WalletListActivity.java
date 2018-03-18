package com.aftarobot.wallet.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aftarobot.mlibrary.api.FBApi;
import com.aftarobot.mlibrary.api.FBListApi;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.Payment;
import com.aftarobot.mlibrary.data.Wallet;
import com.aftarobot.mlibrary.util.SharedPrefUtil;
import com.aftarobot.wallet.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collections;
import java.util.List;

public class WalletListActivity extends AppCompatActivity {

    FBListApi listApi = new FBListApi();
    RecyclerView recyclerView;
    Toolbar toolbar;
    WalletAdapter adapter;
    Wallet wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_list);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        wallet = SharedPrefUtil.getWallet(this);
        getSupportActionBar().setTitle("Wallet Contacts");
        getSupportActionBar().setSubtitle(wallet.getName());

        FloatingActionButton fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWallets();
            }
        });

        getWallets();
    }

    public void showPaymentDialog(final Wallet w) {

        AlertDialog.Builder diag = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        diag.setView(dialogView);

        final TextInputEditText edt = dialogView.findViewById(R.id.editAmount);
        final TextView txtName = dialogView.findViewById(R.id.txtName);
        txtName.setText(w.getName());

        diag.setTitle("Payment Process");
        diag.setMessage("Enter the Payment Amount below");
        diag.setPositiveButton("Send Payment", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (TextUtils.isEmpty(edt.getText())) {
                    Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_SHORT).show();
                } else {
                    sendMoney(w, edt.getText().toString());
                }
            }
        });
        diag.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = diag.create();
        b.show();
    }

    private void sendMoney(Wallet w, String amount) {
        showSnack("Sending the big bucks ...", "ok", "cyan");

        FBApi api = new FBApi();
        Payment p = new Payment();
        p.setFromFCMToken(SharedPrefUtil.getCloudMsgToken(this));
        p.setToFCMToken(w.getFcmToken());
        p.setDestinationAccount(w.getAccountID());
        p.setSourceAccount(wallet.getAccountID());
        p.setAmount(amount);
        p.setSeed(SharedPrefUtil.getSecret(this));
        p.setMemo("Joy, absolutely!");

        Log.d(TAG, "sendMoney: from Wallet: ".concat(GSON.toJson(wallet)));
        Log.e(TAG, "sendMoney: payment: ".concat(GSON.toJson(p)) );

        api.addPayment(p, new FBApi.FBListener() {
            @Override
            public void onResponse(Data data) {

            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    private void getWallets() {
        showSnackIndefinite("Loading friends' wallets","ok","cyan");
        listApi.getWallets(new FBListApi.WalletListener() {
            @Override
            public void onResponse(List<Wallet> list) {
                wallets = list;
                Collections.sort(wallets);
                setList();
                snackbar.dismiss();
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    private void setList() {
        adapter = new WalletAdapter(wallets, this, new WalletAdapter.WalletListener() {
            @Override
            public void onWalletTapped(Wallet wallet) {
                showPaymentDialog(wallet);
            }
        });
        recyclerView.setAdapter(adapter);

    }

    private List<Wallet> wallets;
    private Snackbar snackbar;
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final String TAG = WalletListActivity.class.getSimpleName();


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
        snackbar = Snackbar.make(toolbar, message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    private void showSnackIndefinite(String message, String action, String color) {
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

}
