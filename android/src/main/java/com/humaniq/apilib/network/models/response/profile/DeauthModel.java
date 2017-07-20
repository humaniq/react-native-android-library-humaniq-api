package com.humaniq.apilib.network.models.response.profile;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gritsay on 7/19/17.
 */

public class DeauthModel {
  @SerializedName("code") private int code;
  @SerializedName("message") private String message;

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
