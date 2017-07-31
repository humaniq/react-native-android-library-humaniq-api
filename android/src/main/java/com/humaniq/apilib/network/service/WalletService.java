package com.humaniq.apilib.network.service;

/**
 * Created by gritsay on 7/12/17.
 */

import com.humaniq.apilib.network.models.request.wallet.Balance;
import com.humaniq.apilib.network.models.request.wallet.UserTransaction;
import com.humaniq.apilib.network.models.response.BaseResponse;
import com.humaniq.apilib.network.models.response.TransactionResponse;
import com.humaniq.apilib.network.models.response.profile.ExchangeModelHmq;
import com.humaniq.apilib.network.models.response.profile.ExchangeModelUsd;
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

  @GET("/wallet/api/v1/users/{user_id}/transactions/{hash}")
  Call<BaseResponse<UserTransaction>> getUserTransaction(
      @Path("user_id") String id, @Path("hash") String hash);

  //@GET("get/last/block/number")
  //Call<BaseResponse<Long>> getLastBlockNumber();
  @FormUrlEncoded
  @POST(value = "/wallet/api/v1/users/{user_id}/transactions") Call<BaseResponse<TransactionResponse>> createTransaction(
      @Path("user_id") String userId, @Field("to_user_id") String toUserId,
      @Field("to_address") String toAddress, @Field("amount") float amount);


  //curl --request GET \
  //    --url 'https://beta-api.humaniq.co/currency/api/v1/usd_exchange?amount=10'
  @GET("/currency/api/v1/usd_exchange")
  Call<BaseResponse<ExchangeModelHmq>> getExchangeHmq(
      @Query("amount") String amount);

  @GET("/currency/api/v1/hmq_exchange")
  Call<BaseResponse<ExchangeModelUsd>> getExchangeUsd(
      @Query("amount") String amount);



}
