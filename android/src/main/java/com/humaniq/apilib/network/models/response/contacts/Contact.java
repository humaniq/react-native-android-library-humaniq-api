package com.humaniq.apilib.network.models.response.contacts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gritsay on 7/28/17.
 */

public class Contact {

  @SerializedName("phoneNumber")
  private String phoneNumber;
  @SerializedName("accountId")
  private String accountId;

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }
}
