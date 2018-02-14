package com.aftarobot.mlibrary.data;

import java.io.Serializable;

public class ClaimApproval implements Data, Serializable{
    private String claimApprovalId, dateTime, claim, insuranceCompany;
    private boolean approved;

    public String getClaimApprovalId() {
        return claimApprovalId;
    }

    public void setClaimApprovalId(String claimApprovalId) {
        this.claimApprovalId = claimApprovalId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getClaim() {
        return claim;
    }

    public void setClaim(String claim) {
        this.claim = claim;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
