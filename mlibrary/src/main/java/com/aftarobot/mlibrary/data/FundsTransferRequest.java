package com.aftarobot.mlibrary.data;

import java.io.Serializable;
import java.util.Date;

public class FundsTransferRequest implements Data, Serializable {
    private String fundsTransferRequestId, fromAccount, toAccount, insuranceCompany, bank, claim;
    private Date dateTime;
    private double amount;

    public String getFundsTransferRequestId() {
        return fundsTransferRequestId;
    }

    public void setFundsTransferRequestId(String fundsTransferRequestId) {
        this.fundsTransferRequestId = fundsTransferRequestId;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getClaim() {
        return claim;
    }

    public void setClaim(String claim) {
        this.claim = claim;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
