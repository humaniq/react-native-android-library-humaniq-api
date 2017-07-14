package com.humaniq.apilib;

import android.widget.Toast;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.google.gson.Gson;
import com.humaniq.apilib.constructor.ModelUtils;
import com.humaniq.apilib.models.AddressState;
import com.humaniq.apilib.models.Transaction;
import com.humaniq.apilib.models.response.BaseResponse;
import com.humaniq.apilib.services.ServiceBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class Module extends ReactContextBaseJavaModule {

  private static final String DURATION_SHORT_KEY = "SHORT";
  private static final String DURATION_LONG_KEY = "LONG";

  public Module(ReactApplicationContext reactContext) {
    super(reactContext);
    ServiceBuilder.init();

  }

  @Override
  public String getName() {
    return "HumaniqApiLib";
  }

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
    constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
    return constants;
  }

  @ReactMethod
  public void show(String message) {
    Toast.makeText(getReactApplicationContext(), message,
    Toast.LENGTH_LONG).show();
  }

  @ReactMethod
  public void getAddressState(String id, final Promise promise) {
    ServiceBuilder.getProfileService().
            getAddressState(id)
            .enqueue(new retrofit2.Callback<BaseResponse<AddressState>>() {
              @Override
              public void onResponse(Call<BaseResponse<AddressState>> call,
                                     Response<BaseResponse<AddressState>> response) {
                try {
                  WritableMap addressState =
                          ModelUtils.convertJsonToMap(new JSONObject(new Gson().toJson(response.body().data)));

                  promise.resolve(addressState);

                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }

              @Override
              public void onFailure(Call<BaseResponse<AddressState>> call,
                                    Throwable t) {
                promise.reject(t);
              }
            });

  }

  @ReactMethod
  public void getTransactions(String id, final Promise promise) {

    WritableArray writableArray = new WritableNativeArray();
//    WritableMap collectionTransaction = ModelUtils.convertJsonToMap(new JSONObject(""));
//    ServiceBuilder.getProfileService().getAddressTransactions(id)
//            .enqueue(new retrofit2.Callback<BaseResponse<List<Transaction>>>() {
//              @Override
//              public void onResponse(Call<BaseResponse<List<Transaction>>> call,
//                                     Response<BaseResponse<List<Transaction>>> response) {
//
//                try {
//                  WritableArray array = new WritableNativeArray();
//
//                  for(Transaction transaction : response.body().data) {
//                    WritableMap collectionTransaction = ModelUtils.convertJsonToMap(new JSONObject(transaction.toJsonString()));
//                    collectionTransaction.putString("transaction", collectionTransaction.getString("transaction"));
//                    collectionTransaction.putInt("type", collectionTransaction.getInt("type"));
//                    collectionTransaction.putInt("status", collectionTransaction.getInt("status"));
//                    collectionTransaction.putString("amount", collectionTransaction.getString("amount"));
//                    collectionTransaction.putInt("receivedTransactions", collectionTransaction.getInt("received_transactions"));
//                    collectionTransaction.putInt("sentTransactions", collectionTransaction.getInt("sent_transactions"));
//                    //collectionTransaction.putInt("invalidTransactions", collectionTransaction.getInt("invalid_transactions"));
//                  //  collectionTransaction.putInt("timestamp", collectionTransaction.getInt("timestamp"));
//                    array.pushMap(collectionTransaction);
//                  }
////Toast.makeText(getReactApplicationContext(),"hello", Toast.LENGTH_SHORT).show();
//                 promise.resolve(array);
//
//} catch (JSONException e) {
//                  promise.reject("", e);
//                }
//              }
//
//              @Override
//              public void onFailure(Call<BaseResponse<List<Transaction>>> call,
//                                    Throwable t) {
//
//                promise.reject("", t);
//              }
//            });
  }

}
