package com.aftarobot.mlibrary.data;

import java.io.Serializable;
import java.util.Date;

public class FundsTransfer implements Data, Serializable {
    private String fundsTransferId, fundsTransferRequest, fromAccount, toAccount, insuranceCompany, bank;
    private Date dateTime;
    private double amount;

    public String getFundsTransferId() {
        return fundsTransferId;
    }

    public void setFundsTransferId(String fundsTransferId) {
        this.fundsTransferId = fundsTransferId;
    }

    public String getFundsTransferRequest() {
        return fundsTransferRequest;
    }

    public void setFundsTransferRequest(String fundsTransferRequest) {
        this.fundsTransferRequest = fundsTransferRequest;
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
