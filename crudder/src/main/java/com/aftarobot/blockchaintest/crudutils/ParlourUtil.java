package com.aftarobot.blockchaintest.crudutils;

import android.content.Context;
import android.util.Log;

import com.aftarobot.mlibrary.api.ChainDataAPI;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.FuneralParlour;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ParlourUtil {

    private static ChainDataAPI chainDataAPI;
    private static int mCount;
    private static FuneralParlourListener mListener;

    public interface FuneralParlourListener {
        void onFuneralParloursComplete();

        void onProgress(FuneralParlour parlour);

        void onError(String message);
    }

    public static void generate(Context context, final FuneralParlourListener listener) {
        chainDataAPI = new ChainDataAPI(context);
        mListener = listener;
        mCount = 0;
        controlFuneralParlours();
    }

    private static void controlFuneralParlours() {
        if (mCount < 3) {
            switch (mCount) {
                case 0:
                    FuneralParlour fp1 = new FuneralParlour();
                    fp1.setFuneralParlourId("FUNERAL_PARLOUR_001");
                    fp1.setName("Soweto Funeral Services");
                    fp1.setEmail("info@sowetofs.com");
                    fp1.setAddress("635 Khunou Street, Moroka, Soweto");
                    fp1.setLatitude(-25.363868);
                    fp1.setLongitude(27.367578);
                    writeFuneralParlour(fp1);

                    break;
                case 1:
                    FuneralParlour fp2 = new FuneralParlour();
                    fp2.setFuneralParlourId("FUNERAL_PARLOUR_002");
                    fp2.setName("Diepkloof Funeral Home");
                    fp2.setEmail("info@sowetofs.com");
                    fp2.setAddress("1243 Mazomba Street, Diepkloof Zone 3, Soweto");
                    fp2.setLatitude(-25.870656);
                    fp2.setLongitude(27.367578);
                    writeFuneralParlour(fp2);
                    break;
                case 2:
                    FuneralParlour fp3 = new FuneralParlour();
                    fp3.setFuneralParlourId("FUNERAL_PARLOUR_003");
                    fp3.setName("Hatfield Funeral Directors");
                    fp3.setEmail("info@hatfieldfunerals.com");
                    fp3.setAddress("666 Hatfiled  Street, Hatfield, Pretoria");
                    fp3.setLatitude(-25.870656);
                    fp3.setLongitude(27.367578);
                    writeFuneralParlour(fp3);
                    break;
            }

        } else {
            mListener.onFuneralParloursComplete();
        }


    }

    private static void writeFuneralParlour(FuneralParlour parlour) {
        chainDataAPI.addFuneralParlour(parlour, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                FuneralParlour x = (FuneralParlour) data;
                Log.d(TAG, "onResponse, parlour added: ".concat(GSON.toJson(x)));
                mListener.onProgress(x);
                mCount++;
                controlFuneralParlours();
            }

            @Override
            public void onError(String message) {
                mListener.onError(message);
                mCount++;
                controlFuneralParlours();
            }
        });
    }


    public static final String TAG = ParlourUtil.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

}