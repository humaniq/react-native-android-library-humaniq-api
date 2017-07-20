package com.humaniq.apilib.react;

import android.util.Log;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.google.gson.Gson;
import com.humaniq.apilib.Constants;
import com.humaniq.apilib.network.models.request.profile.UserId;
import com.humaniq.apilib.network.models.response.profile.DeauthErrorModel;
import com.humaniq.apilib.network.models.response.profile.DeauthModel;
import com.humaniq.apilib.utils.ModelConverterUtils;
import com.humaniq.apilib.network.models.request.wallet.Balance;
import com.humaniq.apilib.network.models.request.wallet.UserTransaction;
import com.humaniq.apilib.network.models.response.BaseResponse;
import com.humaniq.apilib.network.service.providerApi.ServiceBuilder;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class ProfileModule extends ReactContextBaseJavaModule {

  private static final String LOG_TAG = "ProfileModule";

  public ProfileModule(ReactApplicationContext reactContext) {
    super(reactContext);
    ServiceBuilder.init(Constants.BASE_URL, reactContext);
  }

  @Override public String getName() {
    return "HumaniqProfileApiLib";
  }

  @Override public Map<String, Object> getConstants() {
    return new HashMap<>();
  }

  @ReactMethod public void getBalance(String id, final Promise promise) {
    ServiceBuilder.getWalletService().
        getUserBalance(id).enqueue(new retrofit2.Callback<BaseResponse<Balance>>() {
      @Override public void onResponse(Call<BaseResponse<Balance>> call,
          Response<BaseResponse<Balance>> response) {
        try {
          WritableMap addressState = ModelConverterUtils.convertJsonToMap(
              new JSONObject(new Gson().toJson(response.body().data)));

          promise.resolve(addressState);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override public void onFailure(Call<BaseResponse<Balance>> call, Throwable t) {
        promise.reject(t);
      }
    });
  }

  @ReactMethod public void getTransactions(String id, int offset, int limit, final Promise promise) {
    ServiceBuilder.getWalletService()
        .getUserTransactions(id, offset, limit)
        .enqueue(new retrofit2.Callback<BaseResponse<List<UserTransaction>>>() {
          @Override public void onResponse(Call<BaseResponse<List<UserTransaction>>> call,
              Response<BaseResponse<List<UserTransaction>>> response) {

            try {
              WritableArray array = new WritableNativeArray();

              for (UserTransaction transaction : response.body().data) {
                WritableMap collectionTransaction = ModelConverterUtils.
                    convertJsonToMap(
                        new JSONObject(new Gson().toJson(transaction, UserTransaction.class)));
                array.pushMap(collectionTransaction);
              }
              promise.resolve(array);
            } catch (JSONException e) {
              promise.reject("", e);
            }
          }

          @Override
          public void onFailure(Call<BaseResponse<List<UserTransaction>>> call, Throwable t) {
            promise.reject("", t);
          }
        });
  }

  @ReactMethod public void createTransaction(String fromUserId, String toUserId,
      float amount, final Promise promise) {
    ServiceBuilder.getWalletService()
        .createTransaction(fromUserId, toUserId, amount)
        .enqueue(new Callback<BaseResponse<String>>() {
          @Override public void onResponse(Call<BaseResponse<String>> call,
              Response<BaseResponse<String>> response) {
            WritableMap writableMap = new WritableNativeMap();
            writableMap.putString("status", "OK");
            promise.resolve(writableMap);
          }

          @Override public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
            promise.reject(t);
          }
        });

  }

  @ReactMethod public void deauthenticateUser(String id, final Promise promise) {
    ServiceBuilder.getProfileService()
        .deauthenticateUser(new UserId(id))
        .enqueue(new Callback<DeauthModel>() {
          @Override public void onResponse(Call<DeauthModel> call, Response<DeauthModel> response) {
            if (response.body()!=null && !response.body().equals("")) {
              //right request, error response
              promise.resolve(response.body());
            } else {
              Converter<ResponseBody, DeauthErrorModel> errorConverter =
                  ServiceBuilder.getRetrofit().responseBodyConverter(DeauthErrorModel.class, new Annotation[0]);
              try {
                DeauthErrorModel model = errorConverter.convert(response.errorBody());
                Log.d(LOG_TAG, "error code = "+model.getCode());
                Log.d(LOG_TAG, "error message = "+model.getMessage());
                promise.reject(Integer.toString(model.getCode()), model.getMessage());
              } catch (IOException e) {
                e.printStackTrace();
                promise.reject(e);
              }
            }
          }

          @Override public void onFailure(Call<DeauthModel> call, Throwable t) {
            promise.reject(t);
          }
        });

  }

}