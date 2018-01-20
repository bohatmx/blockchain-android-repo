package com.aftarobot.mlibrary.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by aubreymalabie on 1/13/18.
 */

public class Burial  implements Data, Serializable{
    private String $class;
    private Date dateTime;
    private String idNumber;
    private String deathCertificate;
    private String funeralParlour;

    public String getClassz() { return this.$class; }

    public void setClass(String $class) { this.$class = $class; }


    public Date getDateTime() { return this.dateTime; }

    public void setDateTime(Date dateTime) { this.dateTime = dateTime; }


    public String getIdNumber() { return this.idNumber; }

    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }


    public String getDeathCertificate() { return this.deathCertificate; }

    public void setDeathCertificate(String deathCertificate) { this.deathCertificate = deathCertificate; }


    public String getFuneralParlour() { return this.funeralParlour; }

    public void setFuneralParlour(String funeralParlour) { this.funeralParlour = funeralParlour; }
}
