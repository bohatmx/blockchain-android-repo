package com.aftarobot.wallet.data;

public class RecordAsset
{
  private Links2 _links;

  public Links2 getLinks() { return this._links; }

  public void setLinks(Links2 _links) { this._links = _links; }

  private String asset_type;

  public String getAssetType() { return this.asset_type; }

  public void setAssetType(String asset_type) { this.asset_type = asset_type; }

  private String asset_code;

  public String getAssetCode() { return this.asset_code; }

  public void setAssetCode(String asset_code) { this.asset_code = asset_code; }

  private String asset_issuer;

  public String getAssetIssuer() { return this.asset_issuer; }

  public void setAssetIssuer(String asset_issuer) { this.asset_issuer = asset_issuer; }

  private String paging_token;

  public String getPagingToken() { return this.paging_token; }

  public void setPagingToken(String paging_token) { this.paging_token = paging_token; }

  private String amount;

  public String getAmount() { return this.amount; }

  public void setAmount(String amount) { this.amount = amount; }

  private int num_accounts;

  public int getNumAccounts() { return this.num_accounts; }

  public void setNumAccounts(int num_accounts) { this.num_accounts = num_accounts; }

  private Flags flags;

  public Flags getFlags() { return this.flags; }

  public void setFlags(Flags flags) { this.flags = flags; }
}