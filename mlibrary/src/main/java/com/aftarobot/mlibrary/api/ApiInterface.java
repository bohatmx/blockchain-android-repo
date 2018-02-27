package com.aftarobot.mlibrary.api;

import com.aftarobot.mlibrary.data.Bank;
import com.aftarobot.mlibrary.data.BankAccount;
import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.Burial;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.ClaimApproval;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.DeathCertificate;
import com.aftarobot.mlibrary.data.DeathCertificateRequest;
import com.aftarobot.mlibrary.data.Doctor;
import com.aftarobot.mlibrary.data.FundsTransfer;
import com.aftarobot.mlibrary.data.FundsTransferRequest;
import com.aftarobot.mlibrary.data.FuneralParlour;
import com.aftarobot.mlibrary.data.HistorianRecord;
import com.aftarobot.mlibrary.data.HomeAffairs;
import com.aftarobot.mlibrary.data.Hospital;
import com.aftarobot.mlibrary.data.Identity;
import com.aftarobot.mlibrary.data.InsuranceCompany;
import com.aftarobot.mlibrary.data.Policy;
import com.aftarobot.mlibrary.data.PolicyBeneficiary;
import com.aftarobot.mlibrary.data.Regulator;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by aubreymalabie on 1/12/18.
 */

public interface ApiInterface {
    @POST("InsuranceCompany")
    Call<InsuranceCompany> registerInsuranceCompany(@Body InsuranceCompany company);

    @GET("InsuranceCompany")
    Call<List<InsuranceCompany>> getInsuranceCompanies();

    @GET("InsuranceCompany/{id}")
    Call<InsuranceCompany> getInsuranceCompany(@Path("id") String id);

    @POST("Bank")
    Call<Bank> addBank(@Body Bank bank);

    @GET("Bank")
    Call<List<Bank>> getBanks();

    @GET("Bank/{id}")
    Call<Bank> getBank(@Path("id") String id);

    @POST("BankAccount")
    Call<BankAccount> addBankAccount(@Body BankAccount bankAccount);

    @GET("BankAccount")
    Call<List<BankAccount>> getBankAccounts();

    @GET("BankAccount/{id}")
    Call<BankAccount> getBankAccount(@Path("id") String accountNumber);

    @POST("Hospital")
    Call<Hospital> addHospital(@Body Hospital hospital);

    @GET("Hospital")
    Call<List<Hospital>> getHospital();

    @POST("FuneralParlour")
    Call<FuneralParlour> addFuneralParlour(@Body FuneralParlour funeralParlour);

    @GET("FuneralParlour")
    Call<List<FuneralParlour>> getFuneralParlours();


    @POST("Client")
    Call<Client> addClient(@Body Client client);

    @GET("Client/{id}")
    Call<Client> getClient(@Path("id") String id);

    @PUT("Client/{id}/")
    Call<Client> updateClient(@Path("id") String idNumber, @Body Client client);

    @GET("Client")
    Call<List<Client>> getClients();

    @POST("Beneficiary")
    Call<Beneficiary> registerBeneficiary(@Body Beneficiary beneficiary);

    @GET("Beneficiary")
    Call<List<Beneficiary>> getBeneficiaries();

    @GET("Beneficiary/{id}/")
    Call<Beneficiary> getBeneficiary(@Path("id") String id);

    @PUT("Beneficiary/{id}/")
    Call<Beneficiary> updateBeneficiary(@Path("id") String idNumber, @Body Beneficiary beneficiary);

    @POST("Doctor")
    Call<Doctor> registerDoctor(@Body Doctor client);

    @GET("Doctor")
    Call<List<Doctor>> getDoctors();

    @GET("queries/getinsuranceCompanyPolicies")
    Call<List<Policy>> getCompanyPolicies(@Query("insuranceCompanyId") String insuranceCompanyId);

    @GET("queries/getInsuranceCompanyClaims")
    Call<List<Claim>> getCompanyClaims(@Query("insuranceCompanyId") String insuranceCompanyId);

    @GET("queries/getInsuranceCompanyClients")
    Call<List<Client>> getCompanyClients(@Query("insuranceCompany") String insuranceCompany);

