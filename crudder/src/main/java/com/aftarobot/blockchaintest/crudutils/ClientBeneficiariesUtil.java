package com.aftarobot.blockchaintest.crudutils;

import android.content.Context;
import android.util.Log;

import com.aftarobot.mlibrary.api.ChainDataAPI;
import com.aftarobot.mlibrary.api.FBApi;
import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.Data;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class ClientBeneficiariesUtil {

    public interface ClientBennieListener {
        void onClientAndBeneficiariesAdded();

        void onError(String message);

        void onProgress(String message);
    }

    private static ClientBennieListener mListener;
    private static Client mClient;
    private static List<Beneficiary> mBeneficiaries;
    private static int index;
    private static ChainDataAPI chainDataAPI;
    private static FBApi firebaseAPI;

    public static void addClientAndBeneficiaries(Context context, Client client,
                                                 List<Beneficiary> beneficiaries, final ClientBennieListener listener) {
        mListener = listener;
        mClient = client;
        mBeneficiaries = beneficiaries;
        chainDataAPI = new ChainDataAPI(context);
        firebaseAPI = new FBApi();

        Log.d(TAG, "addClientAndBeneficiaries: ###### ".concat(client.getFullName()).concat(" beneficiaries: ")
                .concat(String.valueOf(beneficiaries.size())));
        addClient();
    }

    private static void addClient() {
        chainDataAPI.addClient(mClient, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                final Client client = (Client) data;
                Log.i(TAG, "onResponse: client added to blockchain: ".concat(GSON.toJson(client)));
                mListener.onProgress("Client added to blockchain: ".concat(client.getFullName()));
                firebaseAPI.addClient(client, new FBApi.FBListener() {
                    @Override
                    public void onResponse(Data data) {
                        Log.w(TAG, "onResponse: client added to firebase: ".concat(client.getFullName()) );
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
                index = 0;
                controlBeneficiaries();
            }

            @Override
            public void onError(String message) {
                mListener.onError(message);
            }
        });
    }

    private static void controlBeneficiaries() {
        if (index < mBeneficiaries.size()) {
            addBeneficiary(mBeneficiaries.get(index));
        } else {
            mListener.onClientAndBeneficiariesAdded();
        }
    }

    private static void addBeneficiary(Beneficiary beneficiary) {
        chainDataAPI.addBeneficiary(beneficiary, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                final Beneficiary b = (Beneficiary) data;
                firebaseAPI.addBeneficiary(b, new FBApi.FBListener() {
                    @Override
                    public void onResponse(Data data) {
                        Log.d(TAG, "addBeneficiary onResponse: beneficiary added to firebase: ".concat(b.getFullName()));
                    }

                    @Override
                    public void onError(String message) {
                        mListener.onError(message);
                    }
                });
                Log.d(TAG, "onResponse: beneficiary added to blockchain: ".concat(GSON.toJson(b)));
                mListener.onProgress("Beneficiary added to blockchain: ".concat(b.getFullName()));
                index++;
                controlBeneficiaries();
            }

            @Override
            public void onError(String message) {
                mListener.onError(message);
                index++;
                controlBeneficiaries();

            }
        });
    }

    private static final String TAG = ClientBeneficiariesUtil.class.getSimpleName();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
}
