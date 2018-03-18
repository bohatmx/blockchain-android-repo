package com.aftarobot.wallet.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.aftarobot.wallet.BuildConfig;
import com.aftarobot.mlibrary.data.Payment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by aubreyM on 16/01/15.
 */

/**
 * Helper class to wrap OKHttp library and provide methods
 * that access data from Monitor Platform server
 */
public class OKUtil {


    private static final Gson gson = new Gson();
    public static final String DEV_URL = "https://us-central1-blockchaindev-e50c8.cloudfunctions.net/p02-addMessage";
    public static final String PROD_URL = "https://us-central1-blockchaindev-e50c8.cloudfunctions.net/p02-addMessage";

    static final String FAILED_RESPONSE_NOT_SUCCESSFUL = "Request failed. Response not successful";
    static final String FAILED_IO = "Request failed. Communication links are not working";

    static final String TAG = OKUtil.class.getSimpleName();

    public interface OKListener {
        void onResponse(String response);

        void onError(String message);
    }

    public OKUtil() {

    }

    private void configureTimeouts(OkHttpClient client) {
        client.setConnectTimeout(40, TimeUnit.SECONDS);
        client.setReadTimeout(60, TimeUnit.SECONDS);
        client.setWriteTimeout(40, TimeUnit.SECONDS);

    }

    private String getURL(Context ctx) {

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "getURL: isDebuggable: ".concat(DEV_URL));
            return DEV_URL;
        } else {
            Log.d(TAG, "getURL: isRelease: ".concat(PROD_URL));
            return PROD_URL;
        }
    }
    private OkHttpClient client = new OkHttpClient();

    public void send(final Payment req, final Activity activity,
                     final OKListener listener) throws OKHttpException {

        final String url = getURL(activity);
        configureTimeouts(client);

        Log.e(TAG, "Getting Firebase id token before sending ...");
        Task<GetTokenResult> task = FirebaseAuth.getInstance().getCurrentUser().getIdToken(true);
        task.addOnSuccessListener(activity, new OnSuccessListener<GetTokenResult>() {
            @Override
            public void onSuccess(GetTokenResult getTokenResult) {
                Log.e(TAG, "onSuccess: token: ".concat(Objects.requireNonNull(getTokenResult.getToken())));
                RequestBody body = new FormEncodingBuilder()
                        .add("JSON", gson.toJson(req))
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .header("Authorization", String.format("Bearer %s", getTokenResult.getToken()))
                        .header("Content-Type", "application/json")
                        .post(body)
                        .build();
                Log.w(TAG, "### sending POST: Payment "
                        + "\n" + request.urlString());
                execute(client, request, activity, listener);

            }
        }) .addOnFailureListener(activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ",e );
                    }
                });

    }
    

    private void execute(OkHttpClient client, final Request req,
                         final Activity activity,
                         final OKListener listener) {

        final long start = System.currentTimeMillis();
        Callback callback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                long end = System.currentTimeMillis();
                Log.e(TAG, "### Server responded with ERROR, round trip elapsed: " + getElapsed(start, end), e);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onError(FAILED_IO);
                    }
                });
            }
            @Override
            public void onResponse(Response response) throws IOException {

                if (!response.isSuccessful()) {
                    Log.e(TAG, "%%%%%% ERROR from cloud function:: "
                            + response.body().string());
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onError(FAILED_RESPONSE_NOT_SUCCESSFUL);
                            return;
                        }
                    });

                } else {
                    String msg = response.body().string();
                    Log.i("OKUtil", "possible GOOD RESPONSE from cloud functions ");
                    listener.onResponse(msg);
                }
                response.body().close();
            }
        };

        client.newCall(req).enqueue(callback);

    }


    String getElapsed(long start, long end) {
        BigDecimal bs = new BigDecimal(start);
        BigDecimal be = new BigDecimal(end);
        BigDecimal a = be.subtract(bs).divide(new BigDecimal(1000), 2, BigDecimal.ROUND_HALF_UP);

        return a.doubleValue() + " seconds";
    }

}
