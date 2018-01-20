package com.aftarobot.mlibrary.data;

import java.io.Serializable;

/**
 * Created by aubreymalabie on 1/12/18.
 */

public class InsuranceCompany implements Data, Serializable {
    private String $class;
    private String email;
    private String insuranceCompanyID;
    private String name;
    private String address;

    public String getClassz() { return this.$class; }

    public void setClass(String $class) { this.$class = $class; }

    public String getEmail() { return this.email; }

    public void setEmail(String email) { this.email = email; }

    public String getInsuranceCompanyID() { return this.insuranceCompanyID; }

    public void setInsuranceCompanyID(String insuranceCompanyID) { this.insuranceCompanyID = insuranceCompanyID; }

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    public String getAddress() { return this.address; }

    public void setAddress(String address) { this.address = address; }
}
