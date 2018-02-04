package com.aftarobot.blockchaintest.crudutils;

import android.content.Context;
import android.util.Log;

import com.aftarobot.mlibrary.api.ChainDataAPI;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.Hospital;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HospitalUtil {

    private static ChainDataAPI chainDataAPI;
    private static int mCount;
    private static HospitalListener mListener;

    public interface HospitalListener {
        void onHospitalsComplete();

        void onProgress(Hospital hospital);

        void onError(String message);
    }

    public static void generate(Context context, final HospitalListener listener) {
        chainDataAPI = new ChainDataAPI(context);
        mListener = listener;
        mCount = 0;
        controlHospitals();
    }

    private static void controlHospitals() {
        if (mCount < 3) {
            switch (mCount) {
                case 0:
                    Hospital h1 = new Hospital();
                    h1.setHospitalId("HOSPITAL_001");
                    h1.setName("Durban Hospital");
                    h1.setAddress("8 Mandela Road, Durban");
                    h1.setEmail("info@durbanhosp.co.za");
                    h1.setLatitude(-25.8373874);
                    h1.setLongitude(27.3536478);
                    writeHospital(h1);
                    break;
                case 1:
                    Hospital h2 = new Hospital();
                    h2.setHospitalId("HOSPITAL_002");
                    h2.setName("Dobsonville Hospital");
                    h2.setAddress("45 Moloko Road, Dobsonville");
                    h2.setEmail("info@dobson.co.za");
                    h2.setLatitude(-25.8373874);
                    h2.setLongitude(27.3536478);
                    writeHospital(h2);
                    break;
                case 2:
                    Hospital h3 = new Hospital();
                    h3.setHospitalId("HOSPITAL_003");
                    h3.setName("Bryanston Life Hospital");
                    h3.setAddress("677 Kingfisher Road, Bryanston");
                    h3.setEmail("info@lifebryans.co.za");
                    h3.setLatitude(-25.8373874);
                    h3.setLongitude(27.3536478);
                    writeHospital(h3);
                    break;
            }

        } else {
            mListener.onHospitalsComplete();
        }


    }

    private static void writeHospital(Hospital hospital) {
        chainDataAPI.addHospital(hospital, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                Hospital x = (Hospital) data;
                Log.d(TAG, "onResponse, hospital added: ".concat(GSON.toJson(x)));
                mListener.onProgress(x);
                mCount++;
                controlHospitals();
            }

            @Override
            public void onError(String message) {
                mListener.onError(message);
                mCount++;
                controlHospitals();
            }
        });
    }


    public static final String TAG = HospitalUtil.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

}