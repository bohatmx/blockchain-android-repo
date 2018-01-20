package com.aftarobot.mlibrary.data;

import java.io.Serializable;

/**
 * Created by aubreymalabie on 1/13/18.
 */

public class Claim implements Data, Serializable{
    private String $class;
    private String claimId;
    private String policy;

    public String getClassz() { return this.$class; }

    public void setClass(String $class) { this.$class = $class; }


    public String getClaimId() { return this.claimId; }

    public void setClaimId(String claimId) { this.claimId = claimId; }


    public String getPolicy() { return this.policy; }

    public void setPolicy(String policy) { this.policy = policy; }
}
