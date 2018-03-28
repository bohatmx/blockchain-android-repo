package com.aftarobot.wallet.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.aftarobot.mlibrary.PhotoTakerActivity;
import com.aftarobot.mlibrary.api.FBApi;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.Payment;
import com.aftarobot.mlibrary.data.Wallet;
import com.aftarobot.mlibrary.util.SharedPrefUtil;
import com.aftarobot.wallet.R;
import com.aftarobot.wallet.data.Account;
import com.aftarobot.wallet.data.assets.Assets;
import com.aftarobot.wallet.data.effects.EffectsResponse;
import com.aftarobot.wallet.data.payments.PaymentsResponse;
import com.aftarobot.wallet.data.payments.Record;
import com.aftarobot.wallet.sdk.StellarAPI;
import com.aftarobot.wallet.services.FCMMessagingService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private StellarAPI stellarAPI = new StellarAPI();
    Toolbar toolbar;
    DrawerLayout drawer;
    Wallet wallet;
    View card1, card2;
    TextView txtDate, txtLastDate, txtBalance, txtAmount, txtType, txtAcct;
    public static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm", Locale.getDefault());

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
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("OneConnect Payments");

        wallet = SharedPrefUtil.getWallet(this);

        setup();
        listen();
        if (wallet != null && wallet.getAccountID() != null) {
            Log.i(TAG, "onCreate: wallet: ".concat(GSON.toJson(wallet)));
            getSupportActionBar().setSubtitle(wallet.getName());
            getAccount(true);

        }
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToManualAccount();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        wallet = SharedPrefUtil.getWallet(this);
        if (wallet != null && wallet.getAccountID() != null) {
            Objects.requireNonNull(getSupportActionBar()).setSubtitle(wallet.getName());
            getAccount(true);

        }
    }

    private void changeToManualAccount() {
        final String publicKey = "GAIHVSEZQ4KVPJTM2CPBZSIBLBPKPZG5U6JFGQIE76TYSNXQME5GKBXO",
                secretKey = "SB57MDG3AGK3S76KLXYHEVFHQBGFX75UBV7N2KO4HYAI3TMIES5BOBS5";

        SharedPrefUtil.saveSecret(secretKey, this);
        wallet = SharedPrefUtil.getWallet(this);
        if (wallet != null) {
            wallet.setAccountID(publicKey);
            Log.e(TAG, "changeToManualAccount: wallet, check walletID: ".concat(GSON.toJson(wallet)));
            FBApi api = new FBApi();
            api.updateWallet(wallet, new FBApi.FBListener() {
                @Override
                public void onResponse(Data data) {
                    wallet.setSeed(secretKey);
                    SharedPrefUtil.saveWallet(wallet, getApplicationContext());
                    restartMe();
                }

                @Override
                public void onError(String message) {
                    showError(message);
                }
            });

        }

    }

    private void setup() {
        txtAcct = findViewById(R.id.txtAccount);
        txtAmount = findViewById(R.id.txtLastAmount);
        txtBalance = findViewById(R.id.txtBalance);
        txtDate = findViewById(R.id.txtDate);
        txtLastDate = findViewById(R.id.txtLastDate);
        txtType = findViewById(R.id.txtType);
        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card1.setAlpha(0.2f);
        card2.setAlpha(0.2f);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAccount(true);
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void getAccount(boolean show) {
        if (show) {
            showSnack("Loading account details ...", "ok", "cyan");
        }
        stellarAPI.getAccount(wallet.getAccountID(),
                new StellarAPI.AccountListener() {
                    @Override
                    public void onResponse(Account account) {
                        showSnack("Balance: ".concat(account.getBalances().get(0).getBalance()), "ok", "green");
                        txtBalance.setText(account.getBalances().get(0).getBalance());
                        txtDate.setText(sdf.format(new Date()));
                        card1.setAlpha(1.0f);
                        getAccountPayments();
                    }

                    @Override
                    public void onError(String message) {
                        showError(message);
                    }
                });
    }

    private void getStellarAssets() {

        HashMap<String, String> map = new HashMap<>();
        map.put("limit", "15");
        stellarAPI.getAssets(map, new StellarAPI.AssetsListener() {
            @Override
            public void onResponse(Assets assets) {
                showSnack("Got ourselves some assets: ", "oh", "green");
                Log.i(TAG, "onResponse: assets returned: ");
                getStellarEffects();
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    private void getStellarEffects() {

        HashMap<String, String> map = new HashMap<>();
        map.put("limit", "5");
        stellarAPI.getEffects(map, new StellarAPI.EffectsListener() {
            @Override
            public void onResponse(EffectsResponse assets) {
                showSnack("Got ourselves some effects: "
                        + assets.getEmbedded().getRecords().size(), "oh", "green");
                Log.i(TAG, "onResponse: assets returned: " + assets.getEmbedded().getRecords().size());
                getAccountEffects();
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }


    private void getAccountEffects() {

        HashMap<String, String> map = new HashMap<>();
        map.put("limit", "5");
        stellarAPI.getAccountEffects(map, wallet.getAccountID(), new StellarAPI.EffectsListener() {
            @Override
            public void onResponse(EffectsResponse assets) {
                showSnack("Got ourselves ACCOUNT effects: "
                        + assets.getEmbedded().getRecords().size(), "oh", "green");
                Log.i(TAG, "onResponse: assets returned: " + assets.getEmbedded().getRecords().size());
                getAccountPayments();
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    private void getAccountPayments() {

        HashMap<String, String> map = new HashMap<>();
        map.put("limit", "50");
        map.put("order", "desc");

        stellarAPI.getAccountPayments(wallet.getAccountID(), map, new StellarAPI.PaymentsListener() {
            @Override
            public void onResponse(PaymentsResponse response) {
                showSnack("Payment transactions found ", "oh", "green");
                Log.i(TAG, "onResponse: account payments returned: " + GSON.toJson(response));
                if (response.getEmbedded() != null) {
                    setRecordFields(response);

                }
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    private void setRecordFields(PaymentsResponse response) {
        List<Record> list = response.getEmbedded().getRecords();
        Record record = null;
        for (Record m : list) {
            if (m.getFrom() != null) {
                record = m;
                break;
            }
        }
        if (record != null) {
            txtLastDate.setText(sdf.format(record.getCreatedAt()));
            txtAmount.setText(record.getAmount());
            txtType.setText(record.getType().toUpperCase());
            txtAcct.setText(record.getTo());
            card2.setAlpha(1.0f);
            if (record.getFrom().equalsIgnoreCase(wallet.getAccountID())) {
                txtAmount.setTextColor(Color.parseColor("red"));
            } else {
                txtAmount.setTextColor(Color.parseColor("black"));
            }
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            int index = SharedPrefUtil.getThemeIndex(this);
            index++;
            if (index > 8) {
                index = 0;
            }
            SharedPrefUtil.saveThemeIndex(index, this);
            restartMe();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void restartMe() {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(i);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent m = null;
        switch (id) {
            case R.id.nav_payments:
                m = new Intent(this, PaymentsActivity.class);
                break;
            case R.id.nav_history:
                break;
            case R.id.nav_trades:
                break;
            case R.id.nav_manage:
                break;
            case R.id.nav_profile:
                m = new Intent(this, PhotoTakerActivity.class);
                break;
            case R.id.nav_wallets:
                m = new Intent(this, WalletListActivity.class);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        if (m != null) {
            startActivity(m);
        }
        return true;
    }

    private void listen() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new PaymentReceiver(),
                new IntentFilter(FCMMessagingService.BROADCAST_PAYMENT_DONE));
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new WalletReceiver(),
                new IntentFilter(FCMMessagingService.BROADCAST_WALLET));
    }

    private class PaymentReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.w(TAG, "onReceive: PaymentReceiver ;;;;;;;;;;;;;;;;;;;;;;");
            Payment p = (Payment) intent.getSerializableExtra("data");
            Log.i(TAG, "onReceive: payment: ".concat(GSON.toJson(p)));
            String token = SharedPrefUtil.getCloudMsgToken(getApplicationContext());
            StringBuilder sb = new StringBuilder();

            if (p.getToFCMToken().equalsIgnoreCase(token)) {
                p.setReceiving(true);
                sb.append("Payment has been received, Yay!: ");
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
            getAccount(false);
        }
    }

    private class WalletReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: WalletReceiver .......................");
            wallet = (Wallet) intent.getSerializableExtra("data");
            Log.e(TAG, "WalletReceiver onReceive: wallet: ".concat(GSON.toJson(wallet)));
            showSnack("Wallet created OK on the blockchain", "ok", "green");
            if (wallet != null) {
                update();
            }
            getAccount(false);

        }
    }

    private void update() {
        getSupportActionBar().setSubtitle(wallet.getName());
    }

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

}
