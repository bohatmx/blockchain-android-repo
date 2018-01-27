package com.aftarobot.hospitalapp.services;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.aftarobot.hospitalapp.NotifActivity;
import com.aftarobot.hospitalapp.R;
import com.aftarobot.mlibrary.data.Burial;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.DeathCertificate;
import com.aftarobot.mlibrary.data.Policy;
import com.aftarobot.mlibrary.data.UserDTO;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
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
    public static final String
            BROADCAST_CERT = "com.aftarobot.BROADCAST_CERT",
            BROADCAST_USER = "com.aftarobot.BROADCAST_USER",
            BROADCAST_CLAIM = "com.aftarobot.BROADCAST_CLAIM",
            BROADCAST_POLICY = "com.aftarobot.BROADCAST_POLICY",
            BROADCAST_BURIAL = "com.aftarobot.BROADCAST_BURIAL";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData() != null) {
            Map<String, String> map = remoteMessage.getData();
            for (String s : map.keySet()) {
                Log.d(TAG, "onMessageReceived: name: " + s + " value: " + map.get(s));
            }
            if (isRunningInForeground(getApplicationContext())) {
                if (map.get("messageType").equalsIgnoreCase("USER")) {
                    UserDTO user = GSON.fromJson(map.get("json"), UserDTO.class);
                    broadcast(BROADCAST_USER, user);
                }
                if (map.get("messageType").equalsIgnoreCase("CLAIM")) {
                    Claim claim = GSON.fromJson(map.get("json"), Claim.class);
                    broadcast(BROADCAST_CLAIM, claim);
                }
                if (map.get("messageType").equalsIgnoreCase("CERT")) {
                    DeathCertificate dc = GSON.fromJson(map.get("json"), DeathCertificate.class);
                    broadcast(BROADCAST_CERT, dc);
                }
                if (map.get("messageType").equalsIgnoreCase("BURIAL")) {
                    Burial burial = GSON.fromJson(map.get("json"), Burial.class);
                    broadcast(BROADCAST_BURIAL, burial);
                }
                if (map.get("messageType").equalsIgnoreCase("POLICY")) {
                    Policy pol = GSON.fromJson(map.get("json"), Policy.class);
                    broadcast(BROADCAST_USER, pol);
                }
            } else {
                if (map.get("messageType").equalsIgnoreCase("USER")) {
                    sendNotification("Welcome to Blockchain", map.get("json"));
                }
                if (map.get("messageType").equalsIgnoreCase("CERT")) {
                    sendNotification("Death Certificate Issued", map.get("json"));
                }
                if (map.get("messageType").equalsIgnoreCase("CLAIM")) {
                    sendNotification("Burial Registered", map.get("json"));
                }
                if (map.get("messageType").equalsIgnoreCase("BURIAL")) {
                    sendNotification("Burial Registered", map.get("json"));
                }
                if (map.get("messageType").equalsIgnoreCase("POLICY")) {
                    sendNotification("Policy Registered", map.get("json"));
                }
            }
        }
    }

    private void broadcast(String intent, Data data) {
        Intent m = new Intent(intent);
        if (data instanceof DeathCertificate) {
            m.putExtra("data", (DeathCertificate)data);
        }
        if (data instanceof Burial) {
            m.putExtra("data", (Burial)data);
        }
        if (data instanceof Claim) {
            m.putExtra("data", (Claim)data);
        }
        if (data instanceof Policy) {
            m.putExtra("data", (Policy)data);
        }
        LocalBroadcastManager lm = LocalBroadcastManager.getInstance(getApplicationContext());
        lm.sendBroadcast(m);

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

    public static boolean isRunningInForeground(Context context) {
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = null;
            if (am != null) {
                runningProcesses = am.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        for (String activeProcess : processInfo.pkgList) {
                            if (activeProcess.equals(context.getPackageName())) {
                                Log.w(TAG, "isRunningInForeground: TRUE");
                                return true;
                            }
                        }
                    }
                }
            } else {
                return false;
            }


            Log.w(TAG, "isRunningInForeground: FALSE");
        } catch (Exception e) {
            Log.e(TAG, "isRunningInForeground: ", e);
        }

        return false;
    }


    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
}
