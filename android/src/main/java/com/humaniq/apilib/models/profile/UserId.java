package com.humaniq.apilib.models.profile;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gritsay on 7/19/17.
 */

public class UserId {
  @SerializedName("account_id") private String accountId;

  public UserId(String accountId) {
    this.accountId = accountId;
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }
}
