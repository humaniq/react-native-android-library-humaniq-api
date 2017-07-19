package com.humaniq.apilib.services.restService;

import com.google.gson.annotations.SerializedName;
import com.humaniq.apilib.models.profile.UserId;
import com.humaniq.apilib.models.profile.response.DeauthModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by gritsay on 7/19/17.
 */

public interface ProfileService {

  @POST("tapatybe/api/v1/deauthenticate/user")
  @SerializedName("account_id") Call<DeauthModel> deauthenticateUser(@Body UserId userId);

}
