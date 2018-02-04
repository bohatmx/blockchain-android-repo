package com.aftarobot.mlibrary.util;

import android.content.Context;
import android.util.Log;

import com.aftarobot.mlibrary.api.ChainDataAPI;
import com.aftarobot.mlibrary.api.FBApi;
import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.InsuranceCompany;
import com.aftarobot.mlibrary.data.Policy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClientUtil {

    static ChainDataAPI chainDataAPI;
    static FBApi fbApi = new FBApi();
    static List<Client> clients = new ArrayList<>();
    static List<Beneficiary> beneficiaries = new ArrayList<>();
    static List<Policy> policies = new ArrayList<>();

    public interface ClientListener {
        void clientsComplete();
        void onError(String message);
        void onProgressMessage(String message);
    }

    private static InsuranceCompany mCompany;
    private static ClientListener mListener;

    public static void generateClients(Context context,final InsuranceCompany company, int count, final ClientListener listener) {
        mCompany = company;
        mListener = listener;
        mCount = count;
        index = 0;
        chainDataAPI = new ChainDataAPI(context);
        controlClients();
    }

    private static int index;
    private static int mCount;

    private static void controlClients() {
        if (index < mCount) {
            addRandomClient();
        } else {
            mListener.clientsComplete();
        }

    }

    private static Random random = new Random(System.currentTimeMillis());
    private static void addRandomClient() {
        Client client = ListUtil.getRandomClient();
        client.setInsuranceCompany("resource:com.oneconnect.insurenet.InsuranceCompany#".concat(mCompany.getInsuranceCompanyID()));
        chainDataAPI.addClient(client, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                final Client randomClient = (Client) data;
                clients.add(randomClient);
                Log.i(TAG, "onResponse: client added to chain: "
                        .concat(String.valueOf(clients.size())).concat(" ")
                        .concat(GSON.toJson(randomClient)));

                mListener.onProgressMessage("Client added to BlockChain: ".concat(randomClient.getFullName().concat("\n")));
                Beneficiary v = ListUtil.getRandomBeneficiary();
                v.setLastName(randomClient.getLastName());
                StringBuilder sb = new StringBuilder();
                sb.append(v.getFirstName().toLowerCase()).append(".")
                        .append(v.getLastName().toLowerCase()).append(random.nextInt(9))
                        .append("@blockchain-oct.com");
                v.setEmail(sb.toString());
                chainDataAPI.addBeneficiary(v, new ChainDataAPI.Listener() {
                    @Override
                    public void onResponse(Data data) {
                        final Beneficiary beneficiary = (Beneficiary) data;
                        beneficiaries.add(beneficiary);
                        Log.w(TAG, "onResponse: beneficiary added to chain: "
                                .concat(String.valueOf(beneficiaries.size())).concat(" ")
                                .concat(GSON.toJson(beneficiary)) );

                        mListener.onProgressMessage("Beneficiary added to BlockChain: ".concat(beneficiary.getFullName().concat("\n")));
                        fbApi.addBeneficiary(beneficiary, new FBApi.FBListener() {
                            @Override
                            public void onResponse(Data data) {
                                Log.i(TAG, "onResponse: beneficiary added to Firebase: ".concat(beneficiary.getFullName()));
                                addPolicy(randomClient, beneficiary);
                            }

                            @Override
                            public void onError(String message) {
                                mListener.onError(message);
                                index++;
                                controlClients();
                            }
                        });

                    }

                    @Override
                    public void onError(String message) {
                        mListener.onError(message);
                        index++;
                        controlClients();
                    }
                });


            }

            @Override
            public void onError(String message) {
                mListener.onError(message);
                index++;
                controlClients();

            }
        });
    }

    private static void addPolicy(final Client client, Beneficiary beneficiary) {
        final Policy policy = new Policy();
        policy.setInsuranceCompany("resource:com.oneconnect.insurenet.InsuranceCompany#".concat(mCompany.getInsuranceCompanyID()));
        policy.setPolicyNumber(ListUtil.getRandomPolicyNumber());
        policy.setClient("resource:com.oneconnect.insurenet.Client#".concat(client.getIdNumber()));
        policy.setDescription(ListUtil.getRandomDescription());
        policy.setAmount(ListUtil.getRandomPolicyAmount());

        List<String> list = new ArrayList<>(1);
        list.add("resource:com.oneconnect.insurenet.Beneficiary#".concat(beneficiary.getIdNumber()));
        policy.setBeneficiaries(list);

        chainDataAPI.addPolicy(policy, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                Policy x = (Policy)data;
                policies.add(x);
                //todo - add this policy to client via PUT call
                Log.e(TAG, "onResponse: policy added to chain: "
                        .concat(String.valueOf(policies.size())).concat(" ")
                        .concat(GSON.toJson(x)));
                mListener.onProgressMessage("Policy added to BlockChain: ".concat(x.getPolicyNumber()));
                client.setPolicies(new ArrayList<String>());
                String ply = "resource:com.oneconnect.insurenet.Policy#"
                        .concat(x.getPolicyNumber());
                client.getPolicies().add(ply);


                Log.d(TAG, "about to update client policy: ".concat(GSON.toJson(client)));
                chainDataAPI.updateClientPolicies(client, new ChainDataAPI.Listener() {
                    @Override
                    public void onResponse(Data data) {
                        Log.w(TAG, "updateClientPolicies onResponse: index: " + index );
                        index++;
                        controlClients();
                    }

                    @Override
                    public void onError(String message) {
                        mListener.onError(message);
                        index++;
                        controlClients();
                    }
                });


            }

            @Override
            public void onError(String message) {
                mListener.onError(message);
                index++;
                controlClients();

            }
        });
    }
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final String TAG = ClientUtil.class.getSimpleName();

}
