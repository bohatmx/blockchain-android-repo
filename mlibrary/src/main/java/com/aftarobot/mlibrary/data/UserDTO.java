package com.aftarobot.mlibrary.data;

import java.io.Serializable;

/**
 * Created by aubreymalabie on 1/20/18.
 */

public class UserDTO implements Data, Serializable {
    private String uid, email, displayName, userID, hospitalId, funeralParlourId;
    private String companyId, fcmToken, password;
    private String userType, stringDateRegistered;
    private long dateRegistered;

    public static final String
            COMPANY_USER = "company-user",
            CLIENT = "client",
            BENEFICIARY = "beneficiary",
            REGULATOR = "regulator",
            HOME_AFFAIRS = "home-affairs",
            HOSPITAL_USER = "hospital-user",
            DOCTOR = "doctor",
            FUNERAL_PARLOUR_USER = "funeral-parlour";

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getFuneralParlourId() {
        return funeralParlourId;
    }

    public void setFuneralParlourId(String funeralParlourId) {
        this.funeralParlourId = funeralParlourId;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getStringDateRegistered() {
        return stringDateRegistered;
    }

    public void setStringDateRegistered(String date) {
        this.stringDateRegistered = date;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

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
