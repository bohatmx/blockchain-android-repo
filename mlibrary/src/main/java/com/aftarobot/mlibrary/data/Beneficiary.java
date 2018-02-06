package com.aftarobot.mlibrary.data;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aubreymalabie on 1/12/18.
 */

public class Beneficiary extends Person implements Data, Serializable, Comparable<Beneficiary> {
    private Double accountBalance;
    private String fcmToken, password, beneficiaryId;
    private List<String> policies;

    public List<String> getPolicies() {
        return policies;
    }

    public void setPolicies(List<String> policies) {
        this.policies = policies;
    }

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

    @Override
    public int compareTo(@NonNull Beneficiary o) {
        String name1 = this.getLastName().concat(this.getLastName());
        String name2 = o.getLastName().concat(o.getLastName());
        return name1.compareTo(name2);
    }
}
