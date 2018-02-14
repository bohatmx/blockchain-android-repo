package com.aftarobot.blockchaintest.crudutils;

import android.content.Context;
import android.util.Log;

import com.aftarobot.mlibrary.api.ChainDataAPI;
import com.aftarobot.mlibrary.api.ChainListAPI;
import com.aftarobot.mlibrary.data.Bank;
import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.InsuranceCompany;
import com.aftarobot.mlibrary.data.Policy;
import com.aftarobot.mlibrary.util.ListUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PolicyUtil {

    private static ChainDataAPI chainDataAPI;
    private static ChainListAPI chainListAPI;
    private static List<Policy> policies = new ArrayList<>();
    private static int pIndex;

    public interface PolicyUtilListener {
        void clientsComplete();

        void onError(String message);

        void onProgressMessage(String message);
    }

    private static PolicyUtilListener mListener;
    private static List<InsuranceCompany> companyList;
    private static Context mContext;
    private static Bank mBank;


    public static void generateClientsAndPolicies(Context context, int count, Bank bank,
                                                  final List<InsuranceCompany> companies,
                                                  final PolicyUtilListener listener) {
        companyList = companies;
        mListener = listener;
        mCount = count;
        mBank = bank;
        mContext = context;
        index = 0;
        chainDataAPI = new ChainDataAPI(context);
        Log.d(TAG, "generateClientsAndPolicies: companies: ".concat(GSON.toJson(companies)));
        controlGeneration();
    }

    /**
     * Generate clients and add multiple policies with multiple beneficiaries
     */
    private static void controlGeneration() {
        if (index < mCount) {
            addRandomClientWithPolicies();
        } else {
            mListener.clientsComplete();
        }

    }

    private static void addRandomClientWithPolicies() {
        Log.e(TAG, "addRandomClientWithPolicies: ##########################################" );
        final Client client = ListUtil.getRandomClient();

        final List<Beneficiary> beneficiaries = new ArrayList<>();
        int count = random.nextInt(4);
        if (count == 0) {
            count = 2;
        }
        for (int j = 0; j < count; j++) {
            Beneficiary b = ListUtil.getRandomBeneficiary();
            b.setLastName(client.getLastName());
            StringBuilder sb = new StringBuilder();
            sb.append(b.getFirstName().toLowerCase()).append(".");
            sb.append(b.getLastName()).append(String.valueOf(random.nextInt(50)));
            sb.append("@companyemail.com");
            b.setEmail(sb.toString());
            beneficiaries.add(b);
        }
        ClientBeneficiariesUtil.addClientAndBeneficiaries(mContext,
                client, mBank, beneficiaries, new ClientBeneficiariesUtil.ClientBennieListener() {
            @Override
            public void onClientAndBeneficiariesAdded() {
                createPolicies(client, beneficiaries);
                pIndex = 0;
                index++;
                controlPolicies();
            }

            @Override
            public void onError(String message) {
                mListener.onError(message);
                index++;
                controlGeneration();
            }

            @Override
            public void onProgress(String message) {
                mListener.onProgressMessage(message);
            }
        });

    }


    private static List<Policy> clientPolicies;

    private static void createPolicies(final Client client, final List<Beneficiary> beneficiaries) {
        clientPolicies = new ArrayList<>();
        buildPolicy(client,beneficiaries,companyList.get(0));
        for (InsuranceCompany c : companyList) {
            int doIt  = random.nextInt(100);
            if (doIt > 60) {
                buildPolicy(client, beneficiaries, c);
            }
            if (doIt > 90) {
                buildPolicy(client, beneficiaries, c);
            }
        }
        Log.d(TAG, "&&&&&&&&&&&&& created list of Policies for: ".concat(client.getFullName()
                .concat(" ")).concat(String.valueOf(clientPolicies.size())));


    }

    private static void buildPolicy(Client client, List<Beneficiary> beneficiaries, InsuranceCompany c) {
        final Policy policy = new Policy();
        policy.setInsuranceCompany("resource:com.oneconnect.insurenet.InsuranceCompany#"
                .concat(c.getInsuranceCompanyId()));
        policy.setInsuranceCompanyId(c.getInsuranceCompanyId());
        policy.setPolicyNumber(ListUtil.getRandomPolicyNumber());
        policy.setClient("resource:com.oneconnect.insurenet.Client#".concat(client.getIdNumber()));
        policy.setDescription(ListUtil.getRandomDescription());
        policy.setAmount(ListUtil.getRandomPolicyAmount());
        policy.setIdNumber(client.getIdNumber());
        policy.setClaimSubmitted(false);

        List<String> list = new ArrayList<>(beneficiaries.size());
        for (Beneficiary b : beneficiaries) {
            list.add("resource:com.oneconnect.insurenet.Beneficiary#".concat(b.getIdNumber()));
        }
        policy.setBeneficiaries(list);
        clientPolicies.add(policy);
    }

    private static void controlPolicies() {
        if (pIndex < clientPolicies.size()) {
            registerOnePolicy(clientPolicies.get(pIndex));
        } else {
            //done for one client
            controlGeneration();
        }
    }

    private static void registerOnePolicy(Policy policy) {

        chainDataAPI.registerPolicyViaTransaction(policy, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                Policy x = (Policy) data;
                policies.add(x);
                Log.e(TAG, "registerOnePolicy onResponse: policy added to chain, policies: "
                        .concat(String.valueOf(policies.size())).concat(" ")
                        .concat(GSON.toJson(x)));
                mListener.onProgressMessage("Policy added to BlockChain: ".concat(x.getPolicyNumber()));
                pIndex++;
                controlPolicies();

            }

            @Override
            public void onError(String message) {
                mListener.onError(message);
                pIndex++;
                controlPolicies();

            }
        });
    }

    private static int index;
    private static int mCount;
    private static Random random = new Random(System.currentTimeMillis());
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final String TAG = PolicyUtil.class.getSimpleName();

}
