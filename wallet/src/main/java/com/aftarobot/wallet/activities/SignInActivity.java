package com.aftarobot.wallet.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.aftarobot.mlibrary.api.FBApi;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.Wallet;
import com.aftarobot.mlibrary.util.SharedPrefUtil;
import com.aftarobot.wallet.BuildConfig;
import com.aftarobot.wallet.R;
import com.aftarobot.wallet.data.Account;
import com.aftarobot.wallet.sdk.StellarAPI;
import com.aftarobot.wallet.services.FCMInstanceIDService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.Random;

public class SignInActivity extends AppCompatActivity {
    public static final String TAG = SignInActivity.class.getSimpleName();

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("OneConnect Payments");
        getSupportActionBar().setSubtitle("Stellar Payments Network");
        listen();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (auth.getCurrentUser() != null) {
            Log.w(TAG, "onCreate: user already authenticated");
            startMain();
            return;
        }

        showSnack("Connecting to the Blockchain ...","ok","cyan");
        if (SharedPrefUtil.getCloudMsgToken(this) != null) {
            signIn();
        }
    }

    private void startMain() {
        Intent m = new Intent(this, MainActivity.class);
        startActivity(m);
        finish();
    }

    private void signIn() {
        auth.signInAnonymously()
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.i(TAG, "onSuccess: authenticated anon: ".concat(authResult.getUser().getUid()));
                        getSponsorAccount(authResult);

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showError(e.getMessage());
                    }
                });
    }

    private String getRandomEmail() {
        Random random = new Random(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        sb.append("user").append(random.nextInt(99));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(".w");
        sb.append(random.nextInt(9));
        sb.append("@date.com");
        return sb.toString();
    }
    private String getRandomName() {
        Random random = new Random(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        sb.append("Wallet ").append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));

        return sb.toString();
    }

    public static final String
            ACCOUNT_ID = "GDG52NAT4NG7ULJK6WALKEU3UBSKGUL533M5NQPIPHTTQGFHRFYUF2MT",
            SECRET = "SDZGFXWT5PHQZ6U6KHMBFFQ346I34B4LB7N524DHY4A22XHEEKHNKXCJ";

    private Account account;
    private StellarAPI stellarAPI = new StellarAPI();

    private void getSponsorAccount(final AuthResult authResult) {
        showSnack("Loading account details ...", "ok", "cyan");
        stellarAPI.getAccount(ACCOUNT_ID,
                new StellarAPI.AccountListener() {
                    @Override
                    public void onResponse(Account acc) {
                        account = acc;
                        addWallet();
                    }

                    @Override
                    public void onError(String message) {
                        showError(message);
                    }
                });
    }

    private void addWallet() {
        FirebaseUser user = auth.getCurrentUser();
        Log.i(TAG, "addWallet, user: ".concat(GSON.toJson(user)));
        Wallet w = new Wallet();
        w.setSequenceNumber(account.getSequence());
        w.setSourceSeed(SECRET);
        w.setSourceAccountID(ACCOUNT_ID);
        w.setDate(new Date().getTime());
        w.setUid(user.getUid());
        w.setFcmToken(SharedPrefUtil.getCloudMsgToken(this));
        if (user.getEmail() != null) {
            w.setEmail(user.getEmail());
        } else {
            w.setEmail(getRandomEmail());
        }
        w.setDebug(BuildConfig.DEBUG);
        w.setName(getRandomName());

        Log.e(TAG, "addWallet: ".concat(GSON.toJson(w)));
        showSnack("Creating your wallet ...", "ok", "yellow");
        FBApi api = new FBApi();
        api.addWallet(w, new FBApi.FBListener() {
            @Override
            public void onResponse(Data data) {
                startMain();
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });

    }

    private Snackbar snackbar;
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void listen() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new FCMReceiver(),
                new IntentFilter(FCMInstanceIDService.BROADCAST_FCMTOKEN));

    }

    private class FCMReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.w(TAG, "onReceive: FCMReceiver ;;;;;;;;;;;;;;;;;;;;;;" );
            signIn();

        }
    }

}
