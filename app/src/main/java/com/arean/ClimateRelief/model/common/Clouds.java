package com.arean.ClimateRelief.model.common;

import com.google.gson.annotations.SerializedName;

public class Clouds {

  @SerializedName("all")
  private int all;

  public int getAll() {
    return all;
  }

  public void setAll(int all) {
    this.all = all;
  }
}