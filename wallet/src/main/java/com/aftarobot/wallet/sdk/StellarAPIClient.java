package com.aftarobot.wallet.sdk;

import android.util.Log;

import com.aftarobot.mlibrary.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StellarAPIClient {
    public static final String BASE_URL_DEBUG = " https://horizon-testnet.stellar.org/";
    public static final String BASE_URL_RELEASE = "http://192.168.8.100:3000/api/";
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient;


    public static Retrofit getClient() {
        String url;
        if (BuildConfig.DEBUG) {
            url = BASE_URL_DEBUG;
        } else {
            url = BASE_URL_RELEASE;
        }
        return getRetro(url);
    }


    private static Retrofit getRetro(String url) {

        okHttpClient = new OkHttpClient();
        okHttpClient.newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        Log.e("APIClient", "getClient: executed ok, have retrofit object .....".concat(retrofit.toString()));
        return retrofit;
    }
}
