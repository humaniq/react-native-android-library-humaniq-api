package com.humaniq.apilib.services.restService;

/**
 * Created by gritsay on 7/12/17.
 */

import com.google.gson.JsonObject;
import com.humaniq.apilib.models.Balance;
import com.humaniq.apilib.models.Transaction;
import com.humaniq.apilib.models.UserTransaction;
import com.humaniq.apilib.models.response.BaseResponse;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
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

  @GET("/wallet/api/v1/users/{user_id}/transactions/")
  Call<BaseResponse<List<UserTransaction>>> getUserTransactions(
      @Path("user_id") String id, @Query("offset") int offset, @Query("limit") int limit);

  //@GET("get/last/block/number")
  //Call<BaseResponse<Long>> getLastBlockNumber();

  @POST("/wallet/api/v1/users/{user_id}/transactions") Call<BaseResponse<String>> createTransaction(
      @Path("user_id") String userId, @Field("to_user_id") String toUserId, @Field("amount") float amount);
}
