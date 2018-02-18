package com.aftarobot.mlibrary.data;

import java.io.Serializable;

public class FundsTransfer implements Data, Serializable {
    private String fundsTransferId, fundsTransferRequest, fromAccount, toAccount,
            insuranceCompany, bank, claim;
    private String dateTime;
    private String bankId;
    private String insuranceCompanyId;
    private double amount;

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getInsuranceCompanyId() {
        return insuranceCompanyId;
    }

    public void setInsuranceCompanyId(String insuranceCompanyId) {
        this.insuranceCompanyId = insuranceCompanyId;
    }

    public String getClaim() {
        return claim;
    }

    public void setClaim(String claim) {
        this.claim = claim;
    }

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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
