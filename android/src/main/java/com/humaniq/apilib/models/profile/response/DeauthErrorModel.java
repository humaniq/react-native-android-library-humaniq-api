package com.humaniq.apilib.models.profile.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gritsay on 7/19/17.
 */

public class DeauthErrorModel {
  @SerializedName("code")
  private int code;
  @SerializedName("message")
  private String message;

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
