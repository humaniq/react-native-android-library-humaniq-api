package com.humaniq.apilib.network.service;

import com.google.gson.JsonObject;
import com.humaniq.apilib.network.models.response.BasePayload;
import com.humaniq.apilib.network.models.response.BaseResponse;
import com.humaniq.apilib.network.models.response.FacialImage;
import com.humaniq.apilib.network.models.response.FacialImageValidation;
import com.humaniq.apilib.network.models.response.ValidationResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by ognev on 7/29/17.
 */

public interface ValidationService {

  /**
   * {
   "facial_image": "/9j/4AAQSkZJRgABAQEAYABgAAD....
   */
  @POST("/tapatybe/api/v1/registered")
  Call<BasePayload<FacialImage>> isRegistered(@Body JsonObject facialImage);

  /**
   * {
   "facial_image_id": "1569730674766644290"
   }
   */

  @POST("/tapatybe/api/v1/facial_recognition/validation")
  Call<BasePayload<FacialImageValidation>> createValidation(@Body JsonObject facialImageId);


  /**
  {
  "facial_image_validation_id": "1569730940819735619",
  "facial_image": "/9j/
   */

  @POST("/tapatybe/api/v1/facial_recognition/validate")
  Call<BasePayload<ValidationResponse>> validate(@Body JsonObject facialImageValidation);
}
