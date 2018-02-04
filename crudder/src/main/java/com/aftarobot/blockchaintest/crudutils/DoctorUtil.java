package com.aftarobot.blockchaintest.crudutils;

import android.content.Context;
import android.util.Log;

import com.aftarobot.mlibrary.api.ChainDataAPI;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.Doctor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DoctorUtil {

    private static ChainDataAPI chainDataAPI;
    private static int mCount;
    private static DoctorListener mListener;

    public interface DoctorListener {
        void onDoctorsComplete();

        void onProgress(Doctor doc);

        void onError(String message);
    }

    public static void generate(Context context, final DoctorListener listener) {
        chainDataAPI = new ChainDataAPI(context);
        mListener = listener;
        mCount = 0;
        controlDoctors();
    }

    private static void controlDoctors() {
        if (mCount < 4) {
            switch (mCount) {
                case 0:
                    Doctor d1 = new Doctor();
                    d1.setIdNumber("DOCTOR_001");
                    d1.setFirstName("Rirhandzu");
                    d1.setLastName("Maluleke");
                    d1.setEmail("riri@gmail.com");
                    d1.setCellPhone("087 344 3690");
                    writeDoctor(d1);
                    break;
                case 1:
                    Doctor d2 = new Doctor();
                    d2.setIdNumber("DOCTOR_002");
                    d2.setFirstName("Mmathebe");
                    d2.setLastName("Jika");
                    d2.setEmail("mathebe@gmail.com");
                    d2.setCellPhone("087 727 9546");
                    writeDoctor(d2);
                    break;
                case 2:
                    Doctor d3 = new Doctor();
                    d3.setIdNumber("DOCTOR_003");
                    d3.setFirstName("Fanyana");
                    d3.setLastName("Shiburi");
                    d3.setEmail("selwyn@gmail.com");
                    d3.setCellPhone("087 987 1212");
                    writeDoctor(d3);
                    break;
                case 3:
                    Doctor d4 = new Doctor();
                    d4.setIdNumber("DOCTOR_004");
                    d4.setFirstName("MaryAnne");
                    d4.setLastName("Pieterse");
                    d4.setEmail("maryanne@gmail.com");
                    d4.setCellPhone("087 727 7355");
                    writeDoctor(d4);
                    break;
            }

        } else {
            mListener.onDoctorsComplete();
        }


    }

    private static void writeDoctor(Doctor doctor) {
        chainDataAPI.addDoctor(doctor, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                Doctor x = (Doctor) data;
                Log.d(TAG, "onResponse, doctor added: ".concat(GSON.toJson(x)));
                mListener.onProgress(x);
                mCount++;
                controlDoctors();
            }

            @Override
            public void onError(String message) {
                mListener.onError(message);
                mCount++;
                controlDoctors();
            }
        });
    }


    public static final String TAG = DoctorUtil.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

}