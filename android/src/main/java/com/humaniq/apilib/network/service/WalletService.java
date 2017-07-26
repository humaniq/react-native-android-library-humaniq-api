package com.humaniq.apilib.network.service;

/**
 * Created by gritsay on 7/12/17.
 */

import com.humaniq.apilib.network.models.request.wallet.Balance;
import com.humaniq.apilib.network.models.request.wallet.UserTransaction;
import com.humaniq.apilib.network.models.response.BaseResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Wallet service
 */

public interface WalletService {

  @GET("/wallet/api/v1/users/{user_id}/balance") Call<BaseResponse<Balance>> getUserBalance(
      @Path("user_id") String id);

  @GET("/wallet/api/v1/users/{user_id}/transactions")
  Call<BaseResponse<List<UserTransaction>>> getUserTransactions(
      @Path("user_id") String id, @Query("offset") int offset, @Query("limit") int limit);

  //@GET("get/last/block/number")
  //Call<BaseResponse<Long>> getLastBlockNumber();
  @FormUrlEncoded
  @POST(value = "/wallet/api/v1/users/{user_id}/transactions") Call<BaseResponse<Object>> createTransaction(
      @Path("user_id") String userId, @Field("to_user_id") String toUserId, @Field("amount") float amount);


}
