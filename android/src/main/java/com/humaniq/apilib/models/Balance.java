package com.humaniq.apilib.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ognev on 7/19/17.
 */

public class Balance {

  @SerializedName("HMQ") private float hmq;

  @SerializedName("USD") private float usd;

  public float getHmq() {
    return hmq;
  }

  public float getUsd() {
    return usd;
  }
}
