package com.aftarobot.mlibrary.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.aftarobot.mlibrary.data.Bank;
import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.FuneralParlour;
import com.aftarobot.mlibrary.data.Hospital;
import com.aftarobot.mlibrary.data.InsuranceCompany;
import com.aftarobot.mlibrary.data.UserDTO;
import com.aftarobot.mlibrary.data.Wallet;
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
        ed.apply();
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
    public static void saveThemeIndex(int index, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("themeIndex", index);
        ed.apply();
        Log.i(TAG, "saveThemeIndex: " + index);
    }

    public static int getThemeIndex(Context ctx) {
        if (ctx == null) return 0;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        int token = sp.getInt("themeIndex", 0);
        return token;
    }
    public static void saveAccountNumber(String token, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("accountNumber", token);
        ed.apply();
        Log.i(TAG, "saveAccountNumber: " + token);
    }

    public static String getAccountNumber(Context ctx) {
        if (ctx == null) return null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String token = sp.getString("accountNumber", null);
        if (token != null) {
            Log.w(TAG, "getAccountNumber: " + token);
        }
        return token;
    }

    public static void saveBank(Bank user, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("bank", GSON.toJson(user));
        ed.apply();
        Log.i(TAG, "saveBank: " + user.getName());
    }

    public static Bank getBank(Context ctx) {
        if (ctx == null) return null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String json = sp.getString("bank", null);
        Bank u = GSON.fromJson(json,Bank.class);
        if (u != null) {
            Log.w(TAG, "getBank: " + u.getName());
        }
        return u;
    }
    public static void saveUser(UserDTO user, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("user", GSON.toJson(user));
        ed.apply();
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
    public static void saveWallet(Wallet wallet, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("wallet", GSON.toJson(wallet));
        ed.apply();
        Log.i(TAG, "saveWallet: " + wallet.getEmail());
    }

    public static Wallet getWallet(Context ctx) {
        if (ctx == null) return null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String json = sp.getString("wallet", null);
        Wallet u = GSON.fromJson(json,Wallet.class);
        if (u != null) {
            Log.w(TAG, "getWallet: " + u.getEmail());
        }
        return u;
    }
    public static void saveCompany(InsuranceCompany company, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("company", GSON.toJson(company));
        ed.apply();
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
        ed.apply();
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
        ed.apply();
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

    public static void saveBeneficiary(Beneficiary user, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        if (user == null) {
            ed.remove("bennie");
            ed.apply();
            Log.e(TAG, "saveBeneficiary: removed beneficiary from SharedPreferences" );
            return;
        }
        ed.putString("bennie", GSON.toJson(user));
        ed.apply();
        Log.i(TAG, "saveBeneficiary: " + user.getFullName());
    }

    public static Beneficiary getBeneficiary(Context ctx) {
        if (ctx == null) return null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String json = sp.getString("bennie", null);
        Beneficiary u = GSON.fromJson(json,Beneficiary.class);
        if (u != null) {
            Log.w(TAG, "getBeneficiary: " + u.getFullName());
        }
        return u;
    }

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void saveClient(Client user, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("client", GSON.toJson(user));
        ed.apply();
        Log.i(TAG, "saveClient: " + user.getEmail());
    }

    public static Client getClient(Context ctx) {
        if (ctx == null) return null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String json = sp.getString("client", null);
        Client u = GSON.fromJson(json,Client.class);
        if (u != null) {
            Log.w(TAG, "getClient: " + u.getEmail());
        }
        return u;
    }
}
