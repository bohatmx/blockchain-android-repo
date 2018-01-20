package com.aftarobot.mlibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by aubreymalabie on 1/20/18.
 */

public class SharedPrefUtil {
    public static final String TAG = SharedPrefUtil.class.getSimpleName();
    public static void saveCloudMsgToken(String token, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("token", token);
        ed.commit();
        Log.i(TAG, "saveCloudMsgToken: " + token);
    }

    public static String getCloudMsgToken(Context ctx) {
        if (ctx == null) return null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String token = sp.getString("token", null);
        if (token != null) {
            Log.w(TAG, "getCloudMsgToken: " + token);
        }
        return token;
    }
}
