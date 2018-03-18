package com.aftarobot.wallet.data;

public class Flags
{
  private boolean auth_required;

  public boolean getAuthRequired() { return this.auth_required; }

  public void setAuthRequired(boolean auth_required) { this.auth_required = auth_required; }

  private boolean auth_revocable;

  public boolean getAuthRevocable() { return this.auth_revocable; }

  public void setAuthRevocable(boolean auth_revocable) { this.auth_revocable = auth_revocable; }
}