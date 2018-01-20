package com.aftarobot.mlibrary.data;

import java.io.Serializable;

/**
 * Created by aubreymalabie on 1/20/18.
 */

public class UserDTO implements Data, Serializable {
    private String uid, email, displayName;
    private String companyId;
    private String userType;
    private long dateRegistered;

    public static final String
            COMPANY_USER = "company-user",
            CLIENT = "client",
            BENEFICIARY = "beneficiary",
            REGULATOR = "regulator",
            HOME_AFFAIRS = "home-affairs",
            HOSPITAL_USER = "hospital-user",
            DOCTOR = "doctor";

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public long getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(long dateRegistered) {
        this.dateRegistered = dateRegistered;
    }
}
