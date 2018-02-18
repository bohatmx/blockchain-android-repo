package com.aftarobot.mlibrary.data;

import java.io.Serializable;

public class BeneficiaryClaimMessage implements Data, Serializable{
    private String fcmToken;
    private Claim claim;

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


