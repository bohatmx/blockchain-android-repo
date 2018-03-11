package com.aftarobot.hospitalapp.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.aftarobot.hospitalapp.HospitalNavActivity;
import com.aftarobot.hospitalapp.R;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.DeathCertificate;
import com.aftarobot.mlibrary.util.PlaySound;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;


public class FCMMessagingService extends FirebaseMessagingService {

    public static final String TAG = FCMMessagingService.class.getSimpleName();
    public static final String
            BROADCAST_CERT = "com.aftarobot.BROADCAST_CERT";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData() != null) {
            Map<String, String> map = remoteMessage.getData();
            for (String s : map.keySet()) {
                Log.d(TAG, "onMessageReceived: name: " + s + " value: " + map.get(s));
            }
            String type = map.get("messageType");
            if (type.equalsIgnoreCase("CERT")) {
                DeathCertificate dc = GSON.fromJson(map.get("json"), DeathCertificate.class);
                broadcast(BROADCAST_CERT, dc);
                sendNotification(type, "Death Certificate Issued", map.get("json"));
            }
            PlaySound.play(getApplicationContext());
        }
    }

    private void broadcast(String intent, Data data) {
        Intent m = new Intent(intent);
        if (data instanceof DeathCertificate) {
            m.putExtra("data", (DeathCertificate)data);
        }

        LocalBroadcastManager lm = LocalBroadcastManager.getInstance(getApplicationContext());
        lm.sendBroadcast(m);

    }
    private void sendNotification(String messageType, String title, String json) {

        Intent resultIntent = new Intent(getApplicationContext(), HospitalNavActivity.class);
        resultIntent.putExtra("title", title);
        resultIntent.putExtra("json", json);
        resultIntent.putExtra("messageType", messageType);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(title)
                .setSmallIcon(R.drawable.hospital)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .build();

        int mNotificationId = 0016;
        NotificationManager mNotifyMgr =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, notification);
        Log.e(TAG, "sendNotification: notification has been sent: ".concat(title));


    }

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
}
