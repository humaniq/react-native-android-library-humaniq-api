package com.humaniq.apilib.network.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ognev on 7/29/17.
 */

public class ValidationResponse {

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
