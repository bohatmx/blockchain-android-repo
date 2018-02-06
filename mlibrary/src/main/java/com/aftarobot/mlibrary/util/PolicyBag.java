package com.aftarobot.mlibrary.util;

import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.InsuranceCompany;
import com.aftarobot.mlibrary.data.Policy;

public class PolicyBag {
    Policy policy;
    Client client;
    InsuranceCompany company;

    public PolicyBag (Policy policy, Client client, InsuranceCompany company) {
        this.policy = policy;
        this.client = client;
        this.company = company;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public InsuranceCompany getCompany() {
        return company;
    }

    public void setCompany(InsuranceCompany company) {
        this.company = company;
    }
}