package com.aftarobot.mlibrary;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.aftarobot.mlibrary.api.FBApi;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.Photo;
import com.aftarobot.mlibrary.data.Wallet;
import com.aftarobot.mlibrary.util.SharedPrefUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;


/**
 * Created by josephmokenela on 10/14/16.
 */

public class FirebaseStorageAPI {

    private static final String TAG = FirebaseStorageAPI.class.getSimpleName();

    private StorageReference storageReference;
    private FBApi dataAPI;


    public FirebaseStorageAPI() {
        dataAPI = new FBApi();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public interface StorageListener {
        void onResponse(Photo photo);

        void onError(String message);

        void onProgress(long uploaded, long size);
    }

    public static final String
            PHOTOS = "photos/";

    public void uploadPhotoUsingStream(final Photo photo, Context ctx,
                                       final StorageListener listener) {

        final LocalBroadcastManager m = LocalBroadcastManager.getInstance(ctx);
        Log.d(TAG, "uploadPhotoUsingStream: phototype: " + photo.getPhotoType());
        final File f = new File(photo.getFilePath());
        if (f.exists()) {
            Log.d(TAG, "uploadPhotoUsingFile: prior to upload: "
                    + f.length() + " - " + f.getAbsolutePath());
        } else {
            listener.onError("Cannot find file for upload");
            return;
        }
        Uri uri = Uri.fromFile(new File(photo.getFilePath()));
        Wallet w = SharedPrefUtil.getWallet(ctx);
        StorageReference photoReference = storageReference.child(PHOTOS
                + w.getUid() + "/"
                + uri.getLastPathSegment());
        Log.d(TAG, "uploadPhoto: starting upload ...: " + uri.getLastPathSegment());
        try {
            photoReference.putStream(new FileInputStream(f)).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.i(TAG, "The photo has been succesfully uploaded: "
                                    + taskSnapshot.getDownloadUrl().toString());
                            photo.setUrl(taskSnapshot.getDownloadUrl().toString());
                            photo.setFilePath(null);
                            dataAPI.addPhoto(photo, new FBApi.FBListener() {
                                @Override
                                public void onResponse(Data data) {
                                    Log.w(TAG, "onResponse: photo added to firebase, OK: ");
                                    Photo p = (Photo) data;
                                    listener.onResponse(p);
                                }

                                @Override
                                public void onError(String message) {

                                }
                            });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Unable to upload photo", e);
                    listener.onError("Unable to upload photo");

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.w(TAG, "######## onProgress: "
                            + taskSnapshot.getBytesTransferred()
                            + " of " + f.length() + " uploaded");
                    Intent i = new Intent(BROADCAST_UPLOAD_PROGRESS);
                    i.putExtra("length", f.length());
                    i.putExtra("done",taskSnapshot.getBytesTransferred());
                    m.sendBroadcast(i);
                    listener.onProgress(taskSnapshot.getBytesTransferred(), f.length());
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "uploadPhotoUsingFile: we fell down", e);
            listener.onError("Unable to upload the bleeding file");
        }
    }

    public static final String BROADCAST_UPLOAD_PROGRESS = "com.aftarobot.BROADCAST_UPLOAD_PROGRESS";
    public void uploadPhoto(final Photo photo, Context ctx,
                            final StorageListener listener) {


        Wallet user = SharedPrefUtil.getWallet(ctx);
        Uri uri = Uri.fromFile(new File(photo.getFilePath()));
        StorageReference photoReference = storageReference.child(PHOTOS
                + user.getUid() + "/"
                + uri.getLastPathSegment());
        Log.d(TAG, "uploadPhoto: starting upload ...: " + uri.getLastPathSegment());
        photoReference.putFile(uri).addOnSuccessListener(
                new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.i(TAG, "The photo has been succesfully uploaded: "
                                + taskSnapshot.getDownloadUrl().toString());
                        photo.setUrl(taskSnapshot.getDownloadUrl().toString());
                        photo.setBytes(taskSnapshot.getTotalByteCount());
                        dataAPI.addPhoto(photo, new FBApi.FBListener() {
                            @Override
                            public void onResponse(Data data) {
                                Log.w(TAG, "onResponse: photo added to firebase, OK: ");
                                Photo p = (Photo) data;
                                listener.onResponse(p);
                            }

                            @Override
                            public void onError(String message) {

                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Unable to upload photo", e);
                listener.onError("Unable to upload photo");

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                Log.w(TAG, "######## onProgress: " + taskSnapshot.getBytesTransferred()
                        + " of " + taskSnapshot.getTotalByteCount() + " uploaded");
            }
        });
    }
}
