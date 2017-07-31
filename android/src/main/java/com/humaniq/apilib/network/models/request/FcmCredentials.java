package com.humaniq.apilib.network.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ognev on 7/30/17.
 */

public class FcmCredentials {

  @SerializedName("account_id")
  private long accountId;

  @SerializedName("token")
  private String token;

  public long getAccountId() {
    return accountId;
  }

  public void setAccountId(long accountId) {
    this.accountId = accountId;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
