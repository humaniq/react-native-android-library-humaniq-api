package com.humaniq.apilib.network.models.request.profile;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ognev on 7/24/17.
 */

public class AccountAvatar {

  @SerializedName("account_id")
  private String accountId;

  @SerializedName("facial_image")
  private String facialImage;

  public String getAccountId() {
    return accountId;
  }

  public String getFacialImage() {
    return facialImage;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public void setFacialImage(String facialImage) {
    this.facialImage = facialImage;
  }
}
