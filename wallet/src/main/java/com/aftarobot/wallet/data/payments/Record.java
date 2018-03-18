package com.aftarobot.wallet.data.payments;

import java.util.Date;

public class Record
{
  private Links2 _links;
  private String paging_token;
  private String id;
  private String source_account;
  private String type, asset_type, from, to;
  private int type_i;
  private String account, amount;
  private String funder;
  private String starting_balance;
  private String transaction_hash;
  private Date created_at;

  public Links2 getLinks() { return this._links; }

  public void setLinks(Links2 _links) { this._links = _links; }


  public String getId() { return this.id; }

  public void setId(String id) { this.id = id; }

    public Links2 get_links() {
        return _links;
    }

    public void set_links(Links2 _links) {
        this._links = _links;
    }

    public String getPaging_token() {
        return paging_token;
    }

    public void setPaging_token(String paging_token) {
        this.paging_token = paging_token;
    }

    public String getSource_account() {
        return source_account;
    }

    public void setSource_account(String source_account) {
        this.source_account = source_account;
    }

    public String getAsset_type() {
        return asset_type;
    }

    public void setAsset_type(String asset_type) {
        this.asset_type = asset_type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getType_i() {
        return type_i;
    }

    public void setType_i(int type_i) {
        this.type_i = type_i;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStarting_balance() {
        return starting_balance;
    }

    public void setStarting_balance(String starting_balance) {
        this.starting_balance = starting_balance;
    }

    public String getTransaction_hash() {
        return transaction_hash;
    }

    public void setTransaction_hash(String transaction_hash) {
        this.transaction_hash = transaction_hash;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getPagingToken() { return this.paging_token; }

  public void setPagingToken(String paging_token) { this.paging_token = paging_token; }



  public String getSourceAccount() { return this.source_account; }

  public void setSourceAccount(String source_account) { this.source_account = source_account; }



  public String getType() { return this.type; }

  public void setType(String type) { this.type = type; }



  public int getTypeI() { return this.type_i; }

  public void setTypeI(int type_i) { this.type_i = type_i; }



  public Date getCreatedAt() { return this.created_at; }

  public void setCreatedAt(Date created_at) { this.created_at = created_at; }



  public String getTransactionHash() { return this.transaction_hash; }

  public void setTransactionHash(String transaction_hash) { this.transaction_hash = transaction_hash; }



  public String getStartingBalance() { return this.starting_balance; }

  public void setStartingBalance(String starting_balance) { this.starting_balance = starting_balance; }



  public String getFunder() { return this.funder; }

  public void setFunder(String funder) { this.funder = funder; }



  public String getAccount() { return this.account; }

  public void setAccount(String account) { this.account = account; }
}