package com.humaniq.apilib.network.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ognev on 7/29/17.
 */

public class FacialImage {

  @SerializedName("facial_image_id")
  private String facialImageId;

  public String getFacialImageId() {
    return facialImageId;
  }

  public void setFacialImageId(String facialImageId) {
    this.facialImageId = facialImageId;
  }
}
