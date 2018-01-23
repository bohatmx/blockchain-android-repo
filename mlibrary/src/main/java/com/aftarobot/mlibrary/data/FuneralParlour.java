package com.aftarobot.mlibrary.data;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by aubreymalabie on 1/13/18.
 */

public class FuneralParlour implements Data, Serializable, Comparable<FuneralParlour> {
    private String $class;
    private String funeralParlourId;
    private String email;
    private String name;
    private String address;
    private double latitude;
    private double longitude;

    public String getClassz() { return this.$class; }

    public void setClass(String $class) { this.$class = $class; }


    public String getFuneralParlourId() { return this.funeralParlourId; }

    public void setFuneralParlourId(String funeralParlourId) { this.funeralParlourId = funeralParlourId; }


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

    @Override
    public int compareTo(@NonNull FuneralParlour o) {
        return this.name.compareTo(o.name);
    }
}
