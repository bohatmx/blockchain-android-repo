package com.aftarobot.wallet.data;

import java.util.ArrayList;

public class Account {

    private Links _links;

    public Links getLinks() { return this._links; }

    public void setLinks(Links _links) { this._links = _links; }

    private String id;

    public String getId() { return this.id; }

    public void setId(String id) { this.id = id; }

    private String paging_token;

    public String getPagingToken() { return this.paging_token; }

    public void setPagingToken(String paging_token) { this.paging_token = paging_token; }

    private String account_id;

    public String getAccountId() { return this.account_id; }

    public void setAccountId(String account_id) { this.account_id = account_id; }

    private String sequence;

    public String getSequence() { return this.sequence; }

    public void setSequence(String sequence) { this.sequence = sequence; }

    private int subentry_count;

    public int getSubentryCount() { return this.subentry_count; }

    public void setSubentryCount(int subentry_count) { this.subentry_count = subentry_count; }

    private Thresholds thresholds;

    public Thresholds getThresholds() { return this.thresholds; }

    public void setThresholds(Thresholds thresholds) { this.thresholds = thresholds; }

    private Flags flags;

    public Flags getFlags() { return this.flags; }

    public void setFlags(Flags flags) { this.flags = flags; }

    private ArrayList<Balance> balances;

    public ArrayList<Balance> getBalances() { return this.balances; }

    public void setBalances(ArrayList<Balance> balances) { this.balances = balances; }

    private ArrayList<Signer> signers;

    public ArrayList<Signer> getSigners() { return this.signers; }

    public void setSigners(ArrayList<Signer> signers) { this.signers = signers; }

    private Data data;

    public Data getData() { return this.data; }

    public void setData(Data data) { this.data = data; }
}
