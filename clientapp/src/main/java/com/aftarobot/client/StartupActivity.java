package com.aftarobot.client;

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

import com.aftarobot.mlibrary.api.FBApi;
import com.aftarobot.mlibrary.api.FBListApi;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.util.SharedPrefUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StartupActivity extends AppCompatActivity {

    FBListApi fbListApi;
    FirebaseAuth auth;
    List<Client> clients;
    Spinner spinner;
    Button btnSignUp;
    FloatingActionButton fab;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Client Policies");
        getSupportActionBar().setSubtitle("OneConnect Blockchain");
        setup();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startMain();
            return;
        }
        fbListApi = new FBListApi();
        getClients();

    }

    private FBApi fbApi;

    private void login() {
        Log.w(TAG, "login: client: ".concat(GSON.toJson(client)) );
        if (client.getPassword() == null) {
            showError("Password is null. Not good. Failed.");
            return;
        }
        auth.signInWithEmailAndPassword(client.getEmail(), client.getPassword())
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showError("Firebase login failed");
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.i(TAG, "onSuccess: ");
                        //todo update the clients fcmtoken
                        fbApi = new FBApi();
                        fbApi.updateClientToken(client, new FBApi.FBListener() {
                            @Override
                            public void onResponse(Data data) {
                                Log.e(TAG, "updateClientToken onResponse: client fcmToken updated");
                                SharedPrefUtil.saveClient(client, getApplicationContext());
                                startMain();
                            }

                            @Override
                            public void onError(String message) {
                                showError(message);
                            }
                        });

                    }
                });
    }

    private void getClients() {
        showSnackbar("Loading clients ....", "ok", "cyan");
        auth.signInAnonymously()
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        fbListApi.getClients(new FBListApi.ClientListener() {
                            @Override
                            public void onResponse(List<Client> list) {
                                clients = list;
                                setSpinner();
                                snackbar.dismiss();
                                fab.setAlpha(1.0f);
                                fab.setEnabled(true);
                            }

                            @Override
                            public void onError(String message) {
                                showError(message);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showError("Anonymous Firebase sign in failed");
                    }
                });
    }

    private Client client;

    private void setSpinner() {
        List<String> list = new ArrayList<>();
        list.add("Select Client");
        Collections.sort(clients);
        for (Client c : clients) {
            list.add(c.getFullName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, list);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    btnSignUp.setAlpha(0.3f);
                    btnSignUp.setEnabled(false);
                    client = null;
                } else {
                    btnSignUp.setAlpha(1.0f);
                    btnSignUp.setEnabled(true);
                    client = clients.get(position - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void startMain() {
        Intent m = new Intent(this, ClientMainActivity.class);
        startActivity(m);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_startup, menu);
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

    private void setup() {
        btnSignUp = findViewById(R.id.btnSignup);
        spinner = findViewById(R.id.spinner);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setAlpha(0.3f);
                fab.setEnabled(false);
                getClients();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        btnSignUp.setAlpha(0.3f);
        btnSignUp.setEnabled(false);
    }

    Snackbar snackbar;
    public static final String TAG = StartupActivity.class.getSimpleName();

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
public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
}
