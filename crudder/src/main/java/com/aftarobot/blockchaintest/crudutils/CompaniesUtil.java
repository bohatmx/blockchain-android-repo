package com.aftarobot.blockchaintest.crudutils;

import android.content.Context;
import android.util.Log;

import com.aftarobot.mlibrary.api.ChainDataAPI;
import com.aftarobot.mlibrary.data.BankAccount;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.InsuranceCompany;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CompaniesUtil {

    private static ChainDataAPI chainDataAPI;
    private static int mCount;
    private static InsuranceCompanyListener mListener;

    public interface InsuranceCompanyListener {
        void onInsuranceCompanysComplete();
        void onProgress(InsuranceCompany company);
        void onError(String message);
    }

    public static void generate(Context context, final InsuranceCompanyListener listener) {
        chainDataAPI = new ChainDataAPI(context);
        mListener = listener;
        mCount = 0;
        controlInsuranceCompanys();
    }

    private static void controlInsuranceCompanys() {
        if (mCount < 4) {
            switch (mCount) {
                case 0:
                    InsuranceCompany co = new InsuranceCompany();
                    co.setInsuranceCompanyId("COMPANY_001");
                    co.setName("OneConnect Insurance LLC");
                    co.setEmail("info@oneconnectinsurance.com");
                    co.setAddress("234 Maude Street, Sandton");
                    writeCompany(co);
                    break;
                case 1:
                    InsuranceCompany co1 = new InsuranceCompany();
                    co1.setInsuranceCompanyId("COMPANY_002");
                    co1.setName("Phila Insurance");
                    co1.setEmail("info@phila.com");
                    co1.setAddress("26 Remington Street, Cape Town");
                    writeCompany(co1);
                    break;
                case 2:
                    InsuranceCompany co3 = new InsuranceCompany();
                    co3.setInsuranceCompanyId("COMPANY_003");
                    co3.setName("Cape Insurance");
                    co3.setEmail("info@kzninsurance.com");
                    co3.setAddress("12 Beach Street, Cape Town");
                    writeCompany(co3);
                    break;
                case 3:
                    InsuranceCompany co4 = new InsuranceCompany();
                    co4.setInsuranceCompanyId("COMPANY_004");
                    co4.setName("Black Ox Insurance");
                    co4.setEmail("info@blackoxinsurance.com");
                    co4.setAddress("132 Fricker Road, Illovo, Johannesburg");
                    writeCompany(co4);
                    break;
            }

        } else {
            mListener.onInsuranceCompanysComplete();
        }


    }

    private static void writeCompany(final InsuranceCompany company) {
        chainDataAPI.addInsuranceCompany(company, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                InsuranceCompany x = (InsuranceCompany) data;
                Log.d(TAG, "onResponse, company added: ".concat(GSON.toJson(x)));
                mListener.onProgress(x);
                BankAccount ba = new BankAccount();
                ba.setBank("resource:com.oneconnect.insurenet.Bank#BANK_001");
                ba.setInsuranceCompany("resource:com.oneconnect.insurenet.InsuranceCompany#"
                        .concat(company.getInsuranceCompanyId()));
                ba.setBalance(500000000);
                ba.setAccountNumber(company.getInsuranceCompanyId());

                chainDataAPI.addBankAccount(ba, new ChainDataAPI.Listener() {
                    @Override
                    public void onResponse(Data data) {
                        Log.w(TAG, "onResponse: bank account added for: ".concat(company.getName()) );
                        mCount++;
                        controlInsuranceCompanys();
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
                mCount++;
                controlInsuranceCompanys();
            }
        });
    }


    public static final String TAG = CompaniesUtil.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

}