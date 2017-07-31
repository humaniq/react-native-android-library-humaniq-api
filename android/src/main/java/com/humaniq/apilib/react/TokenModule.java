package com.humaniq.apilib.react;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.google.gson.JsonObject;
import com.humaniq.apilib.Constants;
import com.humaniq.apilib.network.models.request.FcmCredentials;
import com.humaniq.apilib.network.models.response.BaseResponse;
import com.humaniq.apilib.network.service.providerApi.ServiceBuilder;
import com.humaniq.apilib.storage.Prefs;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ognev on 7/29/17.
 */

public class TokenModule extends ReactContextBaseJavaModule {

  public TokenModule(ReactApplicationContext reactContext) {
    super(reactContext);
    new Prefs(reactContext);
  }

  @Override public String getName() {
    return "HumaniqTokenApiLib";
  }

  @ReactMethod public void saveCredentials(
      String jwtToken, String accountId, Promise promise) {
    Prefs.saveJwtToken(jwtToken);
    Prefs.saveAccountId(accountId);
    WritableMap writableMap = new WritableNativeMap();
    writableMap.putString("status", "saved: " + Prefs.getJwtToken());
    try {
      sendRegistrationToServer();
    } catch (Exception e) {
      e.printStackTrace();
    }

    promise.resolve(writableMap);
  }


  private void sendRegistrationToServer() throws IOException {
    ServiceBuilder.init(Constants.BASE_URL, getReactApplicationContext());

    FcmCredentials fcmCredentials = new FcmCredentials();
    fcmCredentials.setAccountId(Long.valueOf(Prefs.getAccountId()));
    fcmCredentials.setToken(Prefs.getFCMToken());

    ServiceBuilder
        .getFcmService()
        .saveFcmToken(fcmCredentials)
        .enqueue(new Callback<BaseResponse<Object>>() {
          @Override public void onResponse(Call<BaseResponse<Object>> call,
              Response<BaseResponse<Object>> response) {

          }

          @Override public void onFailure(Call<BaseResponse<Object>> call, Throwable t) {

          }
        });
  }
  @ReactMethod public void getFCMToken(Promise promise) {
    new Prefs(getReactApplicationContext());
    WritableMap writableMap = new WritableNativeMap();
    writableMap.putString("token", Prefs.getFCMToken());
    promise.resolve(writableMap);
  }

  @ReactMethod public void getJwtToken(Promise promise) {
    WritableMap jwtMap = new WritableNativeMap();
    jwtMap.putString("token", Prefs.getJwtToken());
    promise.resolve(jwtMap);
  }
  @ReactMethod public void getAccountId(Promise promise) {
    WritableMap jwtMap = new WritableNativeMap();
    jwtMap.putString("token", Prefs.getAccountId());
    promise.resolve(jwtMap);
  }
}
