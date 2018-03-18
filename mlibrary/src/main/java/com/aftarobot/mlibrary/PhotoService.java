package com.aftarobot.mlibrary;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.aftarobot.mlibrary.api.FBListApi;
import com.aftarobot.mlibrary.data.Photo;
import com.aftarobot.mlibrary.data.Wallet;
import com.aftarobot.mlibrary.util.SharedPrefUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class PhotoService extends IntentService {

    public PhotoService() {
        super("PhotoService");
    }

    FirebaseStorageAPI storageAPI;
    private Photo photo;
    public static final String TAG = PhotoService.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "onHandleIntent: ######################### uploading file" );
        if (intent != null) {
            photo = (Photo)intent.getSerializableExtra("photo");
            Log.d(TAG, "onHandleIntent: photo to be uploaded: ".concat(GSON.toJson(photo)));
            storageAPI = new FirebaseStorageAPI();
            startUpload();
        }
    }


    private void startUpload() {

        storageAPI.uploadPhotoUsingStream(photo, getApplicationContext(), new FirebaseStorageAPI.StorageListener() {
            @Override
            public void onResponse(Photo photo) {
                Log.d(TAG, "onResponse, upload complete: ".concat(GSON.toJson(photo)));
                FBListApi api = new FBListApi();
                api.getWallet(photo.getWalletID(), new FBListApi.WalletListener() {
                    @Override
                    public void onResponse(List<Wallet> list) {
                        if (!list.isEmpty()) {
                            Log.e(TAG, "onResponse: wallet: ".concat(GSON.toJson(list.get(0))) );
                            SharedPrefUtil.saveWallet(list.get(0),getApplicationContext());
                        }
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onProgress(long uploaded, long size) {

            }
        });

    }
}
