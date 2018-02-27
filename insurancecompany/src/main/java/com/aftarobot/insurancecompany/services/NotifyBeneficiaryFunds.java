package com.aftarobot.insurancecompany.services;

import android.content.Context;
import android.util.Log;

import com.aftarobot.mlibrary.api.ChainListAPI;
import com.aftarobot.mlibrary.api.FBApi;
import com.aftarobot.mlibrary.api.FBListApi;
import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.BeneficiaryClaimMessage;
import com.aftarobot.mlibrary.data.BeneficiaryFunds;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.FundsTransfer;
import com.aftarobot.mlibrary.data.Policy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.List;

public class NotifyBeneficiaryFunds {
    public static final String TAG = NotifyBeneficiaryFunds.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Policy policy;
    private static NotifyListener mListener;
    private static FBApi fbApi;
    private static FBListApi fbListApi;
    private static ChainListAPI chainListAPI;
    private static FundsTransfer fundsTransfer;

    public interface NotifyListener {
        void onNotified();

        void onProgress(String message);

        void onError(String message);
    }

    private static int index;

    public static void notify(Context context, final FundsTransfer transfer, final NotifyListener listener) {
        fundsTransfer = transfer;
        mListener = listener;
        chainListAPI = new ChainListAPI(context);
        fbApi = new FBApi();
        final String claimId = transfer.getClaim().split("#")[1];
        listener.onProgress("Finding claim: ".concat(claimId));
        fbListApi = new FBListApi();
        fbApi = new FBApi();
        chainListAPI.getClaim(claimId, new ChainListAPI.ClaimsListener() {
            @Override
            public void onResponse(List<Claim> claims) {
                Log.e(TAG, "getClaim onResponse: ####################### type ".concat(claimId));
                if (!claims.isEmpty()) {
                    Claim c = claims.get(0);
                    Log.d(TAG, "onResponse: %%%%%%%%% found claim: ".concat(GSON.toJson(c)));
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
                                    controlBennies();
                                }
                            }
                        }

                        @Override
                        public void onError(String message) {
                            listener.onError(message);
                        }
                    });
                } else {
                    listener.onError("### Claim not found: ".concat(claimId));
                }
            }

            @Override
            public void onError(String message) {
                listener.onError(message);
            }
        });
    }

    private static void controlBennies() {
        if (index < policy.getBeneficiaries().size()) {
            final String idNumber = policy.getBeneficiaries().get(index).split("#")[1];
            sendBeneficiaryMessage(idNumber);
        } else {
            Log.i(TAG, "controlBennies: all messages sent OK");
            mListener.onNotified();
        }
    }

    private static void sendBeneficiaryMessage(String idNumber) {
        fbListApi.getBeneficiaryByIDnumber(idNumber,
                new FBListApi.BeneficiaryListener() {
                    @Override
                    public void onResponse(List<Beneficiary> beneficiaries) {

                        if (!beneficiaries.isEmpty()) {
                            for (final Beneficiary ben : beneficiaries) {
                                BeneficiaryFunds funds = new BeneficiaryFunds();
                                funds.setIdNumber(ben.getIdNumber());
                                funds.setFcmToken(ben.getFcmToken());
                                funds.setDate(new Date().getTime());
                                funds.setFundsTransfer(fundsTransfer);
                                fbApi.addBeneficiaryFunds(funds, new FBApi.FBListener() {
                                    @Override
                                    public void onResponse(Data data) {
                                        mListener.onProgress("Funds message sent to: "
                                                .concat(ben.getFullName()));
                                        Log.i(TAG, "addBeneficiaryFunds onResponse: funds added to Firebase");
                                        index++;
                                        controlBennies();
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
