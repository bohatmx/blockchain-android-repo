package com.aftarobot.wallet.data.effects;

import com.aftarobot.wallet.data.Links2;

public class RecordEffects
{
  private Links2 _links;

  public Links2 getLinks() { return this._links; }

  public void setLinks(Links2 _links) { this._links = _links; }

  private String id;

  public String getId() { return this.id; }

  public void setId(String id) { this.id = id; }

  private String paging_token;

  public String getPagingToken() { return this.paging_token; }

  public void setPagingToken(String paging_token) { this.paging_token = paging_token; }

  private String account;

  public String getAccount() { return this.account; }

  public void setAccount(String account) { this.account = account; }

  private String type;

  public String getType() { return this.type; }

  public void setType(String type) { this.type = type; }

  private int type_i;

  public int getTypeI() { return this.type_i; }

  public void setTypeI(int type_i) { this.type_i = type_i; }

  private String starting_balance;

  public String getStartingBalance() { return this.starting_balance; }

  public void setStartingBalance(String starting_balance) { this.starting_balance = starting_balance; }

  private String asset_type;

  public String getAssetType() { return this.asset_type; }

  public void setAssetType(String asset_type) { this.asset_type = asset_type; }

  private String amount;

  public String getAmount() { return this.amount; }

  public void setAmount(String amount) { this.amount = amount; }
}