    @GET("Policy")
    Call<List<Policy>> getPolicies();

    @GET("Policy/{id}")
    Call<Policy> getPolicy(@Path("id") String id);

    @POST("Policy")
    Call<Policy> addPolicy(@Body Policy policy);

    @POST("RegisterPolicy")
    Call<Policy> registerPolicyViaTx(@Body Policy policy);

    @GET("Claim/{id}")
    Call<Claim> getClaim(@Path("id") String claimId);

    @GET("Claim")
    Call<List<Claim>> getClaims();

    @POST("Regulator")
    Call<Regulator> registerRegulator(@Body Regulator regulator);

    @GET("Regulator")
    Call<List<Regulator>> getRegulators();

    @POST("HomeAffairs")
    Call<HomeAffairs> registerHomeAffairs(@Body HomeAffairs homeAffairs);

    @GET("HomeAffairs")
    Call<List<HomeAffairs>> getHomeAffairs();

    @POST("DeathCertificate")
    Call<DeathCertificate> addDeathCertificate(@Body DeathCertificate deathCertificate);

    @GET("DeathCertificate")
    Call<List<DeathCertificate>> getDeathCertificates();

    @POST("Burial")
    Call<Burial> addBurial(@Body Burial burial);

    @GET("Burial")
    Call<List<Burial>> getBurials();

    @GET("system/historian")
    Call<List<HistorianRecord>> getHistorianRecords();

    @GET("system/identities")
    Call<List<Identity>> getIdentities();

    @POST("RegisterDeathCertificate")
    Call<DeathCertificate> registerDeathCertificate(@Body DeathCertificate certificate);

    @POST("RegisterBurial")
    Call<Burial> registerBurial(@Body Burial burial);

    @POST("DeathCertificateRequest")
    Call<DeathCertificateRequest> postDeathCertificateRequest(@Body DeathCertificateRequest deathCertificateRequest);

    @GET("DeathCertificateRequest")
    Call<List<DeathCertificateRequest>> getDeathCertificateRequests();

    @PUT("DeathCertificateRequest/{id}/")
    Call<DeathCertificateRequest> updateDeathCertificateRequest(@Path("id") String idNumber,
                                                                @Body DeathCertificateRequest deathCertificateRequest);

    @POST("AddDeathCertificateRequest")
    Call<DeathCertificateRequest> addDeathCertificateRequestViaTranx(@Body DeathCertificateRequest deathCertificateRequest);

    @GET("queries/getClaimByPolicyNumber")
    Call<List<Claim>> getClaimsByPolicyNumber(@Query("policyNumber") String policyNumber);

    @GET("queries/getPolicyByIdNumber")
    Call<List<Policy>> getPoliciesByIdNumber(@Query("idNumber") String idNumber);

    @GET("queries/getBankFundsTransferRequests")
    Call<List<FundsTransferRequest>> getFundsTransferRequests(@Query("bankId") String bankId);

    @GET("queries/getInsuranceCompanyFundsTransfers")
    Call<List<FundsTransfer>> getFundsTransfers(@Query("insuranceCompanyId") String companyId);

    @POST("SubmitClaim")
    Call<Claim> submitClaim(@Body Claim claim);

    @POST("RequestFundsTransfer")
    Call<FundsTransferRequest> requestFundsTransfer(@Body FundsTransferRequest fundsTransferRequest);

    @POST("TransferFunds")
    Call<FundsTransfer> transferFunds(@Body FundsTransfer fundsTransfer);

    @POST("AddBeneficiaryToPolicy")
    Call<PolicyBeneficiary> addBeneficiaryToPolicy(@Body PolicyBeneficiary policyBeneficiary);

    @POST("RegisterBeneficiaryBankAccount")
    Call<BankAccount> registerBeneficiaryBankAccount(@Body BankAccount account);

    @POST("RegisterClientBankAccount")
    Call<BankAccount> registerClientBankAccount(@Body BankAccount account);

    @POST("ProcessClaimApproval")
    Call<ClaimApproval> processClaimApproval(@Body ClaimApproval approval);
}
