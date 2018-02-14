package com.aftarobot.mlibrary.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.aftarobot.mlibrary.R;
import com.aftarobot.mlibrary.data.Bank;
import com.aftarobot.mlibrary.data.BankAccount;
import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.Burial;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.ClaimApproval;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.DeathCertificate;
import com.aftarobot.mlibrary.data.DeathCertificateRequest;
import com.aftarobot.mlibrary.data.Doctor;
import com.aftarobot.mlibrary.data.FundsTransfer;
import com.aftarobot.mlibrary.data.FundsTransferRequest;
import com.aftarobot.mlibrary.data.FuneralParlour;
import com.aftarobot.mlibrary.data.HomeAffairs;
import com.aftarobot.mlibrary.data.Hospital;
import com.aftarobot.mlibrary.data.InsuranceCompany;
import com.aftarobot.mlibrary.data.Policy;
import com.aftarobot.mlibrary.data.PolicyBeneficiary;
import com.aftarobot.mlibrary.data.Regulator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

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
        apiService = APIClient.getClient(ctx).create(ApiInterface.class);
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
                    Log.e(TAG, "onResponse: ".concat(GSON.toJson(response.errorBody())));
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

    public void addBank(final Bank bank, final Listener listener) {
        Call<Bank> call = apiService.addBank(bank);
        call.enqueue(new Callback<Bank>() {
            @Override
            public void onResponse(@NonNull Call<Bank> call, @NonNull Response<Bank> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Bank added: ".concat(bank.getName()));
                    listener.onResponse(response.body());
                } else {
                    Log.e(TAG, "onResponse: ".concat(GSON.toJson(response.errorBody())));
                    listener.onError("Failed to add Bank");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Bank> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError("Failed to add Bank");

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
                    Log.e(TAG, "onResponse: ".concat(GSON.toJson(response.errorBody())));
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
                        Log.e(TAG, "onResponse: ".concat(GSON.toJson(response.errorBody())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<DeathCertificate> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(context.getString(R.string.dc_add_failed));

            }
        });
    }

    public void addDeathCertificateRequestByTranx(final DeathCertificateRequest request, final Listener listener) {
        Call<DeathCertificateRequest> call = apiService.addDeathCertificateRequestViaTranx(request);
        call.enqueue(new Callback<DeathCertificateRequest>() {
            @Override
            public void onResponse(Call<DeathCertificateRequest> call, Response<DeathCertificateRequest> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "DeathCertificateRequest added on blockchain: ".concat(request.getIdNumber()));
                    listener.onResponse(response.body());
                } else {
                    try {
                        listener.onError(context.getString(R.string.dc_add_failed));
                        Log.e(TAG, "addDeathCertificateRequestByTranx onResponse: ".concat(response.errorBody().string()));
                    } catch (Exception e) {
                        Log.e(TAG, "onResponse: ERROR ", e);
                    }

                }
            }

            @Override
            public void onFailure(Call<DeathCertificateRequest> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(context.getString(R.string.dc_add_failed));

            }
        });
    }

    public void postDeathCertificateRequest(final DeathCertificateRequest request, final Listener listener) {
        Call<DeathCertificateRequest> call = apiService.postDeathCertificateRequest(request);
        call.enqueue(new Callback<DeathCertificateRequest>() {
            @Override
            public void onResponse(Call<DeathCertificateRequest> call, Response<DeathCertificateRequest> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "DeathCertificateRequest posted on blockchain: ".concat(request.getIdNumber()));
                    listener.onResponse(response.body());
                } else {
                    try {
                        listener.onError(context.getString(R.string.dc_add_failed));
                        Log.e(TAG, "onResponse: ".concat(GSON.toJson(response.errorBody())));
                    } catch (Exception e) {
                        Log.e(TAG, "onResponse: ERROR ", e);
                    }

                }
            }

            @Override
            public void onFailure(Call<DeathCertificateRequest> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(context.getString(R.string.dc_add_failed));

            }
        });
    }

    public void processClaimApproval(final ClaimApproval approval, final Listener listener) {
        Call<ClaimApproval> call = apiService.processClaimApproval(approval);
        call.enqueue(new Callback<ClaimApproval>() {
            @Override
            public void onResponse(Call<ClaimApproval> call, Response<ClaimApproval> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "ClaimApproval processed: ".concat(approval.getClaimApprovalId()));
                    listener.onResponse(response.body());
                } else {
                    try {
                        listener.onError("Failed to register burial");
                        Log.e(TAG, "onResponse: ".concat(GSON.toJson(response)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ClaimApproval> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError("Claim Approval Failed");

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
                        Log.e(TAG, "onResponse: ".concat(GSON.toJson(response)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<Burial> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(context.getString(R.string.burial_failed));

            }
        });
    }

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public void updateClientPolicies(final Client client, final Listener listener) {
        //Log.e(TAG, "updateClientPolicies: ".concat(GSON.toJson(client)) );
        String id = client.getIdNumber();
        client.setIdNumber(null);
        Call<Client> call = apiService.updateClient(id, client);
        call.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(@NonNull Call<Client> call, @NonNull Response<Client> response) {
                if (response.isSuccessful()) {
                    Client m = response.body();
                    Log.i(TAG, "onResponse: client policies updated"
                            .concat(GSON.toJson(m)));
                    listener.onResponse(m);

                } else {
                    Log.e(TAG, "onResponse: ERROR: ".concat(response.message()));
                    listener.onError(response.message());
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(t.getMessage());
            }
        });
    }
    public void updateBeneficiary(final Beneficiary beneficiary, final Listener listener) {
        String id = beneficiary.getIdNumber();
        beneficiary.setIdNumber(null);

        Call<Beneficiary> call = apiService.updateBeneficiary(id, beneficiary);
        call.enqueue(new Callback<Beneficiary>() {
            @Override
            public void onResponse(@NonNull Call<Beneficiary> call, @NonNull Response<Beneficiary> response) {
                if (response.isSuccessful()) {
                    Beneficiary m = response.body();
                    Log.i(TAG, "onResponse: Beneficiary policy  updated"
                            .concat(GSON.toJson(m)));
                    listener.onResponse(m);

                } else {
                    Log.e(TAG, "onResponse: ERROR: ".concat(response.message()));
                    listener.onError(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Beneficiary> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(t.getMessage());
            }
        });
    }
    public void updateDeathCertificateRequest(final DeathCertificateRequest deathCertificateRequest, final Listener listener) {
        //Log.e(TAG, "updateClientPolicies: ".concat(GSON.toJson(deathCertificateRequest)) );
        String id = deathCertificateRequest.getIdNumber();
        deathCertificateRequest.setIdNumber(null);

        Call<DeathCertificateRequest> call = apiService.updateDeathCertificateRequest(id, deathCertificateRequest);
        call.enqueue(new Callback<DeathCertificateRequest>() {
            @Override
            public void onResponse(@NonNull Call<DeathCertificateRequest> call, @NonNull Response<DeathCertificateRequest> response) {
                if (response.isSuccessful()) {
                    DeathCertificateRequest m = response.body();
                    Log.i(TAG, "onResponse: deathCertificateRequest issue flag updated"
                            .concat(GSON.toJson(m)));
                    listener.onResponse(m);

                } else {
                    Log.e(TAG, "onResponse: ERROR: ".concat(response.message()));
                    listener.onError(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<DeathCertificateRequest> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(t.getMessage());
            }
        });
    }

    public void addDeathCertificate(final DeathCertificate certificate, final Listener listener) {
        Call<DeathCertificate> call = apiService.addDeathCertificate(certificate);
        call.enqueue(new Callback<DeathCertificate>() {
            @Override
            public void onResponse(Call<DeathCertificate> call, Response<DeathCertificate> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "DeathCertificate added: ".concat(certificate.getIdNumber()));
                    listener.onResponse(response.body());
                } else {
                    Log.e(TAG, "onResponse: ".concat(GSON.toJson(response.errorBody())));
                    listener.onError(context.getString(R.string.dc_add_failed));
                }
            }

            @Override
            public void onFailure(Call<DeathCertificate> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
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

                    Log.e(TAG, "addBurial ERROR: ".concat(GSON.toJson(response)));
                    listener.onError(context.getString(R.string.burial_add_failed));
                }
            }

            @Override
            public void onFailure(Call<Burial> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(context.getString(R.string.burial_add_failed));

            }
        });
    }

    public void registerClientBankAccount(final BankAccount account, final Listener listener) {
        Call<BankAccount> call = apiService.registerClientBankAccount(account);
        call.enqueue(new Callback<BankAccount>() {
            @Override
            public void onResponse(Call<BankAccount> call, Response<BankAccount> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "client account added: ".concat(account.getAccountNumber()));
                    listener.onResponse(response.body());
                } else {
                    try {
                        Log.e(TAG, "registerClientBankAccount error: ".concat(response.errorBody().string()));
                    } catch (IOException e) {
                        listener.onError(e.getMessage());
                    }
                    listener.onError("Failed to register client bank account");
                }
            }

            @Override
            public void onFailure(Call<BankAccount> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(context.getString(R.string.claim_add_failed));

            }
        });
    }

    public void requestFundsTransfer(final FundsTransferRequest fundsTransferRequest, final Listener listener) {
        Call<FundsTransferRequest> call = apiService.requestFundsTransfer(fundsTransferRequest);
        call.enqueue(new Callback<FundsTransferRequest>() {
            @Override
            public void onResponse(Call<FundsTransferRequest> call, Response<FundsTransferRequest> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fundsTransferRequest added: ".concat(fundsTransferRequest.getFundsTransferRequestId()));
                    listener.onResponse(response.body());
                } else {
                    try {
                        Log.e(TAG, "registerClientBankAccount error: ".concat(response.errorBody().string()));
                    } catch (IOException e) {
                        listener.onError(e.getMessage());
                    }
                    listener.onError("Failed to request funds transfer");
                }
            }

            @Override
            public void onFailure(Call<FundsTransferRequest> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(context.getString(R.string.claim_add_failed));

            }
        });
    }
    public void transferFunds(final FundsTransfer fundsTransfer, final Listener listener) {
        Call<FundsTransfer> call = apiService.transferFunds(fundsTransfer);
        call.enqueue(new Callback<FundsTransfer>() {
            @Override
            public void onResponse(Call<FundsTransfer> call, Response<FundsTransfer> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fundsTransfer added: ".concat(fundsTransfer.getFundsTransferId()));
                    listener.onResponse(response.body());
                } else {
                    try {
                        Log.e(TAG, "registerClientBankAccount error: ".concat(response.errorBody().string()));
                    } catch (IOException e) {
                        listener.onError(e.getMessage());
                    }
                    listener.onError("Failed to transfer funds");
                }
            }

            @Override
            public void onFailure(Call<FundsTransfer> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(context.getString(R.string.claim_add_failed));

            }
        });
    }
    public void registerBeneficiaryBankAccount(final BankAccount account, final Listener listener) {
        Call<BankAccount> call = apiService.registerBeneficiaryBankAccount(account);
        call.enqueue(new Callback<BankAccount>() {
            @Override
            public void onResponse(Call<BankAccount> call, Response<BankAccount> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "beneficiary account added: ".concat(account.getAccountNumber()));
                    listener.onResponse(response.body());
                } else {
                    try {
                        Log.e(TAG, "registerBeneficiaryBankAccount error: ".concat(response.errorBody().string()));
                    } catch (IOException e) {
                        listener.onError(e.getMessage());
                    }
                    listener.onError("Failed to register beneficiary bank account");
                }
            }

            @Override
            public void onFailure(Call<BankAccount> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(context.getString(R.string.claim_add_failed));

            }
        });
    }

    public void submitClaim(final Claim claim, final Listener listener) {
        Log.w(TAG, "########### adding Claim via transaction: ".concat(GSON.toJson(claim)));
        Call<Claim> call = apiService.submitClaim(claim);
        call.enqueue(new Callback<Claim>() {
            @Override
            public void onResponse(Call<Claim> call, Response<Claim> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Insurance claim added: ".concat(claim.getClaimId()));
                    listener.onResponse(response.body());
                } else {
                    try {
                        Log.e(TAG, "addClaim error: ".concat(response.errorBody().string()));
                    } catch (IOException e) {
                        listener.onError(e.getMessage());
                    }
                    listener.onError(context.getString(R.string.claim_add_failed));
                }
            }

            @Override
            public void onFailure(Call<Claim> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(context.getString(R.string.claim_add_failed));

            }
        });
    }
    public void addPolicyBeneficiary(final PolicyBeneficiary policyBeneficiary, final Listener listener) {
        Log.w(TAG, "########### addPolicyBeneficiary via transaction: ".concat(GSON.toJson(policyBeneficiary)));
        Call<PolicyBeneficiary> call = apiService.addBeneficiaryToPolicy(policyBeneficiary);
        call.enqueue(new Callback<PolicyBeneficiary>() {
            @Override
            public void onResponse(Call<PolicyBeneficiary> call, Response<PolicyBeneficiary> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Insurance policy Beneficiary added: ".concat(policyBeneficiary.getPolicyNumber()));
                    listener.onResponse(response.body());
                } else {
                    try {
                        Log.e(TAG, "addPolicyBeneficiary error: ".concat(response.errorBody().string()));
                    } catch (IOException e) {
                        listener.onError(e.getMessage());
                    }
                    listener.onError("Failed adding Beneficiary to Policy");
                }
            }

            @Override
            public void onFailure(Call<PolicyBeneficiary> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError("Possible network error. Failed adding Beneficiary to Policy");

            }
        });
    }

    public void registerPolicyViaTransaction(final Policy policy, final Listener listener) {
        Call<Policy> call = apiService.registerPolicyViaTx(policy);
        Log.d(TAG, "registerPolicyViaTransaction: ".concat(call.request().url().toString()));
        call.enqueue(new Callback<Policy>() {
            @Override
            public void onResponse(Call<Policy> call, Response<Policy> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Insurance policy registered via blockchain transaction: ".concat(policy.getPolicyNumber()));
                    listener.onResponse(response.body());
                } else {
                    try {
                        Log.e(TAG, "onResponse: ".concat(response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    listener.onError(context.getString(R.string.policy_add_failed));
                }
            }

            @Override
            public void onFailure(Call<Policy> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError("Possible network error. ".concat(context.getString(R.string.policy_add_failed)));

            }
        });
    }
    public void addRegulator(final Regulator regulator, final Listener listener) {
        Call<Regulator> call = apiService.registerRegulator(regulator);
        call.enqueue(new Callback<Regulator>() {
            @Override
            public void onResponse(@NonNull Call<Regulator> call, @NonNull Response<Regulator> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Insurance regulator added: ".concat(regulator.getFullName()));
                    listener.onResponse(response.body());
                } else {
                    Log.e(TAG, "onResponse: ".concat(GSON.toJson(response)));
                    listener.onError(context.getString(R.string.regulator_add_failed));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Regulator> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(context.getString(R.string.regulator_add_failed));

            }
        });
    }

    public void addClient(final Client client, final Listener listener) {
        Call<Client> call = apiService.addClient(client);
        call.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(@NonNull Call<Client> call, @NonNull Response<Client> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "client added to blockchain: ".concat(client.getFullName()));
                    listener.onResponse(response.body());
                } else {
                    try {
                        Log.e(TAG, "onResponse: ".concat(GSON.toJson(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    listener.onError(context.getString(R.string.client_add_failed));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Client> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(context.getString(R.string.client_add_failed));

            }
        });
    }

    public void addBeneficiary(final Beneficiary beneficiary, final Listener listener) {
        Call<Beneficiary> call = apiService.registerBeneficiary(beneficiary);
        call.enqueue(new Callback<Beneficiary>() {
            @Override
            public void onResponse(@NonNull Call<Beneficiary> call, @NonNull Response<Beneficiary> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "beneficiary added: ".concat(beneficiary.getFullName()));
                    listener.onResponse(response.body());
                } else {
                    Log.e(TAG, "onResponse: ".concat(GSON.toJson(response)));
                    listener.onError(context.getString(R.string.benefic_add_failed));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Beneficiary> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ", t);
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
                    Log.e(TAG, "onResponse: ".concat(GSON.toJson(response)));
                    listener.onError(context.getString(R.string.company_add_failled));
                }
            }

            @Override
            public void onFailure(Call<InsuranceCompany> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(context.getString(R.string.company_add_failled));

            }
        });
    }

    public void addHospital(final Hospital hospital, final Listener listener) {
        Call<Hospital> call = apiService.addHospital(hospital);
        Log.d(TAG, "addHospital: ".concat(call.request().url().url().toString()));
        call.enqueue(new Callback<Hospital>() {
            @Override
            public void onResponse(Call<Hospital> call, Response<Hospital> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Hospital  added: ".concat(hospital.getName()));
                    listener.onResponse(response.body());
                } else {
                    Log.e(TAG, "onResponse: ".concat(GSON.toJson(response)));
                    listener.onError(context.getString(R.string.hosp_add_failed));
                }
            }

            @Override
            public void onFailure(Call<Hospital> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(context.getString(R.string.hosp_add_failed));

            }
        });
    }

    public void addFuneralParlour(final FuneralParlour funeralParlour, final Listener listener) {
        Call<FuneralParlour> call = apiService.addFuneralParlour(funeralParlour);
        call.enqueue(new Callback<FuneralParlour>() {
            @Override
            public void onResponse(Call<FuneralParlour> call, Response<FuneralParlour> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "FuneralParlour  added: ".concat(funeralParlour.getName()));
                    listener.onResponse(response.body());
                } else {
                    Log.e(TAG, "onResponse: ".concat(GSON.toJson(response)));
                    listener.onError(context.getString(R.string.fp_add_failed));
                }
            }

            @Override
            public void onFailure(Call<FuneralParlour> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onError(context.getString(R.string.fp_add_failed));

            }
        });
    }

    public static final String TAG = ChainDataAPI.class.getSimpleName();
}
