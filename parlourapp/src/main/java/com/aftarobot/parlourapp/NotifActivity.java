package com.aftarobot.parlourapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.aftarobot.mlibrary.data.Burial;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.DeathCertificate;
import com.aftarobot.mlibrary.data.UserDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class NotifActivity extends Activity {

    private String messageType;
    private String title;
    private String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);

        setup();
    }
    private void setup() {
        title = getIntent().getStringExtra("title");
        json = getIntent().getStringExtra("json");
        String messageType = getIntent().getStringExtra("messageType");
        setTitle("Messages");

        if (messageType.contains("USER")) {
            Log.i(TAG, "onCreate: welcome message. cloud messaging works");
            showWelcome(GSON.fromJson(json, UserDTO.class));

        }
        if (messageType.contains("CERT")) {
            Log.w(TAG, "onCreate: death certificate message received");
            showCertificate(GSON.fromJson(json, DeathCertificate.class));

        }
        if (messageType.contains("BURIAL")) {
            Log.w(TAG, "onCreate: burial message received");
            showBurial(GSON.fromJson(json, Burial.class));
        }
        if (messageType.contains("CLAIM")) {
            Log.w(TAG, "onCreate: burial message received");
            showClaim(GSON.fromJson(json, Claim.class));
        }
    }

    private void showWelcome(UserDTO user) {
        Log.i(TAG, "showWelcome: ".concat(GSON.toJson(user)));
        AlertDialog.Builder x = new AlertDialog.Builder(this);
        x.setTitle("Welcome to the Blockchain")
                .setMessage("Welcome to the OneConnect Business Network, a blockchain based solution for a vexing problem!")
                .setPositiveButton("Yes, thank you!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent m = new Intent(getApplicationContext(), NavActivity.class);
                        startActivity(m);
                    }
                })
                .show();
    }

    private void showCertificate(final DeathCertificate dc) {
        Log.w(TAG, "showCertificate: ".concat(GSON.toJson(dc)));
        AlertDialog.Builder x = new AlertDialog.Builder(this);
        x.setTitle("Death Certificate Registered")
                .setMessage("Fresh from the blockchain ...\n\n".concat(GSON.toJson(dc)))
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent m = new Intent(getApplicationContext(), NavActivity.class);
                        m.putExtra("cert", dc);
                        startActivity(m);
                        finish();
                    }
                })
                .show();

    }

    private void showBurial(final Burial burial) {
        Log.d(TAG, "showBurial: ################ ".concat(GSON.toJson(burial)));
        AlertDialog.Builder x = new AlertDialog.Builder(this);
        x.setTitle("Death Certificate Registered")
                .setMessage("Fresh from the blockchain ...\n\n".concat(GSON.toJson(burial)))
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent m = new Intent(getApplicationContext(), NavActivity.class);
                        m.putExtra("burial", burial);
                        startActivity(m);
                        finish();

                    }
                })
                .show();

    }

    private void showClaim(Claim claim) {
        Log.d(TAG, "showClaim: ################ ".concat(GSON.toJson(claim)));
        Intent m = new Intent(getApplicationContext(), NavActivity.class);
        m.putExtra("claim", claim);
        startActivity(m);

        finish();
    }

    public static final String TAG = NotifActivity.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
}
