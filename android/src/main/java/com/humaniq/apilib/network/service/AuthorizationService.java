package com.humaniq.apilib.network.service;

import android.content.Context;
import com.google.gson.JsonObject;
import com.humaniq.apilib.network.models.response.BasePayload;
import com.humaniq.apilib.network.models.response.Token;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

/**
 * Created by ognev on 7/28/17.
 */

public interface AuthorizationService {

  @GET("/tapatybe/api/v1/authenticate/user") Call<BasePayload<Token>> refreshJwtToken(@Body
      JsonObject jsonObject);
}
