package com.humaniq.apilib.network.service;

import com.google.gson.JsonObject;
import com.humaniq.apilib.network.models.request.FcmCredentials;
import com.humaniq.apilib.network.models.response.BaseResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by ognev on 7/29/17.
 */

public interface FcmService {

  @POST("/fcm-mobile/api/v1/mobile_tokens")
  Call<BaseResponse<Object>> saveFcmToken(@Body FcmCredentials tokenBody);
}
