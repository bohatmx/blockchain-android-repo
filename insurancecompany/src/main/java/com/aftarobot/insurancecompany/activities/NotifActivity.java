package com.aftarobot.insurancecompany.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.aftarobot.insurancecompany.R;
import com.aftarobot.mlibrary.data.Burial;
import com.aftarobot.mlibrary.data.DeathCertificate;
import com.aftarobot.mlibrary.data.UserDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class NotifActivity extends AppCompatActivity {

    private String title;
    private String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG, "onCreate: #####################################" );
        setContentView(R.layout.activity_notif);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        title = getIntent().getStringExtra("title");
        json = getIntent().getStringExtra("json");

        if (title.contains("Welcome")) {
            Log.i(TAG, "onCreate: welcome message. cloud messaging works");
            showWelcome(GSON.fromJson(json,UserDTO.class));

        }
        if (title.contains("Death")) {
            Log.w(TAG, "onCreate: death certificate message received" );
            showCertificate(GSON.fromJson(json,DeathCertificate.class));

        }
        if (title.contains("Burial")) {
            Log.w(TAG, "onCreate: burial message received" );
            showBurial(GSON.fromJson(json,Burial.class));
        }
    }

    private void showWelcome(UserDTO user) {
        Log.i(TAG, "showWelcome: ".concat(GSON.toJson(user)));
    }
    private void showCertificate(DeathCertificate dc) {
        Log.w(TAG, "showCertificate: ".concat(GSON.toJson(dc)) );
    }
    private void showBurial(Burial burial) {
        Log.d(TAG, "showBurial: ################ ".concat(GSON.toJson(burial)));

    }

    public static final String TAG = NotifActivity.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
}
