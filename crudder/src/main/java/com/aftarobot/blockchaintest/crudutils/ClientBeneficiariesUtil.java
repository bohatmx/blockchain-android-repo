package com.aftarobot.blockchaintest.crudutils;

import android.content.Context;
import android.util.Log;

import com.aftarobot.mlibrary.api.ChainDataAPI;
import com.aftarobot.mlibrary.api.FBApi;
import com.aftarobot.mlibrary.data.Bank;
import com.aftarobot.mlibrary.data.BankAccount;
import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.util.ListUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class ClientBeneficiariesUtil {

    public interface ClientBennieListener {
        void onClientAndBeneficiariesAdded(Client mClient);

        void onError(String message);

        void onProgress(String message);
    }

    private static ClientBennieListener mListener;
    private static Client mClient;
    private static List<Beneficiary> mBeneficiaries;
    private static int index;
    private static ChainDataAPI chainDataAPI;
    private static FBApi firebaseAPI;
    private static Bank mBank;

    public static void addClientAndBeneficiaries(Context context, Client client, Bank bank,
                                                 List<Beneficiary> beneficiaries, final ClientBennieListener listener) {
        mListener = listener;
        mClient = client;
        mBank = bank;
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
                mClient = client;
                Log.i(TAG, "onResponse: client added to blockchain: ".concat(GSON.toJson(client)));
                mListener.onProgress("Client added: ".concat(client.getFullName()));

                final BankAccount account = new BankAccount();
                account.setAccountNumber(ListUtil.getRandomAccountNumber());
                account.setBalance(0.00);
                account.setClient("resource:com.oneconnect.insurenet.Client#".concat(client.getIdNumber()));
                account.setBank("resource:com.oneconnect.insurenet.Bank#".concat(mBank.getBankId()));
                Log.d(TAG, "account to register: ".concat(GSON.toJson(account)));

                chainDataAPI.registerClientBankAccount(account, new ChainDataAPI.Listener() {
                    @Override
                    public void onResponse(Data data) {
                        String msg = "Client BankAccount registered: ".concat(account.getAccountNumber());
                        mListener.onProgress(msg);
                        Log.i(TAG, "onResponse: ".concat(msg));
                        firebaseAPI.addClient(client, new FBApi.FBListener() {
                            @Override
                            public void onResponse(Data data) {
                                Log.w(TAG, "onResponse: client added to firebase: ".concat(client.getFullName()));
                            }

                            @Override
                            public void onError(String message) {
                                mListener.onError(message);
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
            mListener.onClientAndBeneficiariesAdded(mClient);
        }
    }

    private static void addBeneficiary(final Beneficiary beneficiary) {
        Log.d(TAG, "addBeneficiary: adding .....".concat(GSON.toJson(beneficiary)));
        chainDataAPI.addBeneficiary(beneficiary, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                final Beneficiary b = (Beneficiary) data;

                Log.d(TAG, "onResponse: beneficiary added to blockchain: ".concat(GSON.toJson(b)));
                mListener.onProgress("Beneficiary added: ".concat(b.getFullName()));
                final BankAccount account = new BankAccount();
                account.setAccountNumber(ListUtil.getRandomAccountNumber());
                account.setBalance(0.00);
                account.setBeneficiary("resource:com.oneconnect.insurenet.Beneficiary#".concat(b.getIdNumber()));
                account.setBank("resource:com.oneconnect.insurenet.Bank#".concat(mBank.getBankId()));
                chainDataAPI.registerBeneficiaryBankAccount(account, new ChainDataAPI.Listener() {
                    @Override
                    public void onResponse(Data data) {
                        String msg = "Beneficiary account added: ".concat(account.getAccountNumber());
                        mListener.onProgress(msg);
                        Log.e(TAG, "onResponse: ".concat(msg));
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
                        index++;
                        controlBeneficiaries();
                    }

                    @Override
                    public void onError(String message) {
                        mListener.onError(message);
                    }
                });

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
