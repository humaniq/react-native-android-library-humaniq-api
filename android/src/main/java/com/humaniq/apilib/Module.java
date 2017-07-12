package com.humaniq.apilib;

import android.widget.Toast;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.humaniq.apilib.models.AddressState;
import com.humaniq.apilib.models.response.BaseResponse;
import com.humaniq.apilib.services.ServiceBuilder;

import java.util.HashMap;
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

}