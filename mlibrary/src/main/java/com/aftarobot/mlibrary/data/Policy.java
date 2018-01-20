package com.aftarobot.mlibrary.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aubreymalabie on 1/13/18.
 */

public class Policy implements Data, Serializable {
    private String $class;
    private String policyNumber;
    private String description;
    private double amount;
    private String insuranceCompany;
    private String client;
    private List<String> beneficiaries;

    public String getClassz() { return this.$class; }

    public void setClass(String $class) { this.$class = $class; }


    public String getPolicyNumber() { return this.policyNumber; }

    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }


    public String getDescription() { return this.description; }

    public void setDescription(String description) { this.description = description; }


    public double getAmount() { return this.amount; }

    public void setAmount(double amount) { this.amount = amount; }


    public String getInsuranceCompany() { return this.insuranceCompany; }

    public void setInsuranceCompany(String insuranceCompany) { this.insuranceCompany = insuranceCompany; }

    public String getClient() { return this.client; }

    public void setClient(String client) { this.client = client; }


    public List<String> getBeneficiaries() { return this.beneficiaries; }

    public void setBeneficiaries(List<String> beneficiaries) { this.beneficiaries = beneficiaries; }
}
