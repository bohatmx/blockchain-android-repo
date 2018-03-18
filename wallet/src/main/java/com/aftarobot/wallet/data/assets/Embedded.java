package com.aftarobot.wallet.data.assets;

import com.aftarobot.wallet.data.RecordAsset;

import java.util.ArrayList;

public class Embedded
{
  private ArrayList<RecordAsset> recordAssets;

  public ArrayList<RecordAsset> getRecordAssets() { return this.recordAssets; }

  public void setRecordAssets(ArrayList<RecordAsset> recordAssets) { this.recordAssets = recordAssets; }
}