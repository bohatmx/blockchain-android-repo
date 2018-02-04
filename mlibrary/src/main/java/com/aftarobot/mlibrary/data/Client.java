package com.aftarobot.mlibrary.data;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aubreymalabie on 1/12/18.
 */

public class Client extends Person implements Data, Serializable, Comparable<Client>{
    private Double accountBalance;
    private boolean isDeceased;
    private String insuranceCompany;
    private List<String> policies;

    public List<String> getPolicies() {
        return policies;
    }

    public void setPolicies(List<String> policies) {
        this.policies = policies;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public boolean isDeceased() {
        return isDeceased;
    }

    public void setDeceased(boolean deceased) {
        isDeceased = deceased;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

    @Override
    public int compareTo(@NonNull Client o) {
        String nameA = this.getLastName().concat(this.getFirstName());
        String nameB = o.getLastName().concat(o.getFirstName());
        return nameA.compareTo(nameB);
    }
}
