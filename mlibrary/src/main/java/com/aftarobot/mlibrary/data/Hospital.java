package com.aftarobot.mlibrary.data;

import java.io.Serializable;

/**
 * Created by aubreymalabie on 1/12/18.
 */

public class Hospital implements Data, Serializable {
    private String $class;
    private String hospitalId;
    private String email;
    private String name;
    private String address;
    private double latitude;
    private double longitude;

    public String getClassz() { return this.$class; }

    public void setClass(String $class) { this.$class = $class; }


    public String getHospitalId() { return this.hospitalId; }

    public void setHospitalId(String hospitalId) { this.hospitalId = hospitalId; }


    public String getEmail() { return this.email; }

    public void setEmail(String email) { this.email = email; }


    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }


    public String getAddress() { return this.address; }

    public void setAddress(String address) { this.address = address; }


    public double getLatitude() { return this.latitude; }

    public void setLatitude(double latitude) { this.latitude = latitude; }


    public double getLongitude() { return this.longitude; }

    public void setLongitude(double longitude) { this.longitude = longitude; }
}
