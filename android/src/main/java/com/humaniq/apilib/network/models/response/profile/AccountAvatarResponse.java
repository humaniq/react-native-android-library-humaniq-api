package com.humaniq.apilib.network.models.response.profile;

import com.google.gson.annotations.SerializedName;

public class AccountAvatarResponse {

  @SerializedName("account_id")
  private String accountId;

  @SerializedName("avatar")
  private AccountAvatarResponse.Avatar avatar;

  public static class Avatar {
    @SerializedName("url")
    private String url;

    @SerializedName("expiry")
    private long expiry;

    public String getUrl() {
      return url;
    }

    public long getExpiry() {
      return expiry;
    }
  }

  public String getAccountId() {
    return accountId;
  }

  public Avatar getAvatar() {
    return avatar;
  }
}
