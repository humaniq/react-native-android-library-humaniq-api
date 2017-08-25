package com.humaniq.apilib.react;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.util.Log;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.humaniq.apilib.Codes;
import com.humaniq.apilib.Constants;
import com.humaniq.apilib.network.models.request.profile.AccountAvatar;
import com.humaniq.apilib.network.models.request.profile.AccountPassword;
import com.humaniq.apilib.network.models.request.profile.AccountPerson;
import com.humaniq.apilib.network.models.request.profile.UserId;
import com.humaniq.apilib.network.models.request.wallet.Balance;
import com.humaniq.apilib.network.models.request.wallet.UserTransaction;
import com.humaniq.apilib.network.models.response.BasePayload;
import com.humaniq.apilib.network.models.response.BaseResponse;
import com.humaniq.apilib.network.models.response.TransactionResponse;
import com.humaniq.apilib.network.models.response.profile.AccountAvatarResponse;
import com.humaniq.apilib.network.models.response.profile.AccountProfile;
import com.humaniq.apilib.network.models.response.profile.DeauthErrorModel;
import com.humaniq.apilib.network.models.response.profile.DeauthModel;
import com.humaniq.apilib.network.models.response.profile.ExchangeModelHmq;
import com.humaniq.apilib.network.models.response.profile.ExchangeModelUsd;
import com.humaniq.apilib.network.service.providerApi.ServiceBuilder;
import com.humaniq.apilib.storage.Prefs;
import com.humaniq.apilib.utils.ModelConverterUtils;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.HttpException;
import retrofit2.Response;

public class ProfileModule extends ReactContextBaseJavaModule {

  private static final String LOG_TAG = "ProfileModule";
  private final MixpanelAPI mixpanel;
  private Runnable eventRunnable;
  private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

  public ProfileModule(ReactApplicationContext reactContext) {
    super(reactContext);
    new Prefs(reactContext);
    ServiceBuilder.init(Constants.BASE_URL, reactContext);
    registerMessageHandler();

    mixpanel = MixpanelAPI.getInstance(reactContext, Constants.MIXPANEL_TOKEN);
    mixpanel.identify(Prefs.getAccountId());
    mixpanel.getPeople().identify(Prefs.getAccountId());
  }

  @Override public String getName() {
    return "HumaniqProfileApiLib";
  }

  @Override public Map<String, Object> getConstants() {
    return new HashMap<>();
  }

