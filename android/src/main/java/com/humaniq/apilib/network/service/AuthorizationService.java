package com.humaniq.apilib.network.service;

import android.content.Context;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by ognev on 7/28/17.
 */

public interface AuthorizationService {

  @GET("/") Call<Object> refreshJwtToken(String token);
}
