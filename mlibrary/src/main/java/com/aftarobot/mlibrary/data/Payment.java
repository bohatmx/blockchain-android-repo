package com.aftarobot.mlibrary.data;

import java.io.Serializable;

public class Payment implements Data, Serializable{
    private String seed, sourceAccount, destinationAccount;
    private String amount, memo, toFCMToken, fromFCMToken;
    private boolean receiving;

    public boolean isReceiving() {
        return receiving;
    }

    public void setReceiving(boolean receiving) {
        this.receiving = receiving;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getToFCMToken() {
        return toFCMToken;
    }

    public void setToFCMToken(String toFCMToken) {
        this.toFCMToken = toFCMToken;
    }

    public String getFromFCMToken() {
        return fromFCMToken;
    }

    public void setFromFCMToken(String fromFCMToken) {
        this.fromFCMToken = fromFCMToken;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
