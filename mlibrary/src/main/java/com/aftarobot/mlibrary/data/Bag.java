package com.aftarobot.mlibrary.data;

import java.io.Serializable;
import java.util.List;

public class Bag implements Serializable {
    private List<Client> clients;
    private List<Beneficiary> beneficiaries;
    private List<Policy> policies;
    private List<InsuranceCompany> insuranceCompanies;

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public List<Beneficiary> getBeneficiaries() {
        return beneficiaries;
    }

    public void setBeneficiaries(List<Beneficiary> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    public List<Policy> getPolicies() {
        return policies;
    }

    public void setPolicies(List<Policy> policies) {
        this.policies = policies;
    }

    public List<InsuranceCompany> getInsuranceCompanies() {
        return insuranceCompanies;
    }

    public void setInsuranceCompanies(List<InsuranceCompany> insuranceCompanies) {
        this.insuranceCompanies = insuranceCompanies;
    }
}
