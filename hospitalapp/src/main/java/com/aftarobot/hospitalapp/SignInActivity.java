package com.aftarobot.hospitalapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.aftarobot.mlibrary.util.SharedPrefUtil;
import com.aftarobot.mlibrary.api.ChainListAPI;
import com.aftarobot.mlibrary.api.FBApi;
import com.aftarobot.mlibrary.api.FBListApi;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.Hospital;
import com.aftarobot.mlibrary.data.UserDTO;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
        chainListAPI = new ChainListAPI(this);

        setFields();
        getHospitals();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        checkGooglePlay();

    }

    private Spinner spinner;
    private Button btn;
    private Hospital hospital;

    private void setFields() {
        spinner = findViewById(R.id.spinner);
        btn = findViewById(R.id.btnStart);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hospital == null) {
                    showError("Please select hospital");
                } else {
                    startSignUp();
                }
            }
        });
        btn.setEnabled(false);
    }
    private void setSpinner() {
        List<String> list = new ArrayList<>();
        for (Hospital h: hospitals) {
            list.add(h.getName());
        }
        list.add(0, "Select Hospital");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    hospital = null;
                    btn.setEnabled(false);
                    return;
                }
                btn.setEnabled(true);
                hospital = hospitals.get(position - 1);
                SharedPrefUtil.saveHospital(hospital,getApplicationContext());
                getSupportActionBar().setTitle(hospital.getName());
                getSupportActionBar().setSubtitle("Connecting to Blockchain");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void checkGooglePlay() {
        int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        switch (status) {
            case ConnectionResult.SUCCESS:
                Log.i(TAG, "checkGooglePlay: ConnectionResult.SUCCESS:");
                break;
            case ConnectionResult.SERVICE_MISSING:
                Log.e(TAG, "checkGooglePlay: ConnectionResult.SERVICE_MISSING " );
                GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this);
                break;
            case ConnectionResult.SERVICE_UPDATING:
                Log.w(TAG, "checkGooglePlay: ConnectionResult.SERVICE_UPDATING" );
                break;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                Log.w(TAG, "checkGooglePlay: ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED" );
                GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this);
                break;
            case ConnectionResult.SERVICE_DISABLED:
                Log.e(TAG, "checkGooglePlay:ConnectionResult.SERVICE_DISABLED" );
                break;
            case ConnectionResult.SERVICE_INVALID:
                Log.e(TAG, "checkGooglePlay: ConnectionResult.SERVICE_INVALID " );
                GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this);
                break;

        }

    }
    private void startSignUp() {

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
//                new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build());

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            Log.w(TAG, "onActivityResult: IdpResponse: ".concat(GSON.toJson(response)) );
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.i(TAG, "onActivityResult: Firebase sign in OK: ".concat(GSON.toJson(user)));
                Snackbar.make(toolbar, "Sign in A-OK", Snackbar.LENGTH_SHORT).show();
                addUserToFirebase(user);
            } else {
                showSnackError("Error signing in, please check");
                Log.e(TAG, "onActivityResult: Sign in failed, check response for error code");
            }
        }
    }

    private void startMain() {
        Intent m = new Intent(this, HospitalNavActivity.class);
        startActivity(m);
        finish();
    }
    private List<Hospital> hospitals;
    private ChainListAPI chainListAPI;

    private void getHospitals() {
        chainListAPI.getHospitals(new ChainListAPI.HospitalListener() {
            @Override
            public void onResponse(List<Hospital> list) {
                hospitals = list;
                Collections.sort(hospitals);
                Log.w(TAG, "onResponse: hospitals found on blockchain: " + list.size());
                setSpinner();
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });

    }
    private void showError(String message) {

    }
    public static final SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
    private void addUserToFirebase(final FirebaseUser u) {
        final UserDTO user = new UserDTO();
        user.setHospitalId(hospital.getHospitalId());
        user.setDateRegistered(new Date().getTime());
        user.setEmail(u.getEmail());
        user.setUid(u.getUid());
        user.setDisplayName(u.getDisplayName());
        user.setStringDateRegistered(sdf.format(new Date()));
        user.setFcmToken(SharedPrefUtil.getCloudMsgToken(this));
        user.setUserType(UserDTO.HOSPITAL_USER);
        writeUser(user, u);

    }

    private void writeUser(final UserDTO user, final FirebaseUser u) {
        fbApi.addUser(user, new FBApi.FBListener() {
            @Override
            public void onResponse(Data data) {
                Log.i(TAG, "onResponse: user added OK: ".concat(u.getEmail()));
                FirebaseMessaging.getInstance().subscribeToTopic("certificates".concat(hospital.getHospitalId()));
                Log.e(TAG, "onResponse: user subscribed to topic: certificates".concat(hospital.getHospitalId()) );

                startMain();
            }

            @Override
            public void onError(String message) {
                showSnackError(message);
            }
        });
    }

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
}
