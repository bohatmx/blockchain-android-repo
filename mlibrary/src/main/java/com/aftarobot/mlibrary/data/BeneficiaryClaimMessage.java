package com.aftarobot.mlibrary.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BeneficiaryClaimMessage implements Data, Serializable{
    private String fcmToken;
    private Claim claim;
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

    public void setStringDate(String stringDate) {
        this.stringDate = stringDate;
    }


    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }
}


