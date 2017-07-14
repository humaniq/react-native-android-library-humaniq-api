package com.humaniq.apilib;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.humaniq.apilib.constructor.ModelConverterUtils;
import com.humaniq.apilib.services.ServiceBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/*
*
* import HumaniqApiLib from 'react-native-android-library-humaniq-api'
import HumaniqProfileApiLib from 'react-native-android-library-humaniq-api'
* HumaniqProfileApiLib.getTransactions('0x1111111111111111111111111111111111111111').then((addressState) => {
      // console.log(array);
      console.warn(JSON.stringify(addressState))
        //HumaniqApiLib.show(addressState.address);
    });


        HumaniqApiLib.getTransactions('0x1111111111111111111111111111111111111111').then((array) => {
          // console.log(array);
          console.warn(JSON.stringify(array))
            // HumaniqApiLib.show(array[0].name);
        });*/
public class ProfileModule extends ReactContextBaseJavaModule {

    public ProfileModule(ReactApplicationContext reactContext) {
        super(reactContext);
        ServiceBuilder.init();
    }

    @Override
    public String getName() {
        return "HumaniqProfileApiLib";
    }

    @Override
    public Map<String, Object> getConstants() {
        return new HashMap<>();
    }

    // TODO user real api instead of MOCK!!!

    @ReactMethod
    public void getAddressState(String id, final Promise promise) {
        // MOCK data!
        try {
            WritableMap balanceMock =
                    ModelConverterUtils.convertJsonToMap(new JSONObject(" {" +
                            "    \"HMQ\": 190.6," +
                            "    \"USD\": 22.62422" +
                            "  }"));

            promise.resolve(balanceMock);

        } catch (JSONException e) {
            e.printStackTrace();
        }

//    ServiceBuilder.getProfileService().
//            getAddressState(id)
//            .enqueue(new retrofit2.Callback<BaseResponse<AddressState>>() {
//              @Override
//              public void onResponse(Call<BaseResponse<AddressState>> call,
//                                     Response<BaseResponse<AddressState>> response) {
//                try {
//                  WritableMap addressState =
//                          ModelConverterUtils.convertJsonToMap(new JSONObject(new Gson().toJson(response.body().data)));
//
//                  promise.resolve(addressState);
//
//                } catch (JSONException e) {
//                  e.printStackTrace();
//                }
//              }
//
//              @Override
//              public void onFailure(Call<BaseResponse<AddressState>> call,
//                                    Throwable t) {
//                promise.reject(t);
//              }
//            });

    }

    // TODO user real api instead of MOCK!!!
    @ReactMethod
    public void getTransactions(String id, final Promise promise) {
        // MOCK data
        WritableArray writableArray = new WritableNativeArray();
        try {

            WritableMap collectionTransaction1 = ModelConverterUtils.convertJsonToMap(new JSONObject("{" +
                    "        phone: \"+1 (416) 464 71 35\"," +
                    "        amount: \"+12.08\"," +
                    "        type: 0," +
                    "        name: \"Антон\"," +
                    "        surname: \"Петров\"," +
                    "        pic: \"http://www.gstatic.com/webp/gallery/5.jpg\"," +
                    "        time: '15.07.2017'," +
                    "        currency: 'HMQ'" +
                    "    }"));

            WritableMap collectionTransaction2 = ModelConverterUtils.convertJsonToMap(new JSONObject("{" +
                    "        phone: \"+1 (646) 883 99 11\"," +
                    "        amount: \"+30.01\"," +
                    "        type: 1," +
                    "        name: \"Сергей\"," +
                    "        surname: \"Павлов\"," +
                    "        pic: \"http://www.gstatic.com/webp/gallery/2.jpg\"," +
                    "        time: '15.07.2017'," +
                    "        currency: 'HMQ'" +
                    "    }"));

            WritableMap collectionTransaction3 = ModelConverterUtils.convertJsonToMap(new JSONObject("{" +
                    "        phone: \"+1 (707) 715 55 11\"," +
                    "        amount: \"+20.44\"," +
                    "        type: 0," +
                    "        name: \"Анатолий\"," +
                    "        surname: \"Романов\"," +
                    "        pic: \"http://www.gstatic.com/webp/gallery/5.jpg\"," +
                    "        time: '15.07.2017'," +
                    "        currency: 'HMQ'" +
                    "    }"));

            WritableMap collectionTransaction4 = ModelConverterUtils.convertJsonToMap(new JSONObject("{" +
                    "        phone: \"+1 (808) 890 55 11\"," +
                    "        amount: \"+45.09\"," +
                    "        type: 0," +
                    "        name: \"Анатолий\"," +
                    "        surname: \"Романов\"," +
                    "        pic: \"http://www.gstatic.com/webp/gallery/2.jpg\"," +
                    "        time: '16.07.2017'," +
                    "        currency: 'HMQ'" +
                    "    }"));

            WritableMap collectionTransaction5 = ModelConverterUtils.convertJsonToMap(new JSONObject("{" +
                    "        phone: \"+1 (808) 890 55 11\"," +
                    "        amount: \"+99.09\"," +
                    "        type: 0," +
                    "        name: \"Иван\"," +
                    "        surname: \"Сергеев\"," +
                    "        pic: \"http://www.gstatic.com/webp/gallery/5.jpg\"," +
                    "        time: '16.07.2017'," +
                    "        currency: 'HMQ'" +
                    "    }"));

            WritableMap collectionTransaction6 = ModelConverterUtils.convertJsonToMap(new JSONObject("{" +
                    "        phone: \"+1 (678) 890 55 34\"," +
                    "        amount: \"-9.09\"," +
                    "        type: 1," +
                    "        name: \"Кирил\"," +
                    "        surname: \"Романенко\"," +
                    "        pic: \"http://www.gstatic.com/webp/gallery/5.jpg\"," +
                    "        time: '17.07.2017'," +
                    "        currency: 'HMQ'" +
                    "    }"));

            WritableMap collectionTransaction7 = ModelConverterUtils.convertJsonToMap(new JSONObject("{" +
                    "        phone: \"+1 (678) 890 55 34\"," +
                    "        amount: \"-31.00\"," +
                    "        type: 0," +
                    "        name: \"Анастасия\"," +
                    "        surname: \"Минеева\"," +
                    "        pic: \"http://www.gstatic.com/webp/gallery/5.jpg\"," +
                    "        time: '17.07.2017'," +
                    "        currency: 'HMQ'" +
                    "    }"));

            WritableMap collectionTransaction8 = ModelConverterUtils.convertJsonToMap(new JSONObject("{" +
                    "        phone: \"+1 (678) 890 55 34\"," +
                    "        amount: \"-20.01\"," +
                    "        type: 1," +
                    "        name: \"Мария\"," +
                    "        surname: \"Коржун\"," +
                    "        pic: \"http://www.gstatic.com/webp/gallery/5.jpg\"," +
                    "        time: '17.07.2017'," +
                    "        currency: 'HMQ'" +
                    "    }"));

            writableArray.pushMap(collectionTransaction6);
            writableArray.pushMap(collectionTransaction7);
            writableArray.pushMap(collectionTransaction8);
            writableArray.pushMap(collectionTransaction4);
            writableArray.pushMap(collectionTransaction5);
            writableArray.pushMap(collectionTransaction1);
            writableArray.pushMap(collectionTransaction2);
            writableArray.pushMap(collectionTransaction3);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        promise.resolve(writableArray);
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
//                    WritableMap collectionTransaction = ModelConverterUtils.convertJsonToMap(new JSONObject(transaction.toJsonString()));
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