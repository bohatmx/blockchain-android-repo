package com.aftarobot.beneficiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.aftarobot.mlibrary.api.FBApi;
import com.aftarobot.mlibrary.api.FBListApi;
import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.UserDTO;
import com.aftarobot.mlibrary.util.SharedPrefUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private static final int RC_SIGN_IN = 123;
    private FBApi fbApi;
    private FBListApi fbListApi;
    private UserDTO user;
    private List<Beneficiary> beneficiaries;
    private BeneficiaryAdapter adapter;
    private Spinner spinner;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            Log.i(TAG, "onCreate: user already authenticated by Firebase. cool!");
            startMain();
            return;
        }
        fbApi = new FBApi();
        fbListApi = new FBListApi();

        setFields();

        checkGooglePlay();

    }

    private Beneficiary beneficiary;
    private void setList() {
        Collections.sort(beneficiaries);
        List<String> list = new ArrayList<>();
        list.add("Select Beneficiary");
        for (Beneficiary b: beneficiaries) {
            list.add(b.getFullName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,list);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    beneficiary = null;
                    btn.setEnabled(false);
                    btn.setAlpha(0.3f);
                } else {
                    beneficiary = beneficiaries.get(position - 1);
                    btn.setEnabled(true);
                    btn.setAlpha(1.0f);
                    confirm(beneficiary);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void confirm(final Beneficiary beneficiary) {
        AlertDialog.Builder x = new AlertDialog.Builder(this);
        x.setTitle("Confirm Authentication")
                .setMessage("Do you want to authenticate as ".concat(beneficiary.getFullName()).concat("?"))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAuth(beneficiary);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
    private void startAuth(final Beneficiary beneficiary) {
        mAuth.signInWithEmailAndPassword(beneficiary.getEmail(),beneficiary.getPassword())
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = authResult.getUser();
                        Log.i(TAG, "onSuccess: authenticated: ".concat(GSON.toJson(user)));
                        beneficiary.setFcmToken(SharedPrefUtil.getCloudMsgToken(getApplicationContext()));
                        SharedPrefUtil.saveBeneficiary(beneficiary,getApplicationContext());
                        Toast.makeText(getApplicationContext(),"Authentication successful", Toast.LENGTH_LONG).show();
                        fbApi.updateBeneficiaryToken(beneficiary, new FBApi.FBListener() {
                            @Override
                            public void onResponse(Data data) {
                                Log.i(TAG, "updateBeneficiaryFCMToken onResponse: ######### Yebo! fcmToken done... moving on ...".concat(GSON.toJson(beneficiary)));
                            }

                            @Override
                            public void onError(String message) {

                            }
                        });
                        startMain();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showError(e.getMessage());
                    }
                });
    }
    private void setFields() {
        spinner = findViewById(R.id.spinner);
        btn = findViewById(R.id.btnSignup);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBeneficiaries();
            }
        });

    }


    private void checkGooglePlay() {
        int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        switch (status) {
            case ConnectionResult.SUCCESS:
                Log.i(TAG, "checkGooglePlay: ConnectionResult.SUCCESS:");
                authAnonymously();
                break;
            case ConnectionResult.SERVICE_MISSING:
                Log.e(TAG, "checkGooglePlay: ConnectionResult.SERVICE_MISSING ");
                GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this);
                break;
            case ConnectionResult.SERVICE_UPDATING:
                Log.w(TAG, "checkGooglePlay: ConnectionResult.SERVICE_UPDATING");
                break;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                Log.w(TAG, "checkGooglePlay: ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED");
                GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this);
                break;
            case ConnectionResult.SERVICE_DISABLED:
                Log.e(TAG, "checkGooglePlay:ConnectionResult.SERVICE_DISABLED");
                break;
            case ConnectionResult.SERVICE_INVALID:
                Log.e(TAG, "checkGooglePlay: ConnectionResult.SERVICE_INVALID ");
                GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this);
                break;

        }

    }

    private void authAnonymously() {
        showSnackbar("Authenticating anonymously at Firebase","OK","cyan");
        Log.d(TAG, "authAnonymously: ******************** auth anon to Firebase");
        mAuth.signInAnonymously()
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        getBeneficiaries();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       showError(e.getMessage());
                    }
                });
    }

    private void getBeneficiaries() {
        showSnackbar("Loading beneficiaries from Firebase ...","OK","yellow");
        fbListApi.getBeneficiaries(new FBListApi.BeneficiaryListener() {
            @Override
            public void onResponse(List<Beneficiary> list) {
             beneficiaries = new ArrayList<>();
             for (Beneficiary b: list) {
                 if (b.getFcmToken() == null && b.getPassword() != null) {
                     beneficiaries.add(b);
                 }
             }
             snackbar.dismiss();
             setList();
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });

    }

    private void startMain() {
        Intent m = new Intent(this, NavActivity.class);
        startActivity(m);
        finish();
    }

    public static final SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");

    private Snackbar snackbar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public static final String TAG = LoginActivity.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

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

}
