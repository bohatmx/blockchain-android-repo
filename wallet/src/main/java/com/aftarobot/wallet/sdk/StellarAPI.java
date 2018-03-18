package com.aftarobot.wallet.sdk;

import android.util.Log;

import com.aftarobot.wallet.data.Account;
import com.aftarobot.wallet.data.assets.Assets;
import com.aftarobot.wallet.data.effects.EffectsResponse;
import com.aftarobot.wallet.data.payments.PaymentsResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StellarAPI {
    public static final String TAG = StellarAPI.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private StellarInterface apiService;

    public StellarAPI() {
        apiService = StellarAPIClient.getClient().create(StellarInterface.class);
    }
    public interface AccountListener {
        void onResponse(Account account);
        void onError(String message);
    }
    public interface AssetsListener {
        void onResponse(Assets assets);
        void onError(String message);
    }
    public interface EffectsListener {
        void onResponse(EffectsResponse response);
        void onError(String message);
    }
    public interface PaymentsListener {
        void onResponse(PaymentsResponse response);
        void onError(String message);
    }
    public void getAccountPayments(String accountID, HashMap<String,String> map, final PaymentsListener listener) {
        Call<PaymentsResponse> call = apiService.getAccountPayments(accountID,map);
        Log.w(TAG, "getAccount: call: ".concat(call.request().url().url().toString()) );
        call.enqueue(new Callback<PaymentsResponse>() {
            @Override
            public void onResponse(Call<PaymentsResponse> call, Response<PaymentsResponse> response) {
                if (response.isSuccessful()) {
                    PaymentsResponse paymentsResponse = response.body();
                    Log.e(TAG, "onResponse: getAccountPayments: ".concat(GSON.toJson(paymentsResponse)));
                    listener.onResponse(response.body());
                } else {
                    Log.e(TAG, "onResponse: ".concat(GSON.toJson(response)) );
                    listener.onError("Failed to get Account Payments");
                }
            }

            @Override
            public void onFailure(Call<PaymentsResponse> call, Throwable t) {

            }
        });
    }
    public void getPayments(HashMap<String,String> map, final PaymentsListener listener) {
        Call<PaymentsResponse> call = apiService.getPayments(map);
        Log.w(TAG, "getAccount: call: ".concat(call.request().url().url().toString()) );
        call.enqueue(new Callback<PaymentsResponse>() {
            @Override
            public void onResponse(Call<PaymentsResponse> call, Response<PaymentsResponse> response) {
                if (response.isSuccessful()) {
                    PaymentsResponse paymentsResponse = response.body();
                    Log.w(TAG, "onResponse: getPayments: ".concat(GSON.toJson(paymentsResponse)));
                    listener.onResponse(response.body());
                } else {
                    Log.e(TAG, "onResponse: ".concat(GSON.toJson(response)) );
                    listener.onError("Failed to get Payments");
                }
            }

            @Override
            public void onFailure(Call<PaymentsResponse> call, Throwable t) {

            }
        });
    }
    public void getAccount(String accountID, final AccountListener accountListener) {
        Call<Account> call = apiService.getAccount(accountID);
        Log.w(TAG, "getAccount: call: ".concat(call.request().url().url().toString()) );
        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful()) {
                    Account account = response.body();
                    Log.i(TAG, "onResponse: account: ".concat(GSON.toJson(account)));
                    accountListener.onResponse(response.body());
                } else {
                    Log.e(TAG, "onResponse: ".concat(GSON.toJson(response)) );
                    accountListener.onError("Failed to get Account Details");
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t );
                accountListener.onError(t.getMessage());
            }
        });
    }
    public void getAssets(HashMap<String,String> map, final AssetsListener assetsListener) {
        Call<Assets> call = apiService.getAssets(map);
        Log.w(TAG, "getAssets: call: ".concat(call.request().url().url().toString()) );
        call.enqueue(new Callback<Assets>() {
            @Override
            public void onResponse(Call<Assets> call, Response<Assets> response) {
                if (response.isSuccessful()) {
                    Assets assets = response.body();
                    Log.d(TAG, "onResponse: assets: ".concat(GSON.toJson(assets)));
                    assetsListener.onResponse(assets);
                } else {
                    Log.e(TAG, "onResponse: ".concat(GSON.toJson(response)) );
                    assetsListener.onError("Failed to get assets");
                }
            }

            @Override
            public void onFailure(Call<Assets> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t );
                assetsListener.onError(t.getMessage());
            }
        });
    }
    public void getEffects(HashMap<String,String> map, final EffectsListener effectsListener) {
        Call<EffectsResponse> call = apiService.getEffects(map);
        Log.w(TAG, "getEffects: call: ".concat(call.request().url().url().toString()) );
        call.enqueue(new Callback<EffectsResponse>() {
            @Override
            public void onResponse(Call<EffectsResponse> call, Response<EffectsResponse> response) {
                if (response.isSuccessful()) {
                    EffectsResponse effectsResponse = response.body();
                    Log.d(TAG, "onResponse: effectsResponse: ".concat(GSON.toJson(effectsResponse)));
                    effectsListener.onResponse(effectsResponse);
                } else {
                    Log.e(TAG, "onResponse: ".concat(GSON.toJson(response)) );
                    effectsListener.onError("Failed to get effects");
                }
            }

            @Override
            public void onFailure(Call<EffectsResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t );
                effectsListener.onError(t.getMessage());
            }
        });
    }
    public void getAccountEffects(HashMap<String,String> map, String accountID, final EffectsListener effectsListener) {
        Call<EffectsResponse> call = apiService.getAccountEffects(accountID, map);
        Log.w(TAG, "getAccountEffects: call: ".concat(call.request().url().url().toString()) );
        call.enqueue(new Callback<EffectsResponse>() {
            @Override
            public void onResponse(Call<EffectsResponse> call, Response<EffectsResponse> response) {
                if (response.isSuccessful()) {
                    EffectsResponse effectsResponse = response.body();
                    Log.d(TAG, "onResponse: effectsResponse: ".concat(GSON.toJson(effectsResponse)));
                    effectsListener.onResponse(effectsResponse);
                } else {
                    Log.e(TAG, "onResponse: ".concat(GSON.toJson(response)) );
                    effectsListener.onError("Failed to get effects");
                }
            }

            @Override
            public void onFailure(Call<EffectsResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t );
                effectsListener.onError(t.getMessage());
            }
        });
    }
}
