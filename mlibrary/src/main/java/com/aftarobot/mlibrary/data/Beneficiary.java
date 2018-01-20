package com.aftarobot.mlibrary.data;

import java.io.Serializable;

/**
 * Created by aubreymalabie on 1/12/18.
 */

public class Beneficiary extends Person implements Data, Serializable {
    private Double accountBalance;

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }
}
