package com.aftarobot.mlibrary.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by aubreymalabie on 1/13/18.
 */

public class DeathCertificate implements Data, Serializable {
    private String $class;
    private String idNumber;
    private Date dateTime;
    private String causeOfDeath;
    private String hospital;
    private String doctor;
    private String client;



    public String getClassz() { return this.$class; }

    public void setClass(String $class) { this.$class = $class; }


    public String getIdNumber() { return this.idNumber; }

    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }


    public Date getDateTime() { return this.dateTime; }

    public void setDateTime(Date dateTime) { this.dateTime = dateTime; }


    public String getCauseOfDeath() { return this.causeOfDeath; }

    public void setCauseOfDeath(String causeOfDeath) { this.causeOfDeath = causeOfDeath; }


    public String getHospital() { return this.hospital; }

    public void setHospital(String hospital) { this.hospital = hospital; }


    public String getClient() { return this.client; }

    public void setClient(String client) { this.client = client; }


    public String getDoctor() { return this.doctor; }

    public void setDoctor(String doctor) { this.doctor = doctor; }
}
