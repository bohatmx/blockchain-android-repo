package com.aftarobot.blockchaintest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.aftarobot.mlibrary.api.FBApi;
import com.aftarobot.mlibrary.api.FBListApi;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private static final int RC_SIGN_IN = 123;
    private FBApi fbApi;
    private FBListApi fbListApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
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


    private void setFields() {

    }


    private void checkGooglePlay() {
        int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        switch (status) {
            case ConnectionResult.SUCCESS:
                Log.i(TAG, "checkGooglePlay: ConnectionResult.SUCCESS:");
                startSignUp();
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

    private void startSignUp() {

        mAuth.signInAnonymously()
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.i(TAG, "signInAnonymously onSuccess: user is signed in");
                        Snackbar.make(toolbar, "Sign in successful. A-OK", Snackbar.LENGTH_SHORT).show();
                        startMain();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                        showSnackError("Error signing in, please check");
                    }
                });


    }

    private void startMain() {
        Intent m = new Intent(this, CrudActivity.class);
        startActivity(m);
        finish();
    }

    private void showError(String message) {

    }

    public static final SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");

    private Snackbar snackbar;

    private void showSnackError(String message) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public static final String TAG = SignInActivity.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
}
