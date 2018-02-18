package com.aftarobot.insurancecompany.services;

import android.content.Context;
import android.util.Log;

import com.aftarobot.mlibrary.api.ChainListAPI;
import com.aftarobot.mlibrary.api.FBApi;
import com.aftarobot.mlibrary.api.FBListApi;
import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.BeneficiaryClaimMessage;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.Policy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class NotifyBeneficiaryClaim {
    public static final String TAG = NotifyBeneficiaryClaim.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    static Policy policy;
    static NotifyListener mListener;
    static Claim claim;
    static FBApi fbApi;
    static FBListApi fbListApi;
    static ChainListAPI chainListAPI;

    public interface NotifyListener {
        void onNotified();

        void onProgress(String message);

        void onError(String message);
    }

    static int index;

    public static void notifyClaimBeneficiary(Context context, final Claim c, final NotifyListener listener) {
        claim = c;
        mListener = listener;
        fbListApi = new FBListApi();
        fbApi = new FBApi();
        chainListAPI = new ChainListAPI(context);
        chainListAPI.getClaim(c.getClaimId(), new ChainListAPI.ClaimsListener() {
            @Override
            public void onResponse(List<Claim> claims) {
                Log.e(TAG, "getClaim onResponse: ####################### claimId ".concat(claim.getClaimId()));
                if (!claims.isEmpty()) {
                    Claim c = claims.get(0);
                    Log.d(TAG, "onResponse: found claim: ".concat(GSON.toJson(c)));
                    final String policyNumber = c.getPolicyNumber();
                    listener.onProgress("Finding policy: ".concat(policyNumber));
                    chainListAPI.getPolicy(policyNumber, new ChainListAPI.PolicyListener() {
                        @Override
                        public void onResponse(List<Policy> policies) {
                            if (!policies.isEmpty()) {
                                policy = policies.get(0);
                                if (!policy.getBeneficiaries().isEmpty()) {
                                    listener.onProgress("Sending funds message to "
                                            .concat(String.valueOf(policy.getBeneficiaries().size())));
                                    index = 0;
                                    controlClaimBennies();
                                }
                            }
                        }

                        @Override
                        public void onError(String message) {
                            listener.onError(message);
                        }
                    });
                } else {
                    listener.onError("### Claim not found: ".concat(claim.getClaimId()));
                }
            }

            @Override
            public void onError(String message) {
                listener.onError(message);
            }
        });
    }

    private static void controlClaimBennies() {
        Log.e(TAG, "controlClaimBennies: ##########################");
        if (index < policy.getBeneficiaries().size()) {
            final String idNumber = policy.getBeneficiaries().get(index).split("#")[1];
            sendBeneficiaryClaimMessage(idNumber);
        } else {
            Log.i(TAG, "controlClaimBennies: all messages sent OK");
            mListener.onNotified();
        }
    }

    private static void sendBeneficiaryClaimMessage(String idNumber) {
        Log.w(TAG, "sendBeneficiaryClaimMessage: #####################");
        fbListApi.getBeneficiaryByIDnumber(idNumber,
                new FBListApi.BeneficiaryListener() {
                    @Override
                    public void onResponse(List<Beneficiary> beneficiaries) {

                        if (!beneficiaries.isEmpty()) {
                            for (final Beneficiary ben : beneficiaries) {
                                BeneficiaryClaimMessage funds = new BeneficiaryClaimMessage();
                                funds.setFcmToken(ben.getFcmToken());
                                funds.setClaim(claim);
                                fbApi.addBeneficiaryClaimMessage(funds, new FBApi.FBListener() {
                                    @Override
                                    public void onResponse(Data data) {
                                        mListener.onProgress("Claim message sent to: "
                                                .concat(ben.getFullName()));
                                        Log.i(TAG, "addBeneficiaryClaimMessage onResponse: funds added to Firebase");
                                        index++;
                                        controlClaimBennies();
                                    }

                                    @Override
                                    public void onError(String message) {
                                        mListener.onError(message);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onError(String message) {
                        mListener.onError(message);
                    }
                });
    }
}
