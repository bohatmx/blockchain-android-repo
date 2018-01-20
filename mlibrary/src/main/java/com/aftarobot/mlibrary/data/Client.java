package com.aftarobot.mlibrary.data;

import java.io.Serializable;

/**
 * Created by aubreymalabie on 1/12/18.
 */

public class Client extends Person implements Data, Serializable{
    private Double accountBalance;
    private boolean isDeceased;

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
}
