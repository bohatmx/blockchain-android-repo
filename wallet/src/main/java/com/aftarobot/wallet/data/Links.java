package com.aftarobot.wallet.data;

public class Links
{
  private Self self;

  public Self getSelf() { return this.self; }

  public void setSelf(Self self) { this.self = self; }

  private Transactions transactions;

  public Transactions getTransactions() { return this.transactions; }

  public void setTransactions(Transactions transactions) { this.transactions = transactions; }

  private Operations operations;

  public Operations getOperations() { return this.operations; }

  public void setOperations(Operations operations) { this.operations = operations; }

  private Payments payments;

  public Payments getPayments() { return this.payments; }

  public void setPayments(Payments payments) { this.payments = payments; }

  private Effects effects;

  public Effects getEffects() { return this.effects; }

  public void setEffects(Effects effects) { this.effects = effects; }

  private Offers offers;

  public Offers getOffers() { return this.offers; }

  public void setOffers(Offers offers) { this.offers = offers; }

  private Trades trades;

  public Trades getTrades() { return this.trades; }

  public void setTrades(Trades trades) { this.trades = trades; }

  private Data data;

  public Data getData() { return this.data; }

  public void setData(Data data) { this.data = data; }
}
