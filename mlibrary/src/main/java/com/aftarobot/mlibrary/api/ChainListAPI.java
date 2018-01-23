package com.aftarobot.mlibrary.api;

import android.content.Context;
import android.util.Log;

import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.DeathCertificate;
import com.aftarobot.mlibrary.data.Doctor;
import com.aftarobot.mlibrary.data.FuneralParlour;
import com.aftarobot.mlibrary.data.Hospital;
import com.aftarobot.mlibrary.data.InsuranceCompany;
import com.aftarobot.mlibrary.data.Policy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
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
        apiService = APIClient.getClient().create(ApiInterface.class);
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
    public interface ParlourListener {
        void onResponse(List<FuneralParlour> parlours);
        void onError(String message);
    }
    public interface DeathCertListener {
        void onResponse(List<DeathCertificate> certificates);
        void onError(String message);
    }
    public void getClients(final ClientListener listener) {
        Call<List<Client>> call = apiService.getClients();
        Log.w(TAG, "calling ... " + call.request().url().url().toString());
        call.enqueue(new Callback<List<Client>>() {
            @Override
            public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                if (response.isSuccessful()) {
                    List<Client> list = response.body();
                    Log.i(TAG, "getClients returns: ".concat(GSON.toJson(list)));
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
            public void onFailure(Call<List<Client>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(t.getMessage());
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
                    Log.i(TAG, "getBeneficiaries returns: ".concat(GSON.toJson(list)));
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
                listener.onError(t.getMessage());
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
                listener.onError(t.getMessage());
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
                listener.onError(t.getMessage());
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
                listener.onError(t.getMessage());
            }
        });
    }
    public void getCompanyPolicies(String insuranceCompanyId, double limitParam, double skipParam, final PolicyListener listener) {
        Call<List<Policy>> call = apiService.getCompanyPolicies(insuranceCompanyId, limitParam, skipParam);
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
                listener.onError(t.getMessage());
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
                listener.onError(t.getMessage());
            }
        });
    }
    public void getPolicies(final PolicyListener listener) {
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
                listener.onError(t.getMessage());
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
                listener.onError(t.getMessage());
            }
        });
    }
    public static final String TAG = ChainListAPI.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
}
