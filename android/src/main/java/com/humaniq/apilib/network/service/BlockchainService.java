package com.humaniq.apilib.network.service;

import com.humaniq.apilib.network.models.request.blockchain.TransferRequest;
import com.humaniq.apilib.network.models.request.wallet.UserTransaction;
import com.humaniq.apilib.network.models.response.BaseResponse;
import com.humaniq.apilib.network.models.response.blockchain.GetUserAddressStateModel;
import com.humaniq.apilib.network.models.response.blockchain.TransferResponse;
import com.humaniq.apilib.network.models.response.contacts.ContactsResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by gritsay on 7/26/17.
 */

public interface BlockchainService {
  //https://gold-star-1172.postman.co/docs/collection/view/2266679-6d383c78-e0d2-0f08-c21d-a61e0cf1b1ef#000166c6-4090-9b92-565d-64c1209925d1
  @POST("/blockchain-api/transfer")
  Call<TransferResponse> transferHmq (@Body TransferRequest transferRequest);

  @GET("/blockchain-api/get/user/address/state/{user_id}")
  //@Headers({"Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiIxcExEWk1BV1lUc0J1Wms1NjFaZVdJMFM4WEdVcFZrT1JnRjdIZk1OTWZPaDFiRDBLVWdxeHhBTERVR1NIZUJxaWthWVJuNjFSWnZiR1Q1aWJqQlJUeGNOR2pLRHM1ZW9qdmNneTN5dlp0ZFRrTGJvZ0RqSEoyS1dhWml0NmQ0NiJ9.zDGfoJFRG7foSYK9y_HY2RWGg39Hec6qzZ8oBFP1nBE"})
  Call <BaseResponse<Object>>getUserAddressState(
      @Path("user_id") String id
  );
}
