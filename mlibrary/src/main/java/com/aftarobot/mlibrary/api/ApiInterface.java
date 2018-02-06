package com.aftarobot.mlibrary.api;

import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.Burial;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.DeathCertificate;
import com.aftarobot.mlibrary.data.DeathCertificateRequest;
import com.aftarobot.mlibrary.data.Doctor;
import com.aftarobot.mlibrary.data.FuneralParlour;
import com.aftarobot.mlibrary.data.HistorianRecord;
import com.aftarobot.mlibrary.data.HomeAffairs;
import com.aftarobot.mlibrary.data.Hospital;
import com.aftarobot.mlibrary.data.Identity;
import com.aftarobot.mlibrary.data.InsuranceCompany;
import com.aftarobot.mlibrary.data.Policy;
import com.aftarobot.mlibrary.data.Regulator;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by aubreymalabie on 1/12/18.
 */

public interface ApiInterface {
    @POST("InsuranceCompany")
    Call<InsuranceCompany> registerInsuranceCompany(@Body InsuranceCompany company);

    @GET("InsuranceCompany")
    Call<List<InsuranceCompany>> getInsuranceCompanies();

    @GET("InsuranceCompany/{id}")
    Call<InsuranceCompany> getInsuranceCompanyById(@Path("id") String id);

    @POST("Hospital")
    Call<Hospital> registerHospital(@Body Hospital hospital);

    @GET("Hospital")
    Call<List<Hospital>> getHospital();

    @POST("FuneralParlour")
    Call<FuneralParlour> registerFuneralParlour(@Body FuneralParlour funeralParlour);

    @GET("FuneralParlour")
    Call<List<FuneralParlour>> getFuneralParlours();

    @POST("Client")
    Call<Client> registerClient(@Body Client client);

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

    @PUT("Beneficiary/{id}/")
    Call<Beneficiary> updateBeneficiary(@Path("id") String idNumber, @Body Beneficiary beneficiary);

    @POST("Doctor")
    Call<Doctor> registerDoctor(@Body Doctor client);

    @GET("Doctor")
    Call<List<Doctor>> getDoctors();

    @GET("queries/getinsuranceCompanyPolicies")
    Call<List<Policy>> getCompanyPolicies(@Path("insuranceCompanyId") String insuranceCompanyId);

    @GET("Policy")
    Call<List<Policy>> getPolicies();

    @GET("Policy/{id}")
    Call<Policy> getPolicy(@Path("id") String id);

    @POST("Policy")
    Call<Policy> registerPolicy(@Body Policy policy);

    @POST("RegisterPolicy")
    Call<Policy> registerPolicyViaTx(@Body Policy policy);

    @POST("Claim")
    Call<Claim> registerClaim(@Body Claim claim);

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

    @GET("/system/historian")
    Call<List<HistorianRecord>> getHistorianRecords();
    @GET("/system/identities")
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

    @GET("/queries/getClaimByPolicyNumber")
    Call<List<Claim>> getClaimsByPolicyNumber(@Path("policyNumber") String policyNumber);

}
