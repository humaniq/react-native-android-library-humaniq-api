package com.humaniq.apilib.react;

import android.util.Log;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.google.gson.Gson;
import com.humaniq.apilib.Constants;
import com.humaniq.apilib.network.models.request.blockchain.TransferRequest;
import com.humaniq.apilib.network.models.request.wallet.Balance;
import com.humaniq.apilib.network.models.response.BaseResponse;
import com.humaniq.apilib.network.models.response.blockchain.TransferResponse;
import com.humaniq.apilib.network.models.response.contacts.ContactsResponse;
import com.humaniq.apilib.network.service.providerApi.ServiceBuilder;
import com.humaniq.apilib.utils.ModelConverterUtils;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gritsay on 7/26/17.
 */

public class BlockchainModule extends ReactContextBaseJavaModule {
  private final String LOG_TAG = "BlockchainModule";

  public BlockchainModule(ReactApplicationContext reactContext) {
    super(reactContext);
    ServiceBuilder.init(Constants.CONTACTS_BASE_URL, reactContext);
  }

  @Override public String getName() {
    return "HumaniqBlockchainApiLib";
  }

  @ReactMethod
  public void transferHmq(String fromId, String toId, int amount, final Promise promise) {
    Log.d("LOG_TAG",
        "fromId = " + fromId + ", toId = " + toId + ", amount = " + String.valueOf(amount));
    TransferRequest transferRequest = new TransferRequest(fromId, toId, amount);

    ServiceBuilder.getBlockchainService()
        .transferHmq(transferRequest)
        .enqueue(new Callback<TransferResponse>() {
          @Override
          public void onResponse(Call<TransferResponse> call, Response<TransferResponse> response) {
            WritableMap transferResponeMap = null;
            if (response.body() != null && !"".equals(response.body())) {
              Log.d(LOG_TAG, "OnResponse - Error request");
              Log.d(LOG_TAG, response.errorBody().toString());
              promise.reject(response.errorBody().toString());
            } else {
              try {
                transferResponeMap = ModelConverterUtils.convertJsonToMap(
                    new JSONObject(new Gson().toJson(response)));
                promise.resolve(transferResponeMap);
              } catch (JSONException e) {
                e.printStackTrace();
                promise.reject(e);
              }
            }
          }

          @Override public void onFailure(Call<TransferResponse> call, Throwable t) {
            promise.reject(t);
          }
        });
  }
}
