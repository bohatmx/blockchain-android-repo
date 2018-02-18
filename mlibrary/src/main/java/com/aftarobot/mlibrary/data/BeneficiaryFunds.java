package com.aftarobot.mlibrary.data;

import java.io.Serializable;

public class BeneficiaryFunds implements Data, Serializable{
    private String idNumber, fcmToken;
    private FundsTransfer fundsTransfer;

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public FundsTransfer getFundsTransfer() {
        return fundsTransfer;
    }

    public void setFundsTransfer(FundsTransfer fundsTransfer) {
        this.fundsTransfer = fundsTransfer;
    }
}
