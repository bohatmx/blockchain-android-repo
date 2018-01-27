package com.aftarobot.mlibrary.data;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by aubreymalabie on 1/12/18.
 */

public class Client extends Person implements Data, Serializable, Comparable<Client>{
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

    @Override
    public int compareTo(@NonNull Client o) {
        String nameA = this.getLastName().concat(this.getFirstName());
        String nameB = o.getLastName().concat(o.getFirstName());
        return nameA.compareTo(nameB);
    }
}
