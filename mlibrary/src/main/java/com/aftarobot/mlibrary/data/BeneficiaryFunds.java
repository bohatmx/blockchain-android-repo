package com.aftarobot.mlibrary.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BeneficiaryFunds implements Data, Serializable{
    private String idNumber, fcmToken;
    private FundsTransfer fundsTransfer;
    private long date;
    private String stringDate;
    public static final SimpleDateFormat fmt = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
        stringDate = fmt.format(new Date(date));
    }

    public String getStringDate() {
        return stringDate;
    }

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
