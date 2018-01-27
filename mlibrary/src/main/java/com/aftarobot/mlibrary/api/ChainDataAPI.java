package com.aftarobot.mlibrary.api;

import android.content.Context;
import android.util.Log;

import com.aftarobot.mlibrary.R;
import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.Burial;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.DeathCertificate;
import com.aftarobot.mlibrary.data.Doctor;
import com.aftarobot.mlibrary.data.FuneralParlour;
import com.aftarobot.mlibrary.data.HomeAffairs;
import com.aftarobot.mlibrary.data.Hospital;
import com.aftarobot.mlibrary.data.InsuranceCompany;
import com.aftarobot.mlibrary.data.Policy;
import com.aftarobot.mlibrary.data.Regulator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aubreymalabie on 1/13/18.
 */

public class ChainDataAPI {
    private ApiInterface apiService;
    private Context context;

    public ChainDataAPI(Context ctx) {
        context = ctx;
        apiService = APIClient.getClient().create(ApiInterface.class);
    }
    public interface Listener {
        void onResponse(Data data);
        void onError(String message);
    }
    public void addHomeAffairs(final HomeAffairs homeAffairs, final Listener listener) {
        Call<HomeAffairs> call = apiService.registerHomeAffairs(homeAffairs);
        call.enqueue(new Callback<HomeAffairs>() {
            @Override
            public void onResponse(Call<HomeAffairs> call, Response<HomeAffairs> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "HomeAffairs added: ".concat(homeAffairs.getName()));
                    listener.onResponse(response.body());
                } else {
                    Log.e(TAG, "onResponse: ".concat(GSON.toJson(response.errorBody())) );
                    listener.onError(context.getString(R.string.doc_add_failed));
                }
            }

            @Override
            public void onFailure(Call<HomeAffairs> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(context.getString(R.string.doc_add_failed));

            }
        });
    }
    public void addDoctor(final Doctor doctor, final Listener listener) {
        Call<Doctor> call = apiService.registerDoctor(doctor);
        call.enqueue(new Callback<Doctor>() {
            @Override
            public void onResponse(Call<Doctor> call, Response<Doctor> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Doctor added: ".concat(doctor.getIdNumber()));
                    listener.onResponse(response.body());
                } else {
                    Log.e(TAG, "onResponse: ".concat(GSON.toJson(response.errorBody())) );
                    listener.onError(context.getString(R.string.doc_add_failed));
                }
            }

            @Override
            public void onFailure(Call<Doctor> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(context.getString(R.string.doc_add_failed));

            }
        });
    }
    public void registerDeathCertificate(final DeathCertificate certificate, final Listener listener) {
        Call<DeathCertificate> call = apiService.registerDeathCertificate(certificate);
        call.enqueue(new Callback<DeathCertificate>() {
            @Override
            public void onResponse(Call<DeathCertificate> call, Response<DeathCertificate> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "DeathCertificate registered on blockchain: ".concat(certificate.getIdNumber()));
                    listener.onResponse(response.body());
                } else {
                    try {
                        listener.onError(context.getString(R.string.dc_add_failed));
                        Log.e(TAG, "onResponse: ".concat(GSON.toJson(response.errorBody())) );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<DeathCertificate> call, Throwable t) {
                Log.e(TAG, "onFailure: ",t );
                listener.onError(context.getString(R.string.dc_add_failed));

            }
        });
    }
    public void registerBurial(final Burial burial, final Listener listener) {
        Call<Burial> call = apiService.registerBurial(burial);
        call.enqueue(new Callback<Burial>() {
            @Override
            public void onResponse(Call<Burial> call, Response<Burial> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Burial registered: ".concat(burial.getIdNumber()));
                    listener.onResponse(response.body());
                } else {
                    try {
                        listener.onError("Failed to register burial");
                        Log.e(TAG, "onResponse: ".concat(GSON.toJson(response)) );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<Burial> call, Throwable t) {
                Log.e(TAG, "onFailure: ",t );
                listener.onError(context.getString(R.string.burial_failed));

            }
        });
    }
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public void addDeathCertificate(final DeathCertificate certificate, final Listener listener) {
        Call<DeathCertificate> call = apiService.addDeathCertificate(certificate);
        call.enqueue(new Callback<DeathCertificate>() {
            @Override
            public void onResponse(Call<DeathCertificate> call, Response<DeathCertificate> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "DeathCertificate added: ".concat(certificate.getIdNumber()));
                    listener.onResponse(response.body());
                } else {
                    Log.e(TAG, "onResponse: ".concat(GSON.toJson(response.errorBody())) );
                    listener.onError(context.getString(R.string.dc_add_failed));
                }
            }

            @Override
            public void onFailure(Call<DeathCertificate> call, Throwable t) {
                Log.e(TAG, "onFailure: ",t );
                listener.onError(context.getString(R.string.dc_add_failed));

            }
        });
    }
    public void addBurial(final Burial burial, final Listener listener) {
        Call<Burial> call = apiService.addBurial(burial);
        call.enqueue(new Callback<Burial>() {
            @Override
            public void onResponse(Call<Burial> call, Response<Burial> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Burial added: ".concat(GSON.toJson(burial)));
                    listener.onResponse(response.body());
                } else {

                    Log.e(TAG, "addBurial ERROR: ".concat(GSON.toJson(response)) );
                    listener.onError(context.getString(R.string.burial_add_failed));
                }
            }

            @Override
            public void onFailure(Call<Burial> call, Throwable t) {
                Log.e(TAG, "onFailure: ",t );
                listener.onError(context.getString(R.string.burial_add_failed));

            }
        });
    }
    public void addClaim(final Claim claim, final Listener listener) {
        Log.w(TAG, "########### addClaim: ".concat(GSON.toJson(claim)) );
        Call<Claim> call = apiService.registerClaim(claim);
        call.enqueue(new Callback<Claim>() {
            @Override
            public void onResponse(Call<Claim> call, Response<Claim> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Insurance claim added: ".concat(claim.getClaimId()));
                    listener.onResponse(response.body());
                } else {
                    Log.e(TAG, "addClaim error: ".concat(GSON.toJson(response)));
                    listener.onError(context.getString(R.string.claim_add_failed));
                }
            }

            @Override
            public void onFailure(Call<Claim> call, Throwable t) {
                Log.e(TAG, "onFailure: ",t );
                listener.onError(context.getString(R.string.claim_add_failed));

            }
        });
    }
    public void addPolicy(final Policy policy, final Listener listener) {
        Call<Policy> call = apiService.registerPolicy(policy);
        call.enqueue(new Callback<Policy>() {
            @Override
            public void onResponse(Call<Policy> call, Response<Policy> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Insurance policy added: ".concat(policy.getPolicyNumber()));
                    listener.onResponse(response.body());
                } else {
                    Log.e(TAG, "onResponse: ".concat(GSON.toJson(response)) );
                    listener.onError(context.getString(R.string.policy_add_failed));
                }
            }

            @Override
            public void onFailure(Call<Policy> call, Throwable t) {
                Log.e(TAG, "onFailure: ",t );
                listener.onError(context.getString(R.string.policy_add_failed));

            }
        });
    }
    public void addRegulator(final Regulator regulator, final Listener listener) {
        Call<Regulator> call = apiService.registerRegulator(regulator);
        call.enqueue(new Callback<Regulator>() {
            @Override
            public void onResponse(Call<Regulator> call, Response<Regulator> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Insurance regulator added: ".concat(regulator.getFirstName()));
                    listener.onResponse(response.body());
                } else {
                    Log.e(TAG, "onResponse: ".concat(GSON.toJson(response)) );
                    listener.onError(context.getString(R.string.regulator_add_failed));
                }
            }

            @Override
            public void onFailure(Call<Regulator> call, Throwable t) {
                Log.e(TAG, "onFailure: ",t );
                listener.onError(context.getString(R.string.regulator_add_failed));

            }
        });
    }
    public void addClient(final Client client, final Listener listener) {
        Call<Client> call = apiService.registerClient(client);
        call.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Insurance client added: ".concat(client.getFirstName()));
                    listener.onResponse(response.body());
                } else {
                    Log.e(TAG, "onResponse: ".concat(GSON.toJson(response)) );
                    listener.onError(context.getString(R.string.client_add_failed));
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Log.e(TAG, "onFailure: ",t );
                listener.onError(context.getString(R.string.client_add_failed));

            }
        });
    }
    public void addBeneficiary(final Beneficiary beneficiary, final Listener listener) {
        Call<Beneficiary> call = apiService.registerBeneficiary(beneficiary);
        call.enqueue(new Callback<Beneficiary>() {
            @Override
            public void onResponse(Call<Beneficiary> call, Response<Beneficiary> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Insurance beneficiary added: ".concat(beneficiary.getFirstName()));
                    listener.onResponse(response.body());
                } else {
                    Log.e(TAG, "onResponse: ".concat(GSON.toJson(response)) );
                    listener.onError(context.getString(R.string.benefic_add_failed));
                }
            }

            @Override
            public void onFailure(Call<Beneficiary> call, Throwable t) {
                Log.e(TAG, "onFailure: ",t );
                listener.onError(context.getString(R.string.benefic_add_failed));

            }
        });
    }
    public void addInsuranceCompany(final InsuranceCompany company, final Listener listener) {
        Call<InsuranceCompany> call = apiService.registerInsuranceCompany(company);
        Log.d(TAG, "addInsuranceCompany: ".concat(call.request().url().url().toString()));
        call.enqueue(new Callback<InsuranceCompany>() {
            @Override
            public void onResponse(Call<InsuranceCompany> call, Response<InsuranceCompany> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Insurance company added: ".concat(company.getName()));
                    listener.onResponse(response.body());
                } else {
                    Log.e(TAG, "onResponse: ".concat(GSON.toJson(response)) );
                    listener.onError(context.getString(R.string.company_add_failled));
                }
            }

            @Override
            public void onFailure(Call<InsuranceCompany> call, Throwable t) {
                Log.e(TAG, "onFailure: ",t );
                listener.onError(context.getString(R.string.company_add_failled));

            }
        });
    }
    public void addHospital(final Hospital hospital, final Listener listener) {
        Call<Hospital> call = apiService.registerHospital(hospital);
        Log.d(TAG, "addHospital: ".concat(call.request().url().url().toString()));
        call.enqueue(new Callback<Hospital>() {
            @Override
            public void onResponse(Call<Hospital> call, Response<Hospital> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Hospital  added: ".concat(hospital.getName()));
                    listener.onResponse(response.body());
                } else {
                    Log.e(TAG, "onResponse: ".concat(GSON.toJson(response)) );
                    listener.onError(context.getString(R.string.hosp_add_failed));
                }
            }

            @Override
            public void onFailure(Call<Hospital> call, Throwable t) {
                Log.e(TAG, "onFailure: ",t );
                listener.onError(context.getString(R.string.hosp_add_failed));

            }
        });
    }
    public void addFuneralParlour(final FuneralParlour funeralParlour, final Listener listener) {
        Call<FuneralParlour> call = apiService.registerFuneralParlour(funeralParlour);
        call.enqueue(new Callback<FuneralParlour>() {
            @Override
            public void onResponse(Call<FuneralParlour> call, Response<FuneralParlour> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "FuneralParlour  added: ".concat(funeralParlour.getName()));
                    listener.onResponse(response.body());
                } else {
                    Log.e(TAG, "onResponse: ".concat(GSON.toJson(response)) );
                    listener.onError(context.getString(R.string.fp_add_failed));
                }
            }

            @Override
            public void onFailure(Call<FuneralParlour> call, Throwable t) {
                Log.e(TAG, "onFailure: ",t );
                listener.onError(context.getString(R.string.fp_add_failed));

            }
        });
    }
    public static final String TAG = ChainDataAPI.class.getSimpleName();
}
