package com.aftarobot.mlibrary.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.aftarobot.mlibrary.data.Bank;
import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.DeathCertificate;
import com.aftarobot.mlibrary.data.DeathCertificateRequest;
import com.aftarobot.mlibrary.data.Doctor;
import com.aftarobot.mlibrary.data.FundsTransfer;
import com.aftarobot.mlibrary.data.FundsTransferRequest;
import com.aftarobot.mlibrary.data.FuneralParlour;
import com.aftarobot.mlibrary.data.Hospital;
import com.aftarobot.mlibrary.data.InsuranceCompany;
import com.aftarobot.mlibrary.data.Policy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aubreymalabie on 1/13/18.
 */

public class ChainListAPI {
    private ApiInterface apiService;
    private Context context;

    public ChainListAPI(Context ctx) {
        context = ctx;
        apiService = APIClient.getClient(ctx).create(ApiInterface.class);
    }
    public interface CompanyListener {
        void onResponse(List<InsuranceCompany> companies);
        void onError(String message);
    }
    public interface HospitalListener {
        void onResponse(List<Hospital> hospitals);
        void onError(String message);
    }
    public interface ClientListener {
        void onResponse(List<Client> clients);
        void onError(String message);
    }
    public interface BeneficiaryListener {
        void onResponse(List<Beneficiary> beneficiaries);
        void onError(String message);
    }
    public interface DoctorListener {
        void onResponse(List<Doctor> beneficiaries);
        void onError(String message);
    }
    public interface PolicyListener {
        void onResponse(List<Policy> policies);
        void onError(String message);
    }
    public interface ClaimsListener {
        void onResponse(List<Claim> claims);
        void onError(String message);
    }
    public interface ParlourListener {
        void onResponse(List<FuneralParlour> parlours);
        void onError(String message);
    }
    public interface DeathCertListener {
        void onResponse(List<DeathCertificate> certificates);
        void onError(String message);
    }
    public interface DeathCertRequestListener {
        void onResponse(List<DeathCertificateRequest> requests);
        void onError(String message);
    }
    public interface FundsTransferRequestListener {
        void onResponse(List<FundsTransferRequest> requests);
        void onError(String message);
    }
    public interface FundsTransferListener {
        void onResponse(List<FundsTransfer> requests);
        void onError(String message);
    }
    public interface BankListener {
        void onResponse(List<Bank> banks);
        void onError(String message);
    }

    public void getFundsTransferRequests(String id, final FundsTransferRequestListener listener) {
        Call<List<FundsTransferRequest>> call = apiService.getFundsTransferRequests(id);
        Log.w(TAG, "calling ... " + call.request().url().url().toString());
        call.enqueue(new Callback<List<FundsTransferRequest>>() {
            @Override
            public void onResponse(Call<List<FundsTransferRequest>> call, Response<List<FundsTransferRequest>> response) {
                if (response.isSuccessful()) {
                    List<FundsTransferRequest> list = response.body();
                    listener.onResponse(list);
                } else {
                    Log.w(TAG, "onResponse: ftr's not found ");
                    listener.onResponse(new ArrayList<FundsTransferRequest>());
                }
            }

            @Override
            public void onFailure(Call<List<FundsTransferRequest>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(NETWORK_ERROR);
            }
        });
    }
    public void getFundsTransfers(String id, final FundsTransferListener listener) {
        Call<List<FundsTransfer>> call = apiService.getFundsTransfers(id);
        Log.w(TAG, "calling ... " + call.request().url().url().toString());
        call.enqueue(new Callback<List<FundsTransfer>>() {
            @Override
            public void onResponse(Call<List<FundsTransfer>> call, Response<List<FundsTransfer>> response) {
                if (response.isSuccessful()) {
                    List<FundsTransfer> list = response.body();
                    listener.onResponse(list);
                } else {
                    Log.w(TAG, "onResponse: ftr's not found ");
                    listener.onResponse(new ArrayList<FundsTransfer>());
                }
            }

            @Override
            public void onFailure(Call<List<FundsTransfer>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(NETWORK_ERROR);
            }
        });
    }


