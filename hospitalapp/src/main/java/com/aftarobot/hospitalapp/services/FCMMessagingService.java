package com.aftarobot.hospitalapp.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.aftarobot.hospitalapp.NotifActivity;
import com.aftarobot.hospitalapp.R;
import com.aftarobot.mlibrary.data.DeathCertificate;
import com.aftarobot.mlibrary.data.UserDTO;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class FCMMessagingService extends FirebaseMessagingService {

    public static final String TAG = FCMMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //Log.i(TAG, "onMessageReceived: ".concat(GSON.toJson(remoteMessage)));

        if (remoteMessage.getData() != null) {
            Map<String, String> map = remoteMessage.getData();
            for (String s : map.keySet()) {
                Log.d(TAG, "onMessageReceived: name: " + s + " value: " + map.get(s));
            }
            if (map.get("messageType").equalsIgnoreCase("USER")) {
                String json = map.get("json");
                UserDTO user = GSON.fromJson(json, UserDTO.class);
                sendNotification("Welcome to Blockchain", json);

            }
            if (map.get("messageType").equalsIgnoreCase("CERT")) {
                String json = map.get("json");
                DeathCertificate cert = GSON.fromJson(json, DeathCertificate.class);
                sendNotification("Death Certificate Issued", json);
            }

        }
    }


    private void sendNotification(String title, String json) {


        Intent resultIntent = new Intent(getApplicationContext(), NotifActivity.class);
        resultIntent.putExtra("title", title);
        resultIntent.putExtra("json", json);

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
