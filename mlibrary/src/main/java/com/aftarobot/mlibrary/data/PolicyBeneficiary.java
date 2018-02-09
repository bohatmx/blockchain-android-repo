package com.aftarobot.mlibrary.data;

import java.io.Serializable;

public class PolicyBeneficiary implements Data, Serializable{
    private String policyNumber;
    private Beneficiary beneficiary;

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
    }
}
