package com.humaniq.apilib.network.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ognev on 7/30/17.
 */

public class ValidateRequest {

  @SerializedName("facial_image_validation_id")
  private String facialImageId;

  @SerializedName("facial_image")
  private String facialImage;

  public String getFacialImageId() {
    return facialImageId;
  }

  public void setFacialImageId(String facialImageId) {
    this.facialImageId = facialImageId;
  }

  public String getFacialImage() {
    return facialImage;
  }

  public void setFacialImage(String facialImage) {
    this.facialImage = facialImage;
  }
}
