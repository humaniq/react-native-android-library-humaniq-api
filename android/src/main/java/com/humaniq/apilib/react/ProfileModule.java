package com.humaniq.apilib.react;

import android.support.annotation.Nullable;
import android.util.Log;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.gson.Gson;
import com.humaniq.apilib.Codes;
import com.humaniq.apilib.Constants;
import com.humaniq.apilib.network.models.request.profile.AccountAvatar;
import com.humaniq.apilib.network.models.request.profile.AccountPassword;
import com.humaniq.apilib.network.models.request.profile.AccountPerson;
import com.humaniq.apilib.network.models.request.profile.UserId;
import com.humaniq.apilib.network.models.response.BasePayload;
import com.humaniq.apilib.network.models.response.profile.AccountAvatarResponse;
import com.humaniq.apilib.network.models.response.profile.AccountProfile;
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
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class ProfileModule extends ReactContextBaseJavaModule {

  private static final String LOG_TAG = "ProfileModule";
  private Runnable eventRunnable;
  private final ScheduledThreadPoolExecutor executor =
      new ScheduledThreadPoolExecutor(1);

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
    Log.d(LOG_TAG, "Balance Request: " + "id: " + id);
    ServiceBuilder.getWalletService().
        getUserBalance(id).enqueue(new retrofit2.Callback<BaseResponse<Balance>>() {
      @Override public void onResponse(Call<BaseResponse<Balance>> call,
          Response<BaseResponse<Balance>> response) {
        Log.d(LOG_TAG, "Balance Response: " + response.isSuccessful());
        if(response.body() != null && !response.body().equals("")) {
          try {
            WritableMap addressState = ModelConverterUtils.convertJsonToMap(new JSONObject(new Gson().toJson(response.body().data)));
            promise.resolve(addressState);
          } catch (JSONException e) {
            e.printStackTrace();
            promise.reject(e);
          }
        } else {
          promise.reject(new Throwable("fail"));
        }
      }


      @Override public void onFailure(Call<BaseResponse<Balance>> call, Throwable t) {
        promise.reject(t);
      }
    });
  }

  @ReactMethod public void getTransactions(String id, int offset, int limit, final Promise promise) {
    Log.d(LOG_TAG,  "Transaction Request: id: " + id + ",  offset: " + offset + ", limit: " + limit);
    ServiceBuilder.getWalletService()
        .getUserTransactions(id, offset, limit)
        .enqueue(new retrofit2.Callback<BaseResponse<List<UserTransaction>>>() {
          @Override public void onResponse(Call<BaseResponse<List<UserTransaction>>> call,
              Response<BaseResponse<List<UserTransaction>>> response) {
              Log.d(LOG_TAG, "Transaction Response: " + response.isSuccessful()
                  + ",body: " + response.body());
            if (response.body() != null && !response.body().equals("")) {
              try {

                WritableArray array = new WritableNativeArray();

                for (UserTransaction transaction : response.body().data) {
                  WritableMap collectionTransaction = ModelConverterUtils.
                      convertJsonToMap(new JSONObject(new Gson().toJson(transaction, UserTransaction.class)));
                  array.pushMap(collectionTransaction);
                }
                promise.resolve(array);
              } catch (JSONException e) {
                promise.reject("", e);
              }
            } else {
              promise.reject(new Throwable("fail"));
            }

            emulateTransactionEventChanged(); // todo remove after successful testing
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
        .enqueue(new Callback<BaseResponse<Object>>() {
          @Override public void onResponse(Call<BaseResponse<Object>> call,
              Response<BaseResponse<Object>> response) {
            WritableMap writableMap = new WritableNativeMap();
            writableMap.putString("status", "OK");
            promise.resolve(writableMap);
          }

          @Override public void onFailure(Call<BaseResponse<Object>> call, Throwable t) {
            promise.reject(t);
          }
        });

  }

  @ReactMethod public void updateUserPerson(
      String accountId,
      String firstName,
      String lastName,
      final Promise promise) {

    AccountPerson accountPerson = new AccountPerson();
    accountPerson.setAccountId(accountId);
    AccountPerson.Person person = new AccountPerson.Person();
    person.setFirstName(firstName);
    person.setLastName(lastName);
    accountPerson.setPerson(person);

    ServiceBuilder.getProfileService()
        .updateAccountPerson(accountPerson)
        .enqueue(new Callback<BasePayload<AccountPerson>>() {
          @Override public void onResponse(Call<BasePayload<AccountPerson>> call,
              Response<BasePayload<AccountPerson>> response) {
            if (response != null && response.body() != null
                && response.body().code == Codes.ACCOUNT_PERSONAL_UPDATE_SUCCESS) {
              try {
                WritableMap writableMap = ModelConverterUtils.convertJsonToMap(new JSONObject(new Gson().toJson(response.body())));
                promise.resolve(writableMap);
              } catch (JSONException e) {
                e.printStackTrace();
                promise.reject(e);
              }
            }
          }

          @Override public void onFailure(Call<BasePayload<AccountPerson>> call, Throwable t) {
            promise.reject(t);
          }
        });
  }

  @ReactMethod public void uploadProfileAvatar(
      String accountId,
      String avatarBse64,
      final Promise promise) {

    AccountAvatar accountAvatar = new AccountAvatar();
    accountAvatar.setAccountId(accountId);
    accountAvatar.setFacialImage(avatarBse64);
    ServiceBuilder.getProfileService()
        .updateAccountAvatar(accountAvatar)
        .enqueue(new Callback<BasePayload<AccountAvatarResponse>>() {
          @Override public void onResponse(Call<BasePayload<AccountAvatarResponse>> call,
              Response<BasePayload<AccountAvatarResponse>> response) {
            WritableMap avatarRespone = null;
            if(response.body() != null && !"".equals(response.body())) {
              try {
                avatarRespone = ModelConverterUtils.convertJsonToMap(new JSONObject(new Gson().toJson(response)));
                promise.resolve(avatarRespone);
              } catch (JSONException e) {
                e.printStackTrace();
              }
            }
          }

          @Override public void onFailure(Call<BasePayload<AccountAvatarResponse>> call, Throwable t) {
            promise.reject(t);
          }
        });


  }

  @ReactMethod public void changeProfilePassword(
      String accountId,
      String oldPassword, String newPassword,
      final Promise promise) {

    AccountPassword accountPassword = new AccountPassword();
    accountPassword.setAccountId(accountId);
    accountPassword.setOldPassword(oldPassword);
    accountPassword.setNewPassword(newPassword);

    ServiceBuilder.getProfileService()
        .updateAccountPassword(accountPassword)
        .enqueue(new Callback<BasePayload<Object>>() {
          @Override public void onResponse(Call<BasePayload<Object>> call,
              Response<BasePayload<Object>> response) {
            if(response != null && response.body() != null
                && response.body().code == Codes.ACCOUNT_PASSWORD_UPDATE_SUCCESS) {
              try {
                WritableMap writableMap = ModelConverterUtils.convertJsonToMap(new JSONObject(new Gson().toJson(response.body())));
                promise.resolve(writableMap);
              } catch (JSONException e) {
                e.printStackTrace();
                promise.reject(e);
              }
            }
          }

          @Override public void onFailure(Call<BasePayload<Object>> call, Throwable t) {
            promise.reject(t);
          }
        });
  }

  @ReactMethod public void getAccountProfile(String accountId, final Promise promise) {
    ServiceBuilder.getProfileService().getAccountProfile(accountId)
        .enqueue(new Callback<BasePayload<AccountProfile>>() {
          @Override public void onResponse(Call<BasePayload<AccountProfile>> call,
              Response<BasePayload<AccountProfile>> response) {
            if(response.body() != null && response.body().code == Codes.ACCOUNT_PROFILE_RETRIEVED) {
              try {
                WritableMap profile = ModelConverterUtils
                    .convertJsonToMap(new JSONObject(new Gson()
                        .toJson(response.body().payload, AccountProfile.class)));
                promise.resolve(profile);
              } catch (JSONException e) {
                e.printStackTrace();
                promise.reject(e);
              }
            }
          }

          @Override public void onFailure(Call<BasePayload<AccountProfile>> call, Throwable t) {
            promise.reject(t);
          }
        });
  }
  /*
    Sends an event OF TRANSACTION CHANGED to the JS module.
  */
  private void sendEvent(@Nullable WritableMap params) {
    this.getReactApplicationContext().
        getJSModule(DeviceEventManagerModule.
            RCTDeviceEventEmitter.class).
        emit(Constants.EVENT_TRANSACTION_CHANGED, params);
  }
  private void emulateTransactionEventChanged() {
    eventRunnable = new Runnable() {
      @Override
      public void run() {
        try {
          WritableMap changedTransaction = ModelConverterUtils.
              convertJsonToMap(new JSONObject("{"
                  + "    \"type\": 1,"
                  + "    \"status\": 2,"
                  + "    \"timestamp\": 1500594517,"
                  + "    \"amount\": -40,"
                  + "    \"from_address\": \"0xd4bb4bca1545508fc2ea9cb866a89cfa50ffaf6b\","
                  + "    \"to_address\": \"0x3d02c55481e58c6ddccab14f667f0625c4da6507\","
                  + "    \"from_user\": {"
                  + "      \"account_id\": \"\","
                  + "      \"name\": {"
                  + "        \"first_name\": \"John\","
                  + "        \"last_name\": \"Doe\""
                  + "      },"
                  + "      \"profile_photo\": {"
                  + "        \"url\": \"\","
                  + "        \"expiry\": \"0\""
                  + "      },"
                  + "      \"phone_number\": {"
                  + "        \"country_code\": \"0\","
                  + "        \"phone_number\": \"000000\""
                  + "      }"
                  + "    },"
                  + "    \"to_user\": {"
                  + "      \"account_id\": \"\","
                  + "      \"name\": {"
                  + "        \"first_name\": \"John\","
                  + "        \"last_name\": \"Doe\""
                  + "      },"
                  + "      \"profile_photo\": {"
                  + "        \"url\": \"\","
                  + "        \"expiry\": \"0\""
                  + "      },"
                  + "      \"phone_number\": {"
                  + "        \"country_code\": \"0\","
                  + "        \"phone_number\": \"000000\""
                  + "      }"
                  + "    }"
                  + "  }"));
          sendEvent(changedTransaction);
        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    };

    this.executor.scheduleWithFixedDelay(eventRunnable, 10, 10, TimeUnit.SECONDS);

  }

  @ReactMethod public void deauthenticateUser(String accountId, final Promise promise) {
    ServiceBuilder.getProfileService()
        .deauthenticateUser(new UserId(accountId))
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