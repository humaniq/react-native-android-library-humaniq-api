package com.humaniq.apilib.network.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ognev on 8/4/17.
 */

public class Token {
  @SerializedName("account_id")
  private String accountId;

  @SerializedName("token")
  private String token;

  public String getAccountId() {
    return accountId;
  }

  public String getToken() {
    return token;
  }
}
