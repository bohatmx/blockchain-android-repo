package com.aftarobot.mlibrary.data;

import java.io.Serializable;

/**
 * Created by aubreymalabie on 1/13/18.
 */

public class HomeAffairs implements Data, Serializable {
    private String $class;
    private String governmentId;
    private String name;
    private String email;

    public String getClassz() { return this.$class; }

    public void setClass(String $class) { this.$class = $class; }


    public String getGovernmentId() { return this.governmentId; }

    public void setGovernmentId(String governmentId) { this.governmentId = governmentId; }


    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }


    public String getEmail() { return this.email; }

    public void setEmail(String email) { this.email = email; }
}
