package com.aftarobot.wallet.services;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.aftarobot.mlibrary.util.SharedPrefUtil;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by aubreymalabie on 1/20/18.
 */

public class FCMInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "onTokenRefresh, token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        Log.d(TAG, "sendRegistrationToServer: and save in pref settings");
        String oldToken = SharedPrefUtil.getCloudMsgToken(getApplicationContext());
        if (oldToken != null) {
            //todo - update token on server ...
        }

        SharedPrefUtil.saveCloudMsgToken(token, getApplicationContext());
        LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(new Intent(BROADCAST_FCMTOKEN));

    }
    public static final String TAG = FCMInstanceIDService.class.getSimpleName(),
        BROADCAST_FCMTOKEN = "COM.AFTAROBOT.BROADCAST_FCMTOKEN";
}
