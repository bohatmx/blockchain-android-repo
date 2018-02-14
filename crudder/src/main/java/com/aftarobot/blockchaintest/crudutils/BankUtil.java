package com.aftarobot.blockchaintest.crudutils;

import android.content.Context;
import android.util.Log;

import com.aftarobot.mlibrary.api.ChainDataAPI;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.Bank;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class BankUtil {

    private static ChainDataAPI chainDataAPI;
    private static int mCount;
    private static BankListener mListener;
    private static List<Bank> banks = new ArrayList<>();

    public interface BankListener {
        void onBanksComplete(List<Bank> banks);

        void onProgress(Bank doc);

        void onError(String message);
    }

    public static void generate(Context context, final BankListener listener) {
        chainDataAPI = new ChainDataAPI(context);
        mListener = listener;
        mCount = 0;
        controlBanks();
    }

    private static void controlBanks() {
        if (mCount < 2) {
            switch (mCount) {
                case 0:
                    Bank d1 = new Bank();
                    d1.setBankId("BANK_001");
                    d1.setName("Real Big Bank");
                    d1.setEmail("info@realbigbank.com");
                    d1.setPhone("011 344 3690");
                    writeBank(d1);
                    break;
                case 1:
                    Bank d2 = new Bank();
                    d2.setBankId("BANK_002");
                    d2.setName("SmallPeople Bank");
                    d2.setEmail("info@smallpeoplebank.com");
                    d2.setPhone("031 727 9546");
                    writeBank(d2);
                    break;

            }

        } else {
            mListener.onBanksComplete(banks);
        }


    }

    private static void writeBank(Bank doctor) {
        chainDataAPI.addBank(doctor, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                Bank x = (Bank) data;
                banks.add(x);
                Log.d(TAG, "onResponse, doctor added: ".concat(GSON.toJson(x)));
                mListener.onProgress(x);
                mCount++;
                controlBanks();
            }

            @Override
            public void onError(String message) {
                mListener.onError(message);
                mCount++;
                controlBanks();
            }
        });
    }


    public static final String TAG = BankUtil.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

}