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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aftarobot.mlibrary.api.FBApi;
import com.aftarobot.mlibrary.api.FBListApi;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.Payment;
import com.aftarobot.mlibrary.data.Wallet;
import com.aftarobot.mlibrary.util.SharedPrefUtil;
import com.aftarobot.wallet.R;
import com.aftarobot.wallet.data.payments.PaymentsResponse;
import com.aftarobot.wallet.data.payments.Record;
import com.aftarobot.wallet.sdk.StellarAPI;
import com.aftarobot.wallet.services.FCMMessagingService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PaymentsActivity extends AppCompatActivity {

    Toolbar toolbar;
    StellarAPI stellarAPI;
    FBListApi listApi;
    Wallet wallet, selectedWallet;
    List<Wallet> wallets;
    AutoCompleteTextView auto;
    TextInputEditText editMoney;
    Button btnSendMoney;
    TextView txtCount;
    RecyclerView recyclerView;
    List<Payment> payments;
    int[] themes = {
            R.style.DriverTheme,
            R.style.AftaRobotTheme, R.style.CommuterTheme,
            R.style.MarshalTheme, R.style.AdminTheme,
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("OneConnect Payments");
        getSupportActionBar().setSubtitle("Wallet Payments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        wallet = SharedPrefUtil.getWallet(this);
        listen();
        if (wallet != null) {
            getSupportActionBar().setSubtitle(wallet.getName());

        }
        stellarAPI = new StellarAPI();
        listApi = new FBListApi();

        setup();

        getWallets();
    }

    private void setup() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAccountPayments();
            }
        });
        auto = findViewById(R.id.auto);
        layout = findViewById(R.id.layout2);
        editMoney = findViewById(R.id.editMoney);
        btnSendMoney = findViewById(R.id.btnSendMoney);
        txtCount = findViewById(R.id.txtCount);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnSendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
        txtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layout.getVisibility() == View.VISIBLE) {
                    layout.setVisibility(View.GONE);
                    btnSendMoney.setVisibility(View.GONE);
                } else {
                    layout.setVisibility(View.VISIBLE);
                    btnSendMoney.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void confirm() {
        if (selectedWallet == null) {
            Toast.makeText(this,"Please select wallet", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(editMoney.getText())) {
            Toast.makeText(this, "Please enter the money", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder x = new AlertDialog.Builder(this);
        x.setTitle("Payment Confirmation")
                .setIcon(R.drawable.ic_attach_money)
                .setMessage("Do you want to send money to ".concat(selectedWallet.getName()).concat(" ?"))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendMoney();
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();

    }

    private void sendMoney() {
        showSnack("Sending the big bucks ...", "ok", "cyan");
        FBApi api = new FBApi();
        Payment p = new Payment();
        p.setFromFCMToken(SharedPrefUtil.getCloudMsgToken(this));
        p.setToFCMToken(selectedWallet.getFcmToken());
        p.setDestinationAccount(selectedWallet.getAccountID());
        p.setSourceAccount(wallet.getAccountID());
        p.setAmount(editMoney.getText().toString());
        p.setSeed(SharedPrefUtil.getSecret(this));
        p.setMemo("Biggie is here!");
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

    private List<Record> records;
    private void getAccountPayments() {

        HashMap<String, String> map = new HashMap<>();
        map.put("limit", "200");
        map.put("order", "desc");

        stellarAPI.getAccountPayments(wallet.getAccountID(), map, new StellarAPI.PaymentsListener() {
            @Override
            public void onResponse(PaymentsResponse response) {
                showSnack("Payment transactions found ", "oh", "green");
                Log.i(TAG, "onResponse: account payments returned: " + GSON.toJson(response));
                if (response.getEmbedded() != null) {
                    List<Record> list  = response.getEmbedded().getRecords();
                    records = new ArrayList<>();
                    for (Record r: list) {
                        if (r.getFrom() != null) {
                            records.add(r);
                        }
                    }
                    txtCount.setText(String.valueOf(records.size()));
                    setList();

                }
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    private void setList() {
        PaymentAdapter adapter = new PaymentAdapter(records, wallet.getAccountID(), new PaymentAdapter.PaymentListener() {
            @Override
            public void onClientTapped(Record payment) {

            }

            @Override
            public void onEmailTapped(Record payment) {

            }
        });
        recyclerView.setAdapter(adapter);
    }
    private void getWallets() {
        listApi.getWallets(new FBListApi.WalletListener() {
            @Override
            public void onResponse(List<Wallet> list) {
                wallets = list;
                setAuto();
                getAccountPayments();
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void setAuto() {
        List<String> list = new ArrayList<>();
        list.add("Select Wallet");
        for (Wallet w : wallets) {
            list.add(w.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        auto.setThreshold(1);
        auto.setAdapter(adapter);
        auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.w(TAG, "onItemClick: position" + position );
                if (position == 0) {
                    selectedWallet = null;
                } else {
                    setSelectedWallet(auto.getText().toString());
                    btnSendMoney.setEnabled(true);
                    btnSendMoney.setAlpha(1.0f);
                    hideKeyboard();
                }
            }
        });
        auto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "onItemSelected: position: " + position );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSelectedWallet(String name) {
        Log.d(TAG, "setSelectedWallet: fromAcct: ".concat(name));
        selectedWallet = null;
        for (Wallet w : wallets) {
            if (w.getName().equalsIgnoreCase(name)) {
                selectedWallet = w;
                Log.i(TAG, "setSelectedWallet: selected: ".concat(GSON.toJson(w)));
                break;
            }
        }
        Log.e(TAG, "setSelectedWallet: selectedWallet not found in list" );
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
            showSnack(sb.toString().concat(amount), "ok", "green");
        }
    }

    private Snackbar snackbar;
    public static final String TAG = MainActivity.class.getSimpleName();

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
    void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(auto.getWindowToken(), 0);
    }

    View layout;
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

}
