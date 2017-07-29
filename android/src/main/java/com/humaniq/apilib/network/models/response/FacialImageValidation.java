package com.humaniq.apilib.network.models.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by ognev on 7/29/17.
 */

public class FacialImageValidation {

  @SerializedName("facial_image_validation_id")
  private String facialImageValidationId;

  @SerializedName("required_emotions")
  private List<String> requiredEmotions;

  public String getFacialImageValidationId() {
    return facialImageValidationId;
  }

  public void setFacialImageValidationId(String facialImageValidationId) {
    this.facialImageValidationId = facialImageValidationId;
  }

  public List<String> getRequiredEmotions() {
    return requiredEmotions;
  }

  public void setRequiredEmotions(List<String> requiredEmotions) {
    this.requiredEmotions = requiredEmotions;
  }
}
