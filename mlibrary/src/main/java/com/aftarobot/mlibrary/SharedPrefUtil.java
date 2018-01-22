package com.aftarobot.mlibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.aftarobot.mlibrary.data.FuneralParlour;
import com.aftarobot.mlibrary.data.Hospital;
import com.aftarobot.mlibrary.data.InsuranceCompany;
import com.aftarobot.mlibrary.data.UserDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    public static void saveUser(UserDTO user, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("user", GSON.toJson(user));
        ed.commit();
        Log.i(TAG, "saveUser: " + user.getEmail());
    }

    public static UserDTO getUser(Context ctx) {
        if (ctx == null) return null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String json = sp.getString("user", null);
        UserDTO u = GSON.fromJson(json,UserDTO.class);
        if (u != null) {
            Log.w(TAG, "getUser: " + u.getEmail());
        }
        return u;
    }
    public static void saveCompany(InsuranceCompany company, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("company", GSON.toJson(company));
        ed.commit();
        Log.i(TAG, "saveCompany: " + company.getName());
    }

    public static InsuranceCompany getCompany(Context ctx) {
        if (ctx == null) return null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String json = sp.getString("company", null);
        InsuranceCompany u = GSON.fromJson(json,InsuranceCompany.class);
        if (u != null) {
            Log.w(TAG, "getCompany: " + u.getName());
        }
        return u;
    }
    public static void saveHospital(Hospital hospital, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("hospital", GSON.toJson(hospital));
        ed.commit();
        Log.i(TAG, "saveHospital: " + hospital.getName());
    }

    public static Hospital getHospital(Context ctx) {
        if (ctx == null) return null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String json = sp.getString("hospital", null);
        Hospital u = GSON.fromJson(json,Hospital.class);
        if (u != null) {
            Log.w(TAG, "getHospital: " + u.getName());
        }
        return u;
    }
    public static void saveParlour(FuneralParlour parlour, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("parlour", GSON.toJson(parlour));
        ed.commit();
        Log.i(TAG, "saveParlour: " + parlour.getName());
    }

    public static FuneralParlour getParlour(Context ctx) {
        if (ctx == null) return null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String json = sp.getString("parlour", null);
        FuneralParlour u = GSON.fromJson(json,FuneralParlour.class);
        if (u != null) {
            Log.w(TAG, "getParlour: " + u.getName());
        }
        return u;
    }

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
}
