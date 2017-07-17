package com.humaniq.apilib.services.restService;

/**
 * Created by gritsay on 7/12/17.
 */

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import com.humaniq.apilib.models.AddressState;
import com.humaniq.apilib.models.response.BaseResponse;
import com.humaniq.apilib.models.Transaction;

/**
 * Wallet service
 */

public interface WalletService {

    @GET("get/address/state/{id}")
    Call<BaseResponse<AddressState>> getAddressState(@Path("id") String id);

    @GET("get/address/transactions/{id}")
    Call<BaseResponse<List<Transaction>>> getAddressTransactions(@Path("id") String id);

    @GET("get/last/block/number")
    Call<BaseResponse<Long>> getLastBlockNumber();

    @POST("/create/address")
    Call<BaseResponse<String>> createAddress(@Body JsonObject body);


}