    public void getBeneficiary(String id, final BeneficiaryListener listener) {
        Call<Beneficiary> call = apiService.getBeneficiary(id);
        Log.w(TAG, "calling ... " + call.request().url().url().toString());
        call.enqueue(new Callback<Beneficiary>() {
            @Override
            public void onResponse(Call<Beneficiary> call, Response<Beneficiary> response) {
                if (response.isSuccessful()) {
                    Beneficiary client = response.body();
                    List<Beneficiary> list = new ArrayList<>(1);
                    list.add(client);
                    listener.onResponse(list);
                } else {
                    Log.w(TAG, "onResponse: client not found ");
                    listener.onResponse(new ArrayList<Beneficiary>());
                }
            }

            @Override
            public void onFailure(Call<Beneficiary> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(NETWORK_ERROR);;
            }
        });
    }

    public void getClient(String id, final ClientListener listener) {
        Call<Client> call = apiService.getClient(id);
        Log.w(TAG, "calling ... " + call.request().url().url().toString());
        call.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                if (response.isSuccessful()) {
                    Client client = response.body();
                    List<Client> list = new ArrayList<>(1);
                    list.add(client);
                    listener.onResponse(list);
                } else {
                    Log.w(TAG, "onResponse: client not found ");
                    listener.onResponse(new ArrayList<Client>());
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(NETWORK_ERROR);;
            }
        });
    }

    public void getPolicy(String id, final PolicyListener listener) {
        Call<Policy> call = apiService.getPolicy(id);
        Log.w(TAG, "calling ... " + call.request().url().url().toString());
        call.enqueue(new Callback<Policy>() {
            @Override
            public void onResponse(Call<Policy> call, Response<Policy> response) {
                if (response.isSuccessful()) {
                    Policy policy = response.body();
                    List<Policy> list = new ArrayList<>(1);
                    list.add(policy);
                    listener.onResponse(list);
                } else {
                    Log.w(TAG, "onResponse: client not found ");
                    listener.onResponse(new ArrayList<Policy>());
                }
            }

            @Override
            public void onFailure(Call<Policy> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(NETWORK_ERROR);
            }
        });
    }
    public void getInsuranceCompany(String id, final CompanyListener listener) {
        Call<InsuranceCompany> call = apiService.getInsuranceCompany(id);
        Log.w(TAG, "calling ... " + call.request().url().url().toString());
        call.enqueue(new Callback<InsuranceCompany>() {
            @Override
            public void onResponse(Call<InsuranceCompany> call, Response<InsuranceCompany> response) {
                if (response.isSuccessful()) {
                    InsuranceCompany company = response.body();
                    List<InsuranceCompany> list = new ArrayList<>(1);
                    list.add(company);
                    listener.onResponse(list);
                } else {
                    Log.w(TAG, "onResponse: client not found ");
                    listener.onResponse(new ArrayList<InsuranceCompany>());
                }
            }

            @Override
            public void onFailure(Call<InsuranceCompany> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(NETWORK_ERROR);
            }
        });
    }
    public void getClaims(final ClaimsListener listener) {
        Call<List<Claim>> call = apiService.getClaims();
        Log.w(TAG, "calling ... " + call.request().url().url().toString());
        call.enqueue(new Callback<List<Claim>>() {
            @Override
            public void onResponse(Call<List<Claim>> call, Response<List<Claim>> response) {
                if (response.isSuccessful()) {
                    List<Claim> list = response.body();
                    Log.i(TAG, "getClaims returns: ".concat(String.valueOf(list.size())));
                    listener.onResponse(list);

                } else {
                    try {
                        Log.e(TAG, "onResponse: things are fucked up!: ".concat(response.message())
                                .concat(" code: ".concat(String.valueOf(response.code())).concat(" body: ")
                                        .concat(response.errorBody().string())));
                        listener.onError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Claim>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(NETWORK_ERROR);
            }
        });
    }
    public void getClients(final ClientListener listener) {
        Call<List<Client>> call = apiService.getClients();
        Log.w(TAG, "calling ... " + call.request().url().url().toString());
        call.enqueue(new Callback<List<Client>>() {
            @Override
            public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                if (response.isSuccessful()) {
                    List<Client> list = response.body();
                    Log.i(TAG, "getClients returns: ".concat(String.valueOf(list.size())));
                    listener.onResponse(list);

                } else {
                    try {
                        Log.e(TAG, "onResponse: things are fucked up!: ".concat(response.message())
                                .concat(" code: ".concat(String.valueOf(response.code())).concat(" body: ")
                                        .concat(response.errorBody().string())));
                        listener.onError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Client>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(NETWORK_ERROR);
            }
        });
    }
    public void getBeneficiaries(final BeneficiaryListener listener) {
        Call<List<Beneficiary>> call = apiService.getBeneficiaries();
        Log.w(TAG, "calling ... " + call.request().url().url().toString());
        call.enqueue(new Callback<List<Beneficiary>>() {
            @Override
            public void onResponse(Call<List<Beneficiary>> call, Response<List<Beneficiary>> response) {
                if (response.isSuccessful()) {
                    List<Beneficiary> list = response.body();
                    Log.i(TAG, "getBeneficiaries returns: ".concat(String.valueOf(list.size())));
                    listener.onResponse(response.body());

                } else {
                    try {
                        Log.e(TAG, "onResponse: things are fucked up!: ".concat(response.message())
                                .concat(" code: ".concat(String.valueOf(response.code())).concat(" body: ")
                                        .concat(response.errorBody().string())));
                        listener.onError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Beneficiary>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(NETWORK_ERROR);
            }
        });
    }

    public void getFuneralParlours(final ParlourListener listener) {
        Call<List<FuneralParlour>> call = apiService.getFuneralParlours();
        Log.w(TAG, "calling ... " + call.request().url().url().toString());
        call.enqueue(new Callback<List<FuneralParlour>>() {
            @Override
            public void onResponse(Call<List<FuneralParlour>> call, Response<List<FuneralParlour>> response) {
                if (response.isSuccessful()) {
                    List<FuneralParlour> list = response.body();
                    Log.i(TAG, "getFuneralParlours returns: ".concat(GSON.toJson(list)));
                    listener.onResponse(response.body());

                } else {
                    try {
                        Log.e(TAG, "onResponse: things are fucked up!: ".concat(response.message())
                                .concat(" code: ".concat(String.valueOf(response.code())).concat(" body: ")
                                        .concat(response.errorBody().string())));
                        listener.onError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<FuneralParlour>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(NETWORK_ERROR);
            }
        });
    }
    public void getHospitals(final HospitalListener listener) {
        Call<List<Hospital>> call = apiService.getHospital();
        Log.w(TAG, "calling ... " + call.request().url().url().toString());
        call.enqueue(new Callback<List<Hospital>>() {
            @Override
            public void onResponse(Call<List<Hospital>> call, Response<List<Hospital>> response) {
                if (response.isSuccessful()) {
                    List<Hospital> list = response.body();
                    Log.i(TAG, "getHospitals returns: ".concat(GSON.toJson(list)));
                    listener.onResponse(response.body());

                } else {
                    try {
                        Log.e(TAG, "onResponse: things are fucked up!: ".concat(response.message())
                                .concat(" code: ".concat(String.valueOf(response.code())).concat(" body: ")
                                        .concat(response.errorBody().string())));
                        listener.onError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Hospital>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(NETWORK_ERROR);
            }
        });
    }

    public void getBanks(final BankListener listener) {
        Call<List<Bank>> call = apiService.getBanks();
        Log.w(TAG, "calling ... " + call.request().url().url().toString());
        call.enqueue(new Callback<List<Bank>>() {
            @Override
            public void onResponse(Call<List<Bank>> call, Response<List<Bank>> response) {
                if (response.isSuccessful()) {
                    List<Bank> list = response.body();
                    Log.i(TAG, "getBanks returns: ".concat(GSON.toJson(list)));
                    listener.onResponse(response.body());

                } else {
                    try {
                        Log.e(TAG, "onResponse: things are fucked up!: ".concat(response.message())
                                .concat(" code: ".concat(String.valueOf(response.code())).concat(" body: ")
                                        .concat(response.errorBody().string())));
                        listener.onError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Bank>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(NETWORK_ERROR);
            }
        });
    }

    public void getDoctors(final DoctorListener listener) {
        Call<List<Doctor>> call = apiService.getDoctors();
        Log.w(TAG, "calling ... " + call.request().url().url().toString());
        call.enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.isSuccessful()) {
                    List<Doctor> list = response.body();
                    Log.i(TAG, "getDoctors returns: ".concat(GSON.toJson(list)));
                    listener.onResponse(response.body());

                } else {
                    try {
                        Log.e(TAG, "onResponse: things are fucked up!: ".concat(response.message())
                                .concat(" code: ".concat(String.valueOf(response.code())).concat(" body: ")
                                        .concat(response.errorBody().string())));
                        listener.onError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(NETWORK_ERROR);
            }
        });
    }
    public void getCompanyClients(String insuranceCompany, final ClientListener listener) {
        Call<List<Client>> call = apiService.getCompanyClients(insuranceCompany);
        Log.w(TAG, "######### getCompanyClaims calling ... " + call.request().url().url().toString());
        call.enqueue(new Callback<List<Client>>() {
            @Override
            public void onResponse(@NonNull Call<List<Client>> call, @NonNull Response<List<Client>> response) {
                if (response.isSuccessful()) {
                    List<Client> list = response.body();
                    Log.i(TAG, "getCompanyClients returns: ".concat(String.valueOf(list.size())));
                    listener.onResponse(list);

                } else {
                    try {
                        Log.e(TAG, "onResponse: things are fucked up!: ".concat(response.message())
                                .concat(" code: ".concat(String.valueOf(response.code())).concat(" body: ")
                                        .concat(response.errorBody().string())));
                        listener.onError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Client>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(NETWORK_ERROR);
            }
        });
    }
    public void getCompanyClaims(String insuranceCompanyId, final ClaimsListener listener) {
        Call<List<Claim>> call = apiService.getCompanyClaims(insuranceCompanyId);
        Log.w(TAG, "######### getCompanyClaims calling ... " + call.request().url().url().toString());
        call.enqueue(new Callback<List<Claim>>() {
            @Override
            public void onResponse(@NonNull Call<List<Claim>> call, @NonNull Response<List<Claim>> response) {
                if (response.isSuccessful()) {
                    List<Claim> list = response.body();
                    Log.i(TAG, "getCompanyClaims returns: ".concat(String.valueOf(list.size())));
                    listener.onResponse(list);

                } else {
                    try {
                        Log.e(TAG, "onResponse: things are fucked up!: ".concat(response.message())
                                .concat(" code: ".concat(String.valueOf(response.code())).concat(" body: ")
                                        .concat(response.errorBody().string())));
                        listener.onError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Claim>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(NETWORK_ERROR);
            }
        });
    }

    public void getCompanyPolicies(String insuranceCompanyId, final PolicyListener listener) {
        Call<List<Policy>> call = apiService.getCompanyPolicies(insuranceCompanyId);
        Log.w(TAG, "######### getCompanyPolicies calling ... " + call.request().url().url().toString());
        call.enqueue(new Callback<List<Policy>>() {
            @Override
            public void onResponse(@NonNull Call<List<Policy>> call, @NonNull Response<List<Policy>> response) {
                if (response.isSuccessful()) {
                    List<Policy> list = response.body();
                    Log.i(TAG, "getCompanyPolicies returns: ".concat(String.valueOf(list.size())));
                    listener.onResponse(list);

                } else {
                    try {
                        Log.e(TAG, "onResponse: things are fucked up!: ".concat(response.message())
                                .concat(" code: ".concat(String.valueOf(response.code())).concat(" body: ")
                                        .concat(response.errorBody().string())));
                        listener.onError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Policy>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(NETWORK_ERROR);
            }
        });
    }
    public void getInsuranceCompanies(final CompanyListener listener) {
        Call<List<InsuranceCompany>> call = apiService.getInsuranceCompanies();
        Log.w(TAG, "calling ... " + call.request().url().url().toString());
        call.enqueue(new Callback<List<InsuranceCompany>>() {
            @Override
            public void onResponse(Call<List<InsuranceCompany>> call, Response<List<InsuranceCompany>> response) {
                if (response.isSuccessful()) {
                    List<InsuranceCompany> list = response.body();
                    Log.i(TAG, "getInsuranceCompanies returns: ".concat(GSON.toJson(list)));
                    listener.onResponse(response.body());

                } else {
                    try {
                        Log.e(TAG, "onResponse: things are fucked up!: ".concat(response.message())
                                .concat(" code: ".concat(String.valueOf(response.code())).concat(" body: ")
                                        .concat(response.errorBody().string())));
                        listener.onError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<InsuranceCompany>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(NETWORK_ERROR);
            }
        });
    }
    public void getPolicies(final PolicyListener listener) {
        Call<List<Policy>> call = apiService.getPolicies();
        Log.e(TAG, "######### getPolicies calling ... " + call.request().url().url().toString());
        call.enqueue(new Callback<List<Policy>>() {
            @Override
            public void onResponse(Call<List<Policy>> call, Response<List<Policy>> response) {
                if (response.isSuccessful()) {
                    List<Policy> list = response.body();
                    Log.i(TAG, "getPolicies returns: ".concat(GSON.toJson(list)));
                    listener.onResponse(response.body());

                } else {
                    try {
                        Log.e(TAG, "onResponse: things are fucked up!: ".concat(response.message())
                                .concat(" code: ".concat(String.valueOf(response.code())).concat(" body: ")
                                        .concat(response.errorBody().string())));
                        listener.onError("Failed to get Policies");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Policy>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(NETWORK_ERROR);
            }
        });
    }

    public void getPoliciesByClientId(String idNumber, final PolicyListener listener) {
        Call<List<Policy>> call = apiService.getPolicies();
        Log.w(TAG, "calling ... " + call.request().url().url().toString());
        call.enqueue(new Callback<List<Policy>>() {
            @Override
            public void onResponse(Call<List<Policy>> call, Response<List<Policy>> response) {
                if (response.isSuccessful()) {
                    List<Policy> list = response.body();
                    Log.i(TAG, "getPolicies returns: ".concat(GSON.toJson(list)));
                    listener.onResponse(response.body());

                } else {
                    try {
                        Log.e(TAG, "onResponse: things are fucked up!: ".concat(response.message())
                                .concat(" code: ".concat(String.valueOf(response.code())).concat(" body: ")
                                        .concat(response.errorBody().string())));
                        listener.onError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Policy>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(NETWORK_ERROR);
            }
        });
    }

    public void getDeathCertificates(final DeathCertListener listener) {
        Call<List<DeathCertificate>> call = apiService.getDeathCertificates();
        Log.w(TAG, "calling ... " + call.request().url().url().toString());
        call.enqueue(new Callback<List<DeathCertificate>>() {
            @Override
            public void onResponse(Call<List<DeathCertificate>> call, Response<List<DeathCertificate>> response) {
                if (response.isSuccessful()) {
                    List<DeathCertificate> list = response.body();
                    Log.i(TAG, "getDeathCertificates returns: ".concat(GSON.toJson(list)));
                    listener.onResponse(response.body());

                } else {
                    try {
                        Log.e(TAG, "onResponse: things are fucked up!: ".concat(response.message())
                                .concat(" code: ".concat(String.valueOf(response.code())).concat(" body: ")
                                        .concat(response.errorBody().string())));
                        listener.onError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<DeathCertificate>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(NETWORK_ERROR);
            }
        });
    }

    public void getDeathCertificateRequests(final DeathCertRequestListener listener) {
        Call<List<DeathCertificateRequest>> call = apiService.getDeathCertificateRequests();
        Log.w(TAG, "calling ... " + call.request().url().url().toString());
        call.enqueue(new Callback<List<DeathCertificateRequest>>() {
            @Override
            public void onResponse(Call<List<DeathCertificateRequest>> call, Response<List<DeathCertificateRequest>> response) {
                if (response.isSuccessful()) {
                    List<DeathCertificateRequest> list = response.body();
                    Log.i(TAG, "getDeathCertificateRequests returns: ".concat(GSON.toJson(list)));
                    listener.onResponse(response.body());

                } else {
                    try {
                        Log.e(TAG, "onResponse: things are fucked up!: ".concat(response.message())
                                .concat(" code: ".concat(String.valueOf(response.code())).concat(" body: ")
                                        .concat(response.errorBody().string())));
                        listener.onError(response.errorBody().string());
                    } catch (IOException e) {
                        Log.e(TAG, "onResponse: ",e );
                    }
                }
            }

            @Override
            public void onFailure(Call<List<DeathCertificateRequest>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(NETWORK_ERROR);
            }
        });
    }

    public static final String
            TAG = ChainListAPI.class.getSimpleName(),
            NETWORK_ERROR = "Possible network related error";
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
}
