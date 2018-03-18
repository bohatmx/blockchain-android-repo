package com.aftarobot.mlibrary;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aftarobot.mlibrary.api.FBApi;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.Photo;
import com.aftarobot.mlibrary.data.Wallet;
import com.aftarobot.mlibrary.util.SharedPrefUtil;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoTakerActivity extends AppCompatActivity {

    File photoFile;
    File currentThumbFile;
    Button btnUpload;
    TextView  txtCount;
    ImageView imageView;
    FloatingActionButton fab;
    Toolbar toolbar;
    TextInputEditText editName;
    ImageButton btnName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_taker);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Wallet Profile");
        wallet = SharedPrefUtil.getWallet(this);

        setFields();
        getSupportActionBar().setSubtitle(wallet.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
        listen();
    }

    private void updateWallet() {
        if (TextUtils.isEmpty(editName.getText())) {
            Toast.makeText(this,"Enter name or nickname", Toast.LENGTH_SHORT).show();
            return;
        }
        hideKeyboard();
        wallet.setName(editName.getText().toString());
        getSupportActionBar().setSubtitle(wallet.getName());
        SharedPrefUtil.saveWallet(wallet,this);
        FBApi api = new FBApi();
        api.updateWallet(wallet, new FBApi.FBListener() {
            @Override
            public void onResponse(Data data) {
                Toast.makeText(getApplicationContext(),"Name updated. Thanks", Toast.LENGTH_SHORT).show();
                editName.setVisibility(View.GONE);
                btnName.setVisibility(View.GONE);
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });

    }
    private void setFields() {
        btnUpload = findViewById(R.id.btnUpload);
        editName = findViewById(R.id.editName);
        btnName = findViewById(R.id.btnNickname);
        btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWallet();
            }
        });
        btnName.setVisibility(View.GONE);
        editName.setHint(wallet.getName());
        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnName.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        txtCount = findViewById(R.id.CAM_count);
        imageView = findViewById(R.id.CAM_image);

        fab = findViewById(R.id.fab);


        txtCount.setText("0");
        btnUpload.setVisibility(View.GONE);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cachePhoto();
            }
        });

    }

    private Photo photo;
    private Wallet wallet;

    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent data) {
        Log.e(TAG, "##### onActivityResult requestCode: " + requestCode
                + " resultCode: " + resultCode);

        switch (requestCode) {
            case CAPTURE_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    new PhotoTask().execute();
                } else {
                    showError("Failed to process picture");
                }
                btnUpload.setVisibility(View.VISIBLE);
                break;

        }
    }
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private void cachePhoto() {
        Log.e(TAG, "cachePhoto: wallet, check wallet id".concat(GSON.toJson(wallet)) );
        File file = new File(photo.getFilePath());
        photo.setBytes(file.length());
        photo.setUid(wallet.getUid());
        photo.setWalletID(wallet.getWalletID());
        photo.setDateTaken(new Date().getTime());

        showSnack("Uploading photo ...","ok", "cyan");
        btnUpload.setVisibility(View.GONE);
        Log.w(TAG, "cachePhoto: ================= starting service ......" );
        Intent m = new Intent(this, PhotoService.class);
        m.putExtra("photo", photo);
        startService(m);
    }

    private void dispatchTakePictureIntent() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "WRITE_EXTERNAL_STORAGE permission not granted yet");
            checkPermissions();
            return;
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = createImageFile();
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    photoUri = FileProvider.getUriForFile(getApplicationContext(),
                            getApplicationContext().getPackageName().concat(".fileprovider"),
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(takePictureIntent, CAPTURE_IMAGE);
                }
            } catch (IOException ex) {
                showError("Unable to create file for camera app");
            }

        }

    }

    Uri photoUri;
    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File[] storageDirs = ContextCompat.getExternalFilesDirs(this, Environment.DIRECTORY_PICTURES);
        File storageDir = null;
        File image = null;
        if (storageDirs != null) {
            storageDir = storageDirs[0];
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = image.getAbsolutePath();

        }

        return image;
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, ".......................onConnected: Requesting external storage  permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);

        } else {
            dispatchTakePictureIntent();
        }

    }

    static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 811, CAPTURE_IMAGE = 891;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.w(TAG, "PERMISSIONS_WRITE_EXTERNAL_STORAGE permission granted");
                    dispatchTakePictureIntent();

                } else {
                    Log.e(TAG, "DISK_PERMISSION permission denied");
                    //test

                }
            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public static final String DIRECTORY = "aftarobot_app", TAG = PhotoTakerActivity.class.getSimpleName();

    private Snackbar snackbar;

    private void showError(String message) {
        snackbar = Snackbar.make(toolbar, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor("red"));
        snackbar.setAction("Error", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    private void showSnack(String message, String action, String color) {
        snackbar = Snackbar.make(toolbar, message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }
    private void showSnackIndefinite(String message, String action, String color) {
        snackbar = Snackbar.make(toolbar, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    class PhotoTask extends AsyncTask<Void, Void, Integer> {

        double latitude, longitude;

        /**
         * Scale the image to required size and delete the larger one
         *
         * @param voids
         * @return
         */
        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                if (photoFile == null || photoFile.length() == 0) {
                    Log.e(TAG, "----->> photoFile is null or length 0, exiting");
                    return 99;
                } else {
                    Log.w(TAG, "## PhotoTask starting, photoFile length: "
                            + photoFile.length());
                }
                processFile();
            } catch (Exception e) {
                Log.e(TAG, "Camera file processing failed", e);
                return 9;
            }


            return 0;
        }


        void processFile() throws Exception {


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            options.outHeight = 768 / 2;
            options.outWidth = 1024 / 2;
            Bitmap main = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), options);
            getLog(main, "main bitmap");

            if (main.getWidth() > main.getHeight()) {
                Log.d(TAG, "*** this image in landscape ...............................");
                main = rotateBitmap(main);

            }
            getLog(main, "decoded Bitmap");
            currentThumbFile = getFileFromBitmap(main,
                    "t" + System.currentTimeMillis() + ".jpg");


            main.recycle();
            Log.i(TAG, "## photo file length: " + getLength(currentThumbFile.length())
                    + ", original size: " + getLength(photoFile.length()));

        }

        Bitmap rotateBitmap(Bitmap bm) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            // Rotate Bitmap
            Matrix matrix = new Matrix();
            Log.d(TAG, "rotationAngle: 90");
            matrix.setRotate(90, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);

            // Return result
            return rotatedBitmap;
        }

        File getFileFromBitmap(Bitmap bm, String filename)
                throws Exception {
            if (bm == null) throw new Exception();
            File file = null;
            try {
                File rootDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                if (rootDir == null) {
                    rootDir = Environment.getRootDirectory();
                }
                File imgDir = new File(rootDir, "wallet_app");
                if (!imgDir.exists()) {
                    imgDir.mkdir();
                }
                OutputStream outStream = null;
                file = new File(imgDir, filename);
                if (file.exists()) {
                    file.delete();
                    file = new File(imgDir, filename);
                }
                outStream = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.flush();
                outStream.close();

            } catch (Exception e) {
                Log.e(TAG, "Failed to get file from bitmap", e);
            }
            return file;

        }

        private String getLength(long num) {
            BigDecimal decimal = new BigDecimal(num).divide(new BigDecimal(1024), 2, BigDecimal.ROUND_HALF_UP);

            return "" + decimal.doubleValue() + " KB";
        }

        @Override
        protected void onPostExecute(Integer result) {
            Log.e(TAG, "onPostExecute result: " + result.intValue());
            if (result > 0) {
                pictureTakenOK = false;
                Snackbar.make(imageView, "We have a problem with camera", Snackbar.LENGTH_LONG).show();
                return;
            }
            pictureTakenOK = true;
            Glide.with(getApplicationContext()).load(currentThumbFile).into(imageView);
            pictureCount++;
            txtCount.setText("" + pictureCount);
            //
            btnUpload.setVisibility(View.VISIBLE);
            photo = new Photo();
            photo.setWalletID(wallet.getWalletID());
            photo.setFilePath(currentThumbFile.getAbsolutePath());

        }
    }

    boolean pictureTakenOK;
    int pictureCount;

    private void getLog(Bitmap bm, String which) {
        if (bm == null) return;
        Log.e(TAG, which + " - bitmap: width: "
                + bm.getWidth() + " height: "
                + bm.getHeight() + " rowBytes: "
                + bm.getRowBytes());
    }

    private void listen() {
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(
                        new ProgressReceiver(),
                        new IntentFilter(FirebaseStorageAPI.BROADCAST_UPLOAD_PROGRESS));
    }

    private class ProgressReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "ProgressReceiver onReceive: &&&&&&&&&&&&&&&&&&&" );
            long length = intent.getLongExtra("length",0);
            long done = intent.getLongExtra("done",0);

            Log.d(TAG, "onReceive: length: " + length + " done: " + done) ;


            BigDecimal bLen = new BigDecimal(length);
            BigDecimal bdone = new BigDecimal(done);
            bLen = bLen.divide(new BigDecimal(1024));
            bdone = bdone.divide(new BigDecimal(1024));

            double xlength = bLen.doubleValue();
            double xdone = bdone.doubleValue();

            bleep( xlength, xdone);

        }
    }
    DecimalFormat df = new DecimalFormat("###,###,###,##0.0");
    private void bleep(final double length, final double done) {
        Log.d(TAG, "bleep: length: " + length + " done: " + done + " " ) ;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();
                sb.append("Uploaded  ").append(df.format(done)).append("K");
                sb.append(" of ").append(df.format(length)).append("K    ");
                showSnackIndefinite(sb.toString(),"ok","green");
            }
        });


    }
    void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);
    }
}
