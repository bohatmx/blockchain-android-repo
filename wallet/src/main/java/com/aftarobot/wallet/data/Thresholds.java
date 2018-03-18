package com.aftarobot.wallet.data;

public class Thresholds
{
  private int low_threshold;

  public int getLowThreshold() { return this.low_threshold; }

  public void setLowThreshold(int low_threshold) { this.low_threshold = low_threshold; }

  private int med_threshold;

  public int getMedThreshold() { return this.med_threshold; }

  public void setMedThreshold(int med_threshold) { this.med_threshold = med_threshold; }

  private int high_threshold;

  public int getHighThreshold() { return this.high_threshold; }

  public void setHighThreshold(int high_threshold) { this.high_threshold = high_threshold; }
}