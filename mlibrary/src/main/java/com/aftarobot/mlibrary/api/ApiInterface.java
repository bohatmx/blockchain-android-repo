package com.aftarobot.mlibrary.api;

import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.Burial;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.DeathCertificate;
import com.aftarobot.mlibrary.data.Doctor;
import com.aftarobot.mlibrary.data.FuneralParlour;
import com.aftarobot.mlibrary.data.HomeAffairs;
import com.aftarobot.mlibrary.data.Hospital;
import com.aftarobot.mlibrary.data.InsuranceCompany;
import com.aftarobot.mlibrary.data.Policy;
import com.aftarobot.mlibrary.data.Regulator;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    @GET("Client")
    Call<List<Client>> getClients();

    @POST("Beneficiary")
    Call<Beneficiary> registerBeneficiary(@Body Beneficiary beneficiary);

    @GET("Beneficiary")
    Call<List<Beneficiary>> getBeneficiaries();

    @POST("Doctor")
    Call<Doctor> registerDoctor(@Body Doctor client);

    @GET("Doctor")
    Call<List<Doctor>> getDoctors();

    @POST("Policy")
    Call<Policy> registerPolicy(@Body Policy policy);

    @GET("queries/GetinsuranceCompanyPolicies")
    Call<List<Policy>> getCompanyPolicies(String insuranceCompanyId, double limitParam, double skipParam);

    @GET("Policy")
    Call<List<Policy>> getPolicies();


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


    @POST("RegisterDeathCertificate")
    Call<DeathCertificate> registerDeathCertificate(@Body DeathCertificate certificate);
    @POST("RegisterBurial")
    Call<Burial> registerBurial(@Body Burial burial);
}
