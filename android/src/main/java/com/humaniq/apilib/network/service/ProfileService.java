package com.humaniq.apilib.network.service;

import com.humaniq.apilib.network.models.request.profile.AccountAvatar;
import com.humaniq.apilib.network.models.request.profile.AccountPassword;
import com.humaniq.apilib.network.models.request.profile.AccountPerson;
import com.humaniq.apilib.network.models.request.profile.UserId;
import com.humaniq.apilib.network.models.response.BasePayload;
import com.humaniq.apilib.network.models.response.BaseResponse;
import com.humaniq.apilib.network.models.response.profile.AccountAvatarResponse;
import com.humaniq.apilib.network.models.response.profile.DeauthModel;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by gritsay on 7/19/17.
 */

public interface ProfileService {

  @POST("/tapatybe/api/v1/deauthenticate/user")
  Call<DeauthModel> deauthenticateUser(@Body UserId userId);

  @GET("/tapatybe/api/v1/accaunt/profile")
  Call<BaseResponse<Object>> getAccountProfile(@Query("account_id") String accauntId);

  @GET("/tapatybe/api/v1/accaunt/profiles")
  Call<BaseResponse<Object>> getAccountProfiles(@QueryMap Map<String, String> accauntId);


  @POST("/tapatybe/api/v1/account/person")
  Call<BasePayload<AccountPerson>> updateAccountPerson(@Body AccountPerson accountPerson);


  @POST("/tapatybe/api/v1/account/password")
  Call<BasePayload<Object>> updateAccountPassword(@Body AccountPassword accountPassword);


  @POST("/tapatybe/api/v1/account/avatar")
  Call<BasePayload<AccountAvatarResponse>> updateAccountAvatar(@Body AccountAvatar accountAvatar);




}
