package com.aftarobot.mlibrary.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BeneficiaryThanks implements Data, Serializable {
    private String fcmToken, message, name;
    private FundsTransfer fundsTransfer;
    private long date;
    private String stringDate;
    public static final SimpleDateFormat fmt = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public void setStringDate(String stringDate) {
        this.stringDate = stringDate;
    }


    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public FundsTransfer getFundsTransfer() {
        return fundsTransfer;
    }

    public void setFundsTransfer(FundsTransfer fundsTransfer) {
        this.fundsTransfer = fundsTransfer;
    }
}
