package com.aftarobot.mlibrary.data;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by aubreymalabie on 1/12/18.
 */

public class InsuranceCompany implements Data, Serializable, Comparable<InsuranceCompany> {
    private String $class;
    private String email;
    private String insuranceCompanyId;
    private String name;
    private String address;

    public String getClassz() { return this.$class; }

    public void setClass(String $class) { this.$class = $class; }

    public String getEmail() { return this.email; }

    public void setEmail(String email) { this.email = email; }

    public String getInsuranceCompanyId() { return this.insuranceCompanyId; }

    public void setInsuranceCompanyId(String insuranceCompanyId) { this.insuranceCompanyId = insuranceCompanyId; }

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    public String getAddress() { return this.address; }

    public void setAddress(String address) { this.address = address; }

    @Override
    public int compareTo(@NonNull InsuranceCompany o) {

        return this.name.compareTo(o.name);

    }
}