  @ReactMethod public void getBalance(final String id, final Promise promise) {

    final JSONObject props = new JSONObject();
    try {
      props.put("name", "getBalance");
      props.put("request api", "/wallet/api/v1/users/" + id + "/balance");
    } catch (JSONException e) {
      e.printStackTrace();
    }

    mixpanel.track("request", props);

    Log.d(LOG_TAG, "Balance Request: " + "id: " + id);
    ServiceBuilder.getWalletService().
        getUserBalance(id).enqueue(new retrofit2.Callback<BaseResponse<Balance>>() {
      @Override public void onResponse(Call<BaseResponse<Balance>> call,
          Response<BaseResponse<Balance>> response) {
        Log.d(LOG_TAG, "Balance Response: " + response.isSuccessful());
        if (response.body() != null && !response.body().equals("")) {

          try {
            WritableMap addressState = ModelConverterUtils.
                convertJsonToMap(new JSONObject(new Gson().toJson(response.body().data)));
            addressState.putInt("code", 200);
            promise.resolve(addressState);
          } catch (JSONException e) {
            e.printStackTrace();
            promise.reject(e);
          }

          try {
            props.put("method", "getBalance");
            props.put("response", new Gson().toJson(response.body()));
            props.put("code", response.code());
            mixpanel.track("response", props);
          } catch (Exception e) {
            e.printStackTrace();
          }
        } else {
          switch (response.code()) {
            case 403:
            case 401: {
              WritableMap writableMap = new WritableNativeMap();
              writableMap.putInt("code", 401);
              promise.resolve(writableMap);
            }
            break;

            default:
              Log.d(LOG_TAG, "OnResponse - Error request");
              Log.d(LOG_TAG, response.errorBody().toString());
              try {
                promise.reject(String.valueOf(response.code()),
                    new Throwable(response.errorBody().string()));
              } catch (IOException e) {
                e.printStackTrace();
              }
              break;
          }

          try {
            props.put("method", "getBalance");
            props.put("response", response.errorBody().string());
            props.put("code", response.code());
            mixpanel.track("response", props);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }

      @Override public void onFailure(Call<BaseResponse<Balance>> call, Throwable t) {
        promise.reject(t);
        try {
          props.put("method", "getBalance");
          props.put("response", t.getMessage());
          props.put("code", ((HttpException) t).code());
          mixpanel.track("response", props);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  //@ReactMethod public void getUserTransaction(String account_id, String hash, final Promise promise) {
  //  ServiceBuilder
  //      .getWalletService()
  //      .getUserTransaction(account_id, hash)
  //      .enqueue(new Callback<BaseResponse<UserTransaction>>() {
  //        @Override public void onResponse(Call<BaseResponse<UserTransaction>> call,
  //            Response<BaseResponse<UserTransaction>> response) {
  //          try {
  //            WritableMap transactionMap = ModelConverterUtils
  //                .convertJsonToMap(new JSONObject(new Gson().toJson(response.body().data, UserTransaction.class)));
  //            promise.resolve(transactionMap);
  //          } catch (JSONException e) {
  //            e.printStackTrace();
  //          }
  //
  //        }
  //
  //        @Override public void onFailure(Call<BaseResponse<UserTransaction>> call, Throwable t) {
  //
  //        }
  //      });
  //}

  private void registerMessageHandler() {
    IntentFilter intentFilter = new IntentFilter("com.humaniq.apilib.fcm.ReceiveNotification");

    getReactApplicationContext().registerReceiver(new BroadcastReceiver() {
      @Override public void onReceive(Context context, final Intent intent) {
        //String data = "";
        RemoteMessage remoteMessage = intent.getParcelableExtra("data");
        if (remoteMessage != null) {
          //for (String key : remoteMessage.getData().keySet()) {
          //  data += key + ": " + remoteMessage.getData().get(key) + ", ";
          //}

          /*
          * "push_data: type: receipt, transaction_id:
          * {"status": "failed", "error": "not enough hmq", "error_code": 13,
          * "message": "transaction_receipt", "transaction_id": "fe618eee-4bf0-4982-8031-8a3bf9b4042a",
           * "from_user": 1571923392783714240}, "__proto__: Object
Profile.js:110 Object {push: "push_data: type: receipt, transaction_id: {"status…c1d1b6a5b31", "from_user": 1571923392783714240}, "}
*/
          if ("receipt".equals(remoteMessage.getData().get("type"))) {
            WritableMap errorTransactionMap = new WritableNativeMap();
            errorTransactionMap.putString("error", remoteMessage.getData().get("info"));

            sendErrorEvent(errorTransactionMap);
          }

          if ("log".equals(remoteMessage.getData().get("type"))) {
            WritableMap writableMap = new WritableNativeMap();
            writableMap.putString("hash", remoteMessage.getData().get("hash"));
            sendEvent(writableMap);
          }
          //
          //  writableMap.putString("push", "push_data: " + data);

        } else {
          getReactApplicationContext().runOnUiQueueThread(new Runnable() {
            @Override public void run() {
              WritableMap writableMap = new WritableNativeMap();
              writableMap.putString("transaction", "push_not_work");
              sendEvent(writableMap);
            }
          });
        }
      }
    }, intentFilter);
  }

  @ReactMethod
  public void getUserTransaction(String accountId, String transactionHash, final Promise promise) {

    final JSONObject props = new JSONObject();
    try {
      props.put("name", "getUserTransaction");
      props.put("request api",
          "/wallet/api/v1/users/" + accountId + "/transactions/" + transactionHash);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    mixpanel.track("request", props);

    ServiceBuilder.getWalletService()
        .getUserTransaction(accountId, transactionHash)
        .enqueue(new Callback<BaseResponse<UserTransaction>>() {
          @Override public void onResponse(Call<BaseResponse<UserTransaction>> call,
              Response<BaseResponse<UserTransaction>> response) {
            if (response.body() != null) {

              try {
                WritableMap responseMap = ModelConverterUtils.convertJsonToMap(
                    new JSONObject(new Gson().toJson(response.body().data, UserTransaction.class)));
                responseMap.putInt("code", 200);
                promise.resolve(responseMap);
              } catch (JSONException e) {
                e.printStackTrace();
                promise.reject("", e);
              }

              try {
                props.put("method", "getUserTransaction");
                props.put("response", new Gson().toJson(response.body()));
                props.put("code", response.code());
                mixpanel.track("response", props);
              } catch (Exception e) {
                e.printStackTrace();
              }
            } else {
              switch (response.code()) {
                case 403:
                case 401: {
                  WritableMap writableMap = new WritableNativeMap();
                  writableMap.putInt("code", 401);
                  promise.resolve(writableMap);
                }
                break;

                default:
                  Log.d(LOG_TAG, "OnResponse - Error request");
                  Log.d(LOG_TAG, response.errorBody().toString());
                  try {
                    promise.reject(String.valueOf(response.code()),
                        new Throwable(response.errorBody().string()));
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                  break;
              }

              try {
                props.put("method", "getUserTransaction");
                props.put("response", response.errorBody().string());
                props.put("code", response.code());
                mixpanel.track("response", props);
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          }

          @Override public void onFailure(Call<BaseResponse<UserTransaction>> call, Throwable t) {
            promise.reject(t);

            try {
              props.put("method", "getUserTransaction");
              props.put("response", t.getMessage());
              props.put("code", ((HttpException) t).code());
              mixpanel.track("response", props);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
  }

  @ReactMethod public void showNotification() {

  }

  @ReactMethod
  public void getTransactions(String id, int offset, int limit, final Promise promise) {

    //Log.d(LOG_TAG,  "Transaction Request: id: " + id +
    //    ",  offset: " + offset + ", limit: " + limit);

    final JSONObject props = new JSONObject();
    try {
      props.put("name", "getTransactions");
      props.put("request api",
          "/wallet/api/v1/users/" + id + "/transactions?offset=" + offset + "&limit=" + limit);
    } catch (Exception e) {
      e.printStackTrace();
    }

    mixpanel.track("request", props);

    ServiceBuilder.getWalletService()
        .getUserTransactions(id, offset, limit)
        .enqueue(new retrofit2.Callback<BaseResponse<List<UserTransaction>>>() {
          @Override public void onResponse(Call<BaseResponse<List<UserTransaction>>> call,
              Response<BaseResponse<List<UserTransaction>>> response) {
            Log.d(LOG_TAG,
                "Transaction Response: " + response.isSuccessful() + ",body: " + response.body());
            if (response.body() != null) {

              try {
                props.put("method", "getTransactions");
                props.put("response", new Gson().toJson(response.body()));
                props.put("code", response.code());
                mixpanel.track("response", props);
              } catch (JSONException e) {
                e.printStackTrace();
              }

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
                promise.reject(e);
              }
            } else {
              switch (response.code()) {
                case 403:
                case 401: {
                  WritableMap writableMap = new WritableNativeMap();
                  writableMap.putInt("code", 401);
                  promise.resolve(writableMap);
                }

                break;

                default:
                  Log.d(LOG_TAG, "OnResponse - Error request");
                  Log.d(LOG_TAG, response.errorBody().toString());
                  try {
                    promise.reject(String.valueOf(response.code()),
                        new Throwable(response.errorBody().string()));
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                  break;
              }

              try {
                props.put("method", "getTransactions");
                props.put("response", response.errorBody().string());
                props.put("code", response.code());
                mixpanel.track("response", props);
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          }

          @Override
          public void onFailure(Call<BaseResponse<List<UserTransaction>>> call, Throwable t) {
            promise.reject(t);
            try {
              props.put("method", "getTransactions");
              props.put("response", t.getMessage());
              props.put("code", ((HttpException) t).code());
              mixpanel.track("response", props);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
  }

  @ReactMethod
  public void createTransaction(String fromUserId, String toUserId, String toAddress, float amount,
      final Promise promise) {

    final JSONObject props = new JSONObject();
    try {
      props.put("name", "createTransaction");
      props.put("request_api", "/wallet/api/v1/users/{user_id}/transactions"
          + "?user_id="
          + fromUserId
          + "&to_user_id="
          + toUserId
          + "&to_address="
          + toAddress
          + "&amount="
          + amount);
    } catch (Exception e) {
      e.printStackTrace();
    }

    mixpanel.track("request", props);

    ServiceBuilder.getWalletService()
        .createTransaction(fromUserId, toUserId, toAddress, amount)
        .enqueue(new Callback<BaseResponse<TransactionResponse>>() {
          @Override public void onResponse(Call<BaseResponse<TransactionResponse>> call,
              Response<BaseResponse<TransactionResponse>> response) {
            if (response.body() != null) {

              try {
                WritableMap createdTransaction = ModelConverterUtils.
                    convertJsonToMap(new JSONObject(
                        new Gson().toJson(response.body().data, TransactionResponse.class)));
                createdTransaction.putInt("code", 200);
                promise.resolve(createdTransaction);
              } catch (JSONException e) {
                e.printStackTrace();
              }

              try {
                props.put("method", "createTransaction");
                props.put("response", new Gson().toJson(response.body()));
                props.put("code", response.code());
                mixpanel.track("response", props);
              } catch (Exception e) {
                e.printStackTrace();
              }
            } else {
              switch (response.code()) {
                case 403:
                case 401: {
                  WritableMap writableMap = new WritableNativeMap();
                  writableMap.putInt("code", 401);
                  promise.resolve(writableMap);
                }
                break;

                default:
                  Log.d(LOG_TAG, "OnResponse - Error request");
                  Log.d(LOG_TAG, response.errorBody().toString());
                  try {
                    promise.reject(String.valueOf(response.code()),
                        new Throwable(response.errorBody().string()));
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                  break;
              }

              try {
                props.put("method", "createTransaction");
                props.put("response", response.errorBody().string());
                props.put("code", response.code());
                mixpanel.track("response", props);
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          }

          @Override
          public void onFailure(Call<BaseResponse<TransactionResponse>> call, Throwable t) {
            promise.reject(t);

            try {
              props.put("method", "createTransaction");
              props.put("response", t.getMessage());
              props.put("code", ((HttpException) t).code());
              mixpanel.track("response", props);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
  }

  @ReactMethod public void updateUserPerson(String accountId, String firstName, String lastName,
      final Promise promise) {

    AccountPerson accountPerson = new AccountPerson();
    accountPerson.setAccountId(accountId);
    AccountPerson.Person person = new AccountPerson.Person();
    person.setFirstName(firstName);
    person.setLastName(lastName);
    accountPerson.setPerson(person);
    final JSONObject props = new JSONObject();

    try {
      props.put("name", "updateUserPerson");
      props.put("request_api", "/tapatybe/api/v1/account/person");
      props.put("request_body", new Gson().toJson(accountPerson));
    } catch (Exception e) {
      e.printStackTrace();
    }

    mixpanel.track("request", props);

    ServiceBuilder.getProfileService()
        .updateAccountPerson(accountPerson)
        .enqueue(new Callback<BasePayload<AccountPerson>>() {
          @Override public void onResponse(Call<BasePayload<AccountPerson>> call,
              Response<BasePayload<AccountPerson>> response) {
            if (response != null
                && response.body() != null
                && response.body().code == Codes.ACCOUNT_PERSONAL_UPDATE_SUCCESS) {
              try {
                WritableMap writableMap = ModelConverterUtils.convertJsonToMap(
                    new JSONObject(new Gson().toJson(response.body())));
                writableMap.putInt("code", 200);
                promise.resolve(writableMap);
              } catch (JSONException e) {
                e.printStackTrace();
                promise.reject(e);
              }

              try {
                props.put("method", "updateUserPerson");
                props.put("response", new Gson().toJson(response.body()));
                props.put("code", response.code());
                mixpanel.track("response", props);
              } catch (Exception e) {
                e.printStackTrace();
              }
            } else if (response.errorBody() != null) {
              switch (response.code()) {
                case 403:
                case 401: {
                  WritableMap writableMap = new WritableNativeMap();
                  writableMap.putInt("code", 401);
                  promise.resolve(writableMap);
                }
                break;

                default:
                  Log.d(LOG_TAG, "OnResponse - Error request");
                  Log.d(LOG_TAG, response.errorBody().toString());
                  try {
                    promise.reject(String.valueOf(response.code()),
                        new Throwable(response.errorBody().string()));
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                  break;
              }
              try {
                props.put("method", "updateUserPerson");
                props.put("response", response.errorBody().string());
                props.put("code", response.code());
                mixpanel.track("response", props);
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          }

          @Override public void onFailure(Call<BasePayload<AccountPerson>> call, Throwable t) {
            promise.reject(t);

            try {
              props.put("method", "updateUserPerson");
              props.put("response", t.getMessage());
              props.put("code", ((HttpException) t).code());
              mixpanel.track("response", props);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
  }

  @ReactMethod
  public void uploadProfileAvatar(String accountId, String avatarBse64, final Promise promise) {

    AccountAvatar accountAvatar = new AccountAvatar();
    accountAvatar.setAccountId(accountId);
    accountAvatar.setFacialImage(avatarBse64);

    final JSONObject props = new JSONObject();
    try {
      props.put("name", "uploadProfileAvatar");
      props.put("request_api", "/tapatybe/api/v1/account/avatar");
      props.put("request_body", new Gson().toJson(accountAvatar));
    } catch (Exception e) {
      e.printStackTrace();
    }
    mixpanel.track("request", props);

    ServiceBuilder.getProfileService()
        .updateAccountAvatar(accountAvatar)
        .enqueue(new Callback<BasePayload<AccountAvatarResponse>>() {
          @Override public void onResponse(Call<BasePayload<AccountAvatarResponse>> call,
              Response<BasePayload<AccountAvatarResponse>> response) {
            WritableMap avatarRespone = null;
            if (response.body() != null) {
              try {
                avatarRespone = ModelConverterUtils.convertJsonToMap(new JSONObject(
                    new Gson().toJson(response.body().payload, AccountAvatarResponse.class)));
                avatarRespone.putInt("code", 5004);
                promise.resolve(avatarRespone);
              } catch (JSONException e) {
                e.printStackTrace();
              }

              try {
                props.put("method", "uploadProfileAvatar");
                props.put("response", new Gson().toJson(response.body()));
                props.put("code", response.code());
                mixpanel.track("response", props);
              } catch (Exception e) {
                e.printStackTrace();
              }
            } else if (response.errorBody() != null) {
              switch (response.code()) {
                case 403:
                case 401: {
                  WritableMap writableMap = new WritableNativeMap();
                  writableMap.putInt("code", 401);
                  promise.resolve(writableMap);
                }
                break;

                default:
                  WritableMap writableMap = new WritableNativeMap();
                  try {
                    writableMap.putString("message",
                        "NOT_UPLOADED! " + response.code() + ", MESSAGE: " + response.errorBody()
                            .string());
                    writableMap.putInt("code", 3013);
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                  promise.resolve(writableMap);
                  break;
              }
              try {
                props.put("method", "uploadProfileAvatar");
                props.put("response", response.errorBody().string());
                props.put("code", response.code());
                mixpanel.track("response", props);
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          }

          @Override
          public void onFailure(Call<BasePayload<AccountAvatarResponse>> call, Throwable t) {
            promise.reject(t);

            try {
              props.put("method", "uploadProfileAvatar");
              props.put("response", t.getMessage());
              props.put("code", ((HttpException) t).code());
              mixpanel.track("response", props);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
  }

  @ReactMethod
  public void changeProfilePassword(String accountId, String oldPassword, String newPassword,
      final Promise promise) {

    AccountPassword accountPassword = new AccountPassword();
    accountPassword.setAccountId(accountId);
    accountPassword.setOldPassword(oldPassword);
    accountPassword.setNewPassword(newPassword);

    final JSONObject props = new JSONObject();
    try {
      props.put("name", "changeProfilePassword");
      props.put("request_api", "tapatybe/api/v1/account/password");
      props.put("request_body", new Gson().toJson(accountPassword));
    } catch (Exception e) {
      e.printStackTrace();
    }
    mixpanel.track("request", props);

    ServiceBuilder.getProfileService()
        .updateAccountPassword(accountPassword)
        .enqueue(new Callback<BasePayload<Object>>() {
          @Override public void onResponse(Call<BasePayload<Object>> call,
              Response<BasePayload<Object>> response) {
            if (response != null
                && response.body() != null
                && response.body().code == Codes.ACCOUNT_PASSWORD_UPDATE_SUCCESS) {
              try {
                WritableMap writableMap = ModelConverterUtils.convertJsonToMap(
                    new JSONObject(new Gson().toJson(response.body())));
                writableMap.putInt("code", 200);

                promise.resolve(writableMap);
              } catch (JSONException e) {
                e.printStackTrace();
                promise.reject("", e);
              }

              try {
                props.put("method", "changeProfilePassword");
                props.put("response", new Gson().toJson(response.body()));
                props.put("code", response.code());
                mixpanel.track("response", props);
              } catch (Exception e) {
                e.printStackTrace();
              }
            } else if (response != null && response.errorBody() != null) {
              switch (response.code()) {
                case 403:
                case 401: {
                  WritableMap writableMap = new WritableNativeMap();
                  writableMap.putInt("code", 401);
                  promise.resolve(writableMap);
                }
                break;

                default:
                  Log.d(LOG_TAG, "OnResponse - Error request");
                  Log.d(LOG_TAG, response.errorBody().toString());
                  try {
                    promise.reject(String.valueOf(response.code()),
                        new Throwable(response.errorBody().string()));
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                  break;
              }

              try {
                props.put("method", "changeProfilePassword");
                props.put("response", response.errorBody().string());
                props.put("code", response.code());
                mixpanel.track("response", props);
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          }

          @Override public void onFailure(Call<BasePayload<Object>> call, Throwable t) {
            promise.reject(t);

            try {
              props.put("method", "changeProfilePassword");
              props.put("response", t.getMessage());
              props.put("code", ((HttpException) t).code());
              mixpanel.track("response", props);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
  }

  @ReactMethod public void getAccountProfile(String accountId, final Promise promise) {

    final JSONObject props = new JSONObject();
    try {
      props.put("name", "getAccountProfile");
      props.put("request_api", "/tapatybe/api/v1/account/profile?account_id" + accountId);
    } catch (Exception e) {
      e.printStackTrace();
    }
    mixpanel.track("request", props);

    ServiceBuilder.getProfileService()
        .getAccountProfile(accountId)
        .enqueue(new Callback<BasePayload<AccountProfile>>() {
          @Override public void onResponse(Call<BasePayload<AccountProfile>> call,
              Response<BasePayload<AccountProfile>> response) {
            if (response.body() != null
                && response.body().code == Codes.ACCOUNT_PROFILE_RETRIEVED) {
              try {
                WritableMap profile = ModelConverterUtils.convertJsonToMap(new JSONObject(
                    new Gson().toJson(response.body().payload, AccountProfile.class)));
                profile.putInt("code", 200);
                promise.resolve(profile);
              } catch (JSONException e) {
                e.printStackTrace();
                promise.reject(e);
              }

              try {
                props.put("method", "getAccountProfile");
                props.put("response", new Gson().toJson(response.body()));
                props.put("code", response.code());
                mixpanel.track("response", props);
              } catch (Exception e) {
                e.printStackTrace();
              }
            } else if (response.errorBody() != null) {
              switch (response.code()) {
                case 403:
                case 401: {
                  WritableMap writableMap = new WritableNativeMap();
                  writableMap.putInt("code", 401);
                  promise.resolve(writableMap);
                }
                break;

                default:
                  Log.d(LOG_TAG, "OnResponse - Error request");
                  Log.d(LOG_TAG, response.errorBody().toString());
                  try {
                    promise.reject(String.valueOf(response.code()),
                        new Throwable(response.errorBody().string()));
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                  break;
              }

              try {
                props.put("method", "getAccountProfile");
                props.put("response", response.errorBody().string());
                props.put("code", response.code());
                mixpanel.track("response", props);
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          }

          @Override public void onFailure(Call<BasePayload<AccountProfile>> call, Throwable t) {
            promise.reject(t);

            try {
              props.put("method", "getAccountProfile");
              props.put("response", t.getMessage());
              props.put("code", ((HttpException) t).code());
              mixpanel.track("response", props);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
  }

  @ReactMethod public void getAccountProfiles(ReadableArray accountIds, final Promise promise) {

    final JSONObject props = new JSONObject();
    try {
      props.put("name", "getAccountProfiles");
      props.put("request_api",
          "/tapatybe/api/v1/account/profiles?account_id" + convertReadableArrayToList(accountIds));
    } catch (Exception e) {
      e.printStackTrace();
    }
    mixpanel.track("request", props);

    ServiceBuilder.getProfileService()
        .getAccountProfiles(new ArrayList<String>(convertReadableArrayToList(accountIds)))
        .enqueue(new Callback<BasePayload<AccountProfile.List>>() {
          @Override public void onResponse(Call<BasePayload<AccountProfile.List>> call,
              Response<BasePayload<AccountProfile.List>> response) {
            if (response.body() != null) {
              try {
                WritableArray writableArray = new WritableNativeArray();
                for (AccountProfile accountProfile : response.body().payload.getAccountProfiles()) {
                  WritableMap profile = ModelConverterUtils.convertJsonToMap(
                      new JSONObject(new Gson().toJson(accountProfile, AccountProfile.class)));
                  writableArray.pushMap(profile);
                }

                promise.resolve(writableArray);
              } catch (JSONException e) {
                e.printStackTrace();
                promise.reject(e);
              }

              try {
                props.put("method", "getAccountProfiles");
                props.put("response", new Gson().toJson(response.body()));
                props.put("code", response.code());
                mixpanel.track("response", props);
              } catch (Exception e) {
                e.printStackTrace();
              }
            } else if (response.errorBody() != null) {
              switch (response.code()) {
                case 401: {
                  WritableMap writableMap = new WritableNativeMap();
                  writableMap.putInt("code", 401);
                  promise.resolve(writableMap);
                }
                break;

                default:
                  Log.d(LOG_TAG, "OnResponse - Error request");
                  Log.d(LOG_TAG, response.errorBody().toString());
                  try {
                    promise.reject(String.valueOf(response.code()),
                        new Throwable(response.errorBody().string()));
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                  break;
              }

              try {
                props.put("method", "getAccountProfiles");
                props.put("response", response.errorBody().string());
                props.put("code", response.code());
                mixpanel.track("response", props);
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          }

          @Override
          public void onFailure(Call<BasePayload<AccountProfile.List>> call, Throwable t) {
            promise.reject(t);

            try {
              props.put("method", "getAccountProfiles");
              props.put("response", t.getMessage());
              props.put("code", ((HttpException) t).code());
              mixpanel.track("response", props);
            } catch (Exception e) {
              e.printStackTrace();
            }
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

  private void sendErrorEvent(@Nullable WritableMap params) {
    this.getReactApplicationContext().
        getJSModule(DeviceEventManagerModule.
            RCTDeviceEventEmitter.class).
        emit(Constants.EVENT_TRANSACTION_ERROR, params);
  }

  @ReactMethod public void deauthenticateUser(String accountId, final Promise promise) {

    final JSONObject props = new JSONObject();
    try {
      props.put("name", "deauthenticateUser");
      props.put("request_api", "/tapatybe/api/v1/deauthenticate/user");
      props.put("request_body", new Gson().toJson(new UserId(accountId)));
    } catch (Exception e) {
      e.printStackTrace();
    }
    mixpanel.track("request", props);

    ServiceBuilder.getProfileService()
        .deauthenticateUser(new UserId(accountId))
        .enqueue(new Callback<DeauthModel>() {
          @Override public void onResponse(Call<DeauthModel> call, Response<DeauthModel> response) {
            if (response.body() != null && !response.body().equals("")) {
              //right request, error responsev
              WritableMap writableMap = new WritableNativeMap();
              writableMap.putInt("code", 200);

              promise.resolve(writableMap);

              try {
                props.put("method", "deauthenticateUser");
                props.put("response", new Gson().toJson(response.body()));
                props.put("code", response.code());
                mixpanel.track("response", props);
              } catch (Exception e) {
                e.printStackTrace();
              }
            } else {
              Converter<ResponseBody, DeauthErrorModel> errorConverter =
                  ServiceBuilder.getRetrofit()
                      .responseBodyConverter(DeauthErrorModel.class, new Annotation[0]);
              try {
                DeauthErrorModel model = errorConverter.convert(response.errorBody());
                Log.d(LOG_TAG, "error code = " + model.getCode());
                Log.d(LOG_TAG, "error message = " + model.getMessage());
                promise.reject(Integer.toString(model.getCode()), model.getMessage());
              } catch (IOException e) {
                e.printStackTrace();
                promise.reject(e);
              }

              try {
                props.put("method", "deauthenticateUser");
                props.put("response", response.errorBody().string());
                props.put("code", response.code());
                mixpanel.track("response", props);
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          }

          @Override public void onFailure(Call<DeauthModel> call, Throwable t) {
            promise.reject(t);

            try {
              props.put("method", "uploadProfileAvatar");
              props.put("response", t.getMessage());
              props.put("code", ((HttpException) t).code());
              mixpanel.track("response", props);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
  }

  private Set<String> convertReadableArrayToList(ReadableArray array) {
    Set<String> set = new HashSet<String>();

    for (int i = 0; i < array.size(); i++) {
      set.add(array.getString(i));
    }

    return set;
  }

  @ReactMethod public void getExchangeHmq(String amount, final Promise promise) {

    final JSONObject props = new JSONObject();
    try {
      props.put("name", "getExchangeHmq");
      props.put("request_api", "/currency/api/v1/usd_exchange?amount=" + amount);
    } catch (Exception e) {
      e.printStackTrace();
    }
    mixpanel.track("request", props);

    ServiceBuilder.getWalletService()
        .getExchangeHmq(amount)
        .enqueue(new Callback<BaseResponse<ExchangeModelHmq>>() {
          @Override public void onResponse(Call<BaseResponse<ExchangeModelHmq>> call,
              Response<BaseResponse<ExchangeModelHmq>> response) {
            if (response.body() != null && !"".equals(response.body())) {
              Log.d(LOG_TAG, "OnResponse - Success request");
              try {
                ExchangeModelHmq resp = response.body().data;

                WritableMap writableMap =
                    ModelConverterUtils.convertJsonToMap(new JSONObject(new Gson().toJson(resp)));
                writableMap.putInt("code", 200);
                promise.resolve(writableMap);
              } catch (JSONException e) {
                e.printStackTrace();
                promise.reject(e);
              }

              try {
                props.put("method", "getExchangeHmq");
                props.put("response", new Gson().toJson(response.body()));
                props.put("code", response.code());
                mixpanel.track("response", props);
              } catch (Exception e) {
                e.printStackTrace();
              }
            } else {
              switch (response.code()) {
                case 403:
                case 401: {
                  WritableMap writableMap = new WritableNativeMap();
                  writableMap.putInt("code", 401);
                  promise.resolve(writableMap);
                }
                break;

                default:
                  Log.d(LOG_TAG, "OnResponse - Error request");
                  Log.d(LOG_TAG, response.errorBody().toString());
                  try {
                    promise.reject(String.valueOf(response.code()),
                        new Throwable(response.errorBody().string()));
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                  break;
              }

              try {
                props.put("method", "getExchangeHmq");
                props.put("response", response.errorBody().string());
                props.put("code", response.code());
                mixpanel.track("response", props);
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          }

          @Override public void onFailure(Call<BaseResponse<ExchangeModelHmq>> call, Throwable t) {
            promise.reject(t);
            try {
              props.put("method", "getExchangeHmq");
              props.put("response", t.getMessage());
              props.put("code", ((HttpException) t).code());
              mixpanel.track("response", props);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
  }

  @ReactMethod public void getExchangeUsd(String amount, final Promise promise) {

    final JSONObject props = new JSONObject();
    try {
      props.put("name", "getExchangeUsd");
      props.put("request_api", "/currency/api/v1/hmq_exchange?amount=" + amount);
    } catch (Exception e) {
      e.printStackTrace();
    }
    mixpanel.track("request", props);

    ServiceBuilder.getWalletService()
        .getExchangeUsd(amount)
        .enqueue(new Callback<BaseResponse<ExchangeModelUsd>>() {
          @Override public void onResponse(Call<BaseResponse<ExchangeModelUsd>> call,
              Response<BaseResponse<ExchangeModelUsd>> response) {
            if (response.body() != null && !"".equals(response.body())) {
              Log.d(LOG_TAG, "OnResponse - Success request");
              try {
                ExchangeModelUsd resp = response.body().data;

                WritableMap writableMap =
                    ModelConverterUtils.convertJsonToMap(new JSONObject(new Gson().toJson(resp)));
                writableMap.putInt("code", 200);
                promise.resolve(writableMap);
              } catch (JSONException e) {
                e.printStackTrace();
                promise.reject(e);
              }
              try {
                props.put("method", "getExchangeUsd");
                props.put("response", new Gson().toJson(response.body()));
                props.put("code", response.code());
                mixpanel.track("response", props);
              } catch (Exception e) {
                e.printStackTrace();
              }
            } else {
              switch (response.code()) {
                case 403:
                case 401: {
                  WritableMap writableMap = new WritableNativeMap();
                  writableMap.putInt("code", 401);
                  promise.resolve(writableMap);
                }
                break;

                default:
                  Log.d(LOG_TAG, "OnResponse - Error request");
                  Log.d(LOG_TAG, response.errorBody().toString());
                  try {
                    promise.reject(String.valueOf(response.code()),
                        new Throwable(response.errorBody().string()));
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                  break;
              }

              try {
                props.put("method", "getExchangeUsd");
                props.put("response", response.errorBody().string());
                props.put("code", response.code());
                mixpanel.track("response", props);
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          }

          @Override public void onFailure(Call<BaseResponse<ExchangeModelUsd>> call, Throwable t) {
            promise.reject(t);

            try {
              props.put("method", "getExchangeUsd");
              props.put("response", t.getMessage());
              props.put("code", ((HttpException) t).code());
              mixpanel.track("response", props);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
  }
}