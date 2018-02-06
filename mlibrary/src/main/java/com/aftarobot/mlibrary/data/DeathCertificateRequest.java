package com.aftarobot.mlibrary.data;

import java.io.Serializable;

/**
 * Created by aubreymalabie on 1/13/18.
 */

public class DeathCertificateRequest implements Data, Serializable {
    private String $class;
    private String idNumber;
    private String dateTime;
    private String causeOfDeath;
    private String hospital;
    private String doctor;
    private String client;
    private boolean issued;

    public boolean isIssued() {
        return issued;
    }

    public void setIssued(boolean issued) {
        this.issued = issued;
    }

    public String getClassz() { return this.$class; }

    public void setClass(String $class) { this.$class = $class; }


    public String getIdNumber() { return this.idNumber; }

    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }


    public String getDateTime() { return this.dateTime; }

    public void setDateTime(String dateTime) { this.dateTime = dateTime; }


    public String getCauseOfDeath() { return this.causeOfDeath; }

    public void setCauseOfDeath(String causeOfDeath) { this.causeOfDeath = causeOfDeath; }


    public String getHospital() { return this.hospital; }

    public void setHospital(String hospital) { this.hospital = hospital; }


    public String getClient() { return this.client; }

    public void setClient(String client) { this.client = client; }


    public String getDoctor() { return this.doctor; }

    public void setDoctor(String doctor) { this.doctor = doctor; }
}
