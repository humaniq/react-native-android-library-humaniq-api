package com.humaniq.apilib.network.service.providerApi;

import android.util.Log;
import com.facebook.infer.annotation.Present;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.humaniq.apilib.network.models.response.BasePayload;
import com.humaniq.apilib.network.models.response.Token;
import com.humaniq.apilib.storage.Prefs;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ognev on 7/28/17.
 */

public class JwtTokenInterceptor implements Interceptor {
  @Override public okhttp3.Response intercept(Chain chain) throws IOException {
      Request request = chain.request();

      if(Prefs.hasToken()) {
      request = request.newBuilder()
          .addHeader("Authorization", "Bearer " + Prefs.getJwtToken())
          .build();
      }

      Response response = chain.proceed(request);

      if(response.code() == 401) {

          Prefs.clearJwtToken();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("facial_image_id", Prefs.getFacialImageId());
        jsonObject.addProperty("password", Prefs.getPassword());
        JsonObject reactNativeImeiElement = new JsonObject();
        JsonObject deviceImeiElement = new JsonObject();
        deviceImeiElement.addProperty("device_imei", Prefs.getDeviceImei());
        reactNativeImeiElement.add("react_native_imei", deviceImeiElement);
        jsonObject.add("metadata", reactNativeImeiElement);
        jsonObject.addProperty("password", Prefs.getPassword());
        retrofit2.Response<BasePayload<Token>> newJwtTokenResponse =  ServiceBuilder.getAuthorizationService()
            .refreshJwtToken(jsonObject).execute();
        if(newJwtTokenResponse.body() != null) {
          Prefs.saveJwtToken(newJwtTokenResponse.body().payload.getToken());
        } else {
          Log.d("JwtTokenInterceptor", "token null!!");
        }

        request = request.newBuilder()
            .addHeader("Authorization", "Bearer " + Prefs.getJwtToken())
            .build();

        return chain.proceed(request);
      }

      return response;
  }
}
