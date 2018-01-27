package com.aftarobot.mlibrary.util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.aftarobot.mlibrary.data.Burial;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.DeathCertificate;
import com.aftarobot.mlibrary.data.Policy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class MyBroadcastReceiver extends BroadcastReceiver {
    Activity activity;

    public MyBroadcastReceiver(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Object d = intent.getSerializableExtra("data");
        if (d instanceof DeathCertificate) {
            showCert((DeathCertificate) d);
        }
        if (d instanceof Burial) {
            showBurial((Burial) d);
        }
        if (d instanceof Claim) {
            showClaim((Claim) d);
        }
        if (d instanceof Policy) {
            showPolicy((Policy) d);
        }
    }

    private void showClaim(Claim dc) {
        AlertDialog.Builder x = new AlertDialog.Builder(activity);
        x.setTitle("Claim Registered")
                .setMessage("Fresh from the blockchain ...\n\n".concat(GSON.toJson(dc)))
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
    private void showPolicy(Policy dc) {
        AlertDialog.Builder x = new AlertDialog.Builder(activity);
        x.setTitle("Policy Registered")
                .setMessage("Fresh from the blockchain ...\n\n".concat(GSON.toJson(dc)))
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
    private void showBurial(Burial dc) {
        AlertDialog.Builder x = new AlertDialog.Builder(activity);
        x.setTitle("Burial Registered")
                .setMessage("Fresh from the blockchain ...\n\n".concat(GSON.toJson(dc)))
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
    private void showCert(DeathCertificate dc) {
        AlertDialog.Builder x = new AlertDialog.Builder(activity);
        x.setTitle("Death Certificate Registered")
                .setMessage("Fresh from the blockchain ...\n\n".concat(GSON.toJson(dc)))
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
}
