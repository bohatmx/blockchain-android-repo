package com.aftarobot.wallet.data;

public class Signer
{
  private String public_key;

  public String getPublicKey() { return this.public_key; }

  public void setPublicKey(String public_key) { this.public_key = public_key; }

  private int weight;

  public int getWeight() { return this.weight; }

  public void setWeight(int weight) { this.weight = weight; }

  private String key;

  public String getKey() { return this.key; }

  public void setKey(String key) { this.key = key; }

  private String type;

  public String getType() { return this.type; }

  public void setType(String type) { this.type = type; }
}