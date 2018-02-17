package com.aftarobot.bank;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.aftarobot.mlibrary.api.ChainListAPI;
import com.aftarobot.mlibrary.data.Bank;
import com.aftarobot.mlibrary.util.SharedPrefUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SignInActivity extends AppCompatActivity {

    Spinner spinner;
    Button button;
    Toolbar toolbar;
    ChainListAPI chainListAPI;
    List<Bank> banks;
    Bank bank;
    FloatingActionButton fab;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startMain();
        }
        chainListAPI = new ChainListAPI(this);
        getBanks();

        fab = findViewById(R.id.fab);
        button = findViewById(R.id.btnSignup);
        spinner = findViewById(R.id.spinner);
        button.setEnabled(false);
        button.setAlpha(0.3f);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBanks();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    private void startMain() {
        Intent m = new Intent(this,BankMainActivity.class);
        startActivity(m);
        finish();
    }
    private void signup() {
        auth.signInAnonymously()
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseMessaging.getInstance().subscribeToTopic("fundTransferRequests"
                        .concat(bank.getBankId()));
                        Log.e(TAG, "##### onSuccess: subscribed to topic: fundTransferRequests" );
                        showSnackbar("Sign up successful. Enjoy", "ok", "green");
                        startMain();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showError("Unable to sign up. Try again");
                        button.setEnabled(true);
                        button.setAlpha(1.0f);
                    }
                });
    }
    private void getBanks() {
        chainListAPI.getBanks(new ChainListAPI.BankListener() {
            @Override
            public void onResponse(List<Bank> list) {
                Log.w(TAG, "onResponse: banks found" + list.size() );
                banks = list;
                setSpinner();
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    private void setSpinner() {
        List<String> list = new ArrayList<>();
        list.add("Select Bank");
        Collections.sort(banks);
        for (Bank b : banks) {
            list.add(b.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    bank = null;
                    button.setEnabled(false);
                    button.setAlpha(0.3f);
                } else {
                    bank = banks.get(position - 1);
                    button.setEnabled(true);
                    button.setAlpha(1.0f);
                    SharedPrefUtil.saveBank(bank, getApplicationContext());
                    getSupportActionBar().setTitle(bank.getName());
                    getSupportActionBar().setSubtitle("Funds Transfer Request Process");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    public static final String TAG = SignInActivity.class.getSimpleName();
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

}
