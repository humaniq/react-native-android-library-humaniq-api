package com.humaniq.apilib.network.service;

import com.humaniq.apilib.network.models.request.blockchain.TransferRequest;
import com.humaniq.apilib.network.models.response.blockchain.TransferResponse;
import com.humaniq.apilib.network.models.response.contacts.ContactsResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by gritsay on 7/26/17.
 */

public interface BlockchainService {
  //https://gold-star-1172.postman.co/docs/collection/view/2266679-6d383c78-e0d2-0f08-c21d-a61e0cf1b1ef#000166c6-4090-9b92-565d-64c1209925d1
  @POST("/blockchain-api/transfer")
  Call<TransferResponse> transferHmq (@Body TransferRequest transferRequest);
}
