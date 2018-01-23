package com.aftarobot.parlourapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.aftarobot.mlibrary.data.Burial;
import com.aftarobot.mlibrary.data.DeathCertificate;
import com.aftarobot.mlibrary.data.UserDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class NotifActivity extends Activity {

    private String messageType;
    private String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);

        messageType = getIntent().getStringExtra("messageType");
        json = getIntent().getStringExtra("json");

        if (messageType.contains("USER")) {
            Log.i(TAG, "onCreate: welcome message. cloud messaging works");
            showWelcome(GSON.fromJson(json,UserDTO.class));

        }
        if (messageType.contains("CERT")) {
            Log.w(TAG, "onCreate: death certificate message received" );
            showCertificate(GSON.fromJson(json,DeathCertificate.class));

        }
        if (messageType.contains("BURIAL")) {
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
