package com.humaniq.apilib;

import android.widget.Toast;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
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
  public void show(String message, int duration) {
    Toast.makeText(getReactApplicationContext(), message, duration).show();
  }

  @ReactMethod
  public void stateResponseCall(Callback errorCallback,
                                final Callback successCallbac) {

    Call<BaseResponse<AddressState>> stateResponseCall =
            ServiceBuilder.getProfileService().
                    getAddressState("0x1111111111111111111111111111111111111111");

    stateResponseCall.enqueue(new retrofit2.Callback<BaseResponse<AddressState>>() {
      @Override
      public void onResponse(Call<BaseResponse<AddressState>> call, Response<BaseResponse<AddressState>> response) {
        successCallbac.invoke(response.body());
      }

      @Override
      public void onFailure(Call<BaseResponse<AddressState>> call, Throwable t) {
        successCallbac.invoke(t.getLocalizedMessage());
      }
    });

  }

  @ReactMethod
  public void getTransactions(String id, final Promise promise) {
    ServiceBuilder.getProfileService().getAddressTransactions(id)
            .enqueue(new retrofit2.Callback<BaseResponse<List<Transaction>>>() {
              @Override
              public void onResponse(Call<BaseResponse<List<Transaction>>> call,
                                     Response<BaseResponse<List<Transaction>>> response) {

                try {
                  WritableArray array = new WritableNativeArray();

                  for(Transaction transaction : response.body().data) {
                    WritableMap collectionTransaction = ModelUtils.convertJsonToMap(new JSONObject(transaction.toJsonString()));
                    collectionTransaction.putString("transaction", collectionTransaction.getString("transaction"));
                    collectionTransaction.putInt("type", collectionTransaction.getInt("type"));
                    collectionTransaction.putInt("status", collectionTransaction.getInt("status"));
                    collectionTransaction.putInt("amount", collectionTransaction.getInt("amount"));
                    collectionTransaction.putInt("receivedTransactions", collectionTransaction.getInt("receivedTransactions"));
                    collectionTransaction.putInt("sentTransactions", collectionTransaction.getInt("sentTransactions"));
                    collectionTransaction.putInt("invalidTransactions", collectionTransaction.getInt("invalidTransactions"));
                    collectionTransaction.putInt("timestamp", collectionTransaction.getInt("timestamp"));
                    array.pushMap(collectionTransaction);
                  }

                  promise.resolve(array);
                } catch (JSONException e) {
                  promise.reject("", e);
                }
              }

              @Override
              public void onFailure(Call<BaseResponse<List<Transaction>>> call,
                                    Throwable t) {

                promise.reject("", t);
              }
            });
  }

}