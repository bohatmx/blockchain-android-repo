package com.aftarobot.mlibrary.api;

/**
 * Created by aubreymalabie on 1/12/18.
 */

import com.aftarobot.mlibrary.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class APIClient {

    public static final String BASE_URL_DEBUG = "http://192.168.1.233:3000/api/";
    public static final String BASE_URL_RELEASE = "http://192.168.1.233:3000/api/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        String url;
        if (BuildConfig.DEBUG) {
            url = BASE_URL_DEBUG;
        } else {
            url = BASE_URL_RELEASE;
        }
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}