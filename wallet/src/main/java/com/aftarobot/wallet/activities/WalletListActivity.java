package com.aftarobot.wallet.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.LocalBroadcastManager;
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
import com.aftarobot.wallet.services.FCMMessagingService;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWallets();
            }
        });

        listen();
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
                Log.e(TAG, "onWalletTapped: ".concat(GSON.toJson(wallet)) );
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

    int[] themes = {
            R.style.AftaRobotTheme, R.style.CommuterTheme,
            R.style.DriverTheme, R.style.MarshalTheme, R.style.AdminTheme,
            R.style.OwnerTheme, R.style.RouteBuilderTheme,
            R.style.AssocBuilderTheme, R.style.BeaconTheme
    };

    int themeIndex;

    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        themeIndex = SharedPrefUtil.getThemeIndex(this);
        switch (themeIndex) {
            case 0:
                theme.applyStyle(themes[0], true);
                break;
            case 1:
                theme.applyStyle(themes[1], true);
                break;
            case 2:
                theme.applyStyle(themes[2], true);
                break;
            case 3:
                theme.applyStyle(themes[3], true);
                break;
            case 4:
                theme.applyStyle(themes[4], true);
                break;
            case 5:
                theme.applyStyle(themes[5], true);
                break;
            case 6:
                theme.applyStyle(themes[6], true);
                break;
            case 7:
                theme.applyStyle(themes[7], true);
                break;
            case 8:
                theme.applyStyle(themes[8], true);
                break;

        }


        // you could also use a switch if you have many themes that could apply
        return theme;
    }
    private void listen() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new PaymentReceiver(),
                new IntentFilter(FCMMessagingService.BROADCAST_PAYMENT_DONE));

    }

    private class PaymentReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.w(TAG, "onReceive: PaymentReceiver ;;;;;;;;;;;;;;;;;;;;;;" );
            Payment p = (Payment) intent.getSerializableExtra("data");
            Log.i(TAG, "onReceive: payment: ".concat(GSON.toJson(p)));
            String token = SharedPrefUtil.getCloudMsgToken(getApplicationContext());
            StringBuilder sb = new StringBuilder();

            if (p.getToFCMToken().equalsIgnoreCase(token)) {
                p.setReceiving(true);
                sb.append("Payment has been received: ");
            }
            if (p.getFromFCMToken().equalsIgnoreCase(token)) {
                p.setReceiving(false);
                sb.append("Payment has been processed and receiver notified: ");
            }
            String amount;
            if (p != null) {
                amount = p.getAmount();
            } else {
                amount = "Unknown";
            }
            showSnackIndefinite(sb.toString().concat(amount), "ok", "green");
        }
    }

}
