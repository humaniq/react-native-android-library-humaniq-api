package com.humaniq.apilib.network.models.request.profile;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ognev on 7/24/17.
 */

public class AccountPassword {

  @SerializedName("account_id")
  private String accountId;

  @SerializedName("old_password")
  private String oldPassword;

  @SerializedName("new_password")
  private String newPassword;

  public String getAccountId() {
    return accountId;
  }

  public String getOldPassword() {
    return oldPassword;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public void setOldPassword(String oldPassword) {
    this.oldPassword = oldPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }
}
