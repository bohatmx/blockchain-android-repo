package com.aftarobot.mlibrary.api;

/**
 * Created by aubreymalabie on 1/12/18.
 */

import android.util.Log;

import com.aftarobot.mlibrary.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class APIClient {

    public static final String BASE_URL_DEBUG = "http://192.168.86.233:3000/api/";
    public static final String BASE_URL_RELEASE = "http://192.168.86.233:3000/api/";
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient;


    public static Retrofit getClient() {
        String url;
        if (BuildConfig.DEBUG) {
            url = BASE_URL_DEBUG;
        } else {
            url = BASE_URL_RELEASE;
        }
        if (retrofit==null) {
            okHttpClient = new OkHttpClient();
            okHttpClient.newBuilder()
                    .connectTimeout(60,TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS);

            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        Log.e("APIClient", "getClient: executed ok, have retrofit object .....".concat(retrofit.toString()) );
        return retrofit;
    }
}