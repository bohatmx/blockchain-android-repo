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
    private List<String> policies, companies;

    public List<String> getCompanies() {
        return companies;
    }

    public void setCompanies(List<String> companies) {
        this.companies = companies;
    }

    public List<String> getPolicies() {
        return policies;
    }

    public void setPolicies(List<String> policies) {
        this.policies = policies;
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
