package com.aftarobot.mlibrary.data;

import java.io.Serializable;

/**
 * Created by aubreymalabie on 1/13/18.
 */

public class Claim implements Data, Serializable{
    private String $class;
    private String claimId, dateTime, companyId, policyNumber, insuranceCompany;
    private String policy, client, hospital;

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getClassz() { return this.$class; }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setClass(String $class) { this.$class = $class; }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getClaimId() { return this.claimId; }

    public void setClaimId(String claimId) { this.claimId = claimId; }


    public String getPolicy() { return this.policy; }

    public void setPolicy(String policy) { this.policy = policy; }
}
