package com.aftarobot.wallet.data.payments;

public class Links2 {
  private Self2 self;

  public Self2 getSelf() {
    return this.self;
  }

  public void setSelf(Self2 self) {
    this.self = self;
  }

  private Transaction transaction;

  public Transaction getTransaction() {
    return this.transaction;
  }

  public void setTransaction(Transaction transaction) {
    this.transaction = transaction;
  }

  private Effects effects;

  public Effects getEffects() {
    return this.effects;
  }

  public void setEffects(Effects effects) {
    this.effects = effects;
  }

  private Succeeds succeeds;

  public Succeeds getSucceeds() {
    return this.succeeds;
  }

  public void setSucceeds(Succeeds succeeds) {
    this.succeeds = succeeds;
  }

  private Precedes precedes;

  public Precedes getPrecedes() {
    return this.precedes;
  }

  public void setPrecedes(Precedes precedes) {
    this.precedes = precedes;
  }
}