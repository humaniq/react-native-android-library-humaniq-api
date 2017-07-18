package com.humaniq.apilib;

import android.util.Log;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.google.gson.Gson;
import com.humaniq.apilib.constructor.ModelConverterUtils;
import com.humaniq.apilib.models.contacts.response.ContactsResponse;
import com.humaniq.apilib.services.restService.ServiceBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gritsay on 7/17/17.
 */

public class ContactsModule extends ReactContextBaseJavaModule {
    public ContactsModule(ReactApplicationContext reactContext) {
        super(reactContext);
        ServiceBuilder.init(C.CONTACTS_BASE_URL);
    }

    @Override
    public String getName() {
        return "HumaniqContactsApiLib";
    }

    @ReactMethod
    public void extractPhoneNumbers (final Promise promise) {
          ArrayList<String> arrayList = new ArrayList<String>();
          arrayList.add("+79620505555");
           ServiceBuilder.getContactsService().extractPhoneNumbers(arrayList).enqueue(new Callback<ContactsResponse>() {
              @Override
              public void onResponse(Call<ContactsResponse> call, Response<ContactsResponse> response) {
                  if (!response.isSuccessful()) {
                      Log.d("MainActivity","error");
                      Log.d("MainActivity",response.errorBody().toString());

                  } else {
                      Log.d("MainActivity","not error");
                      ContactsResponse res = response.body();
                      Log.d("MainActivity", Integer.toString(res.getData().size()));
                      Gson gson = new Gson();
                      String jsonString = gson.toJson(res);
                      try {
                          JSONObject jsonObject = new JSONObject(jsonString);
                          promise.resolve(ModelConverterUtils.convertJsonToMap(jsonObject));
                      } catch (JSONException e) {
                          e.printStackTrace();
                          promise.reject(e);
                      }
                  }
              }
              @Override
              public void onFailure(Call<ContactsResponse> call, Throwable t) {
                  Log.d("MainActivity","error = "+t);
              }
          });


      }
//    public void extractPhoneNumbers (final Promise promise) {
//        ArrayList<String> phoneNumbers = new ArrayList<String>();
//
//        //stub
//        phoneNumbers.add("+79620505555");
//        phoneNumbers.add("+79620505556");
//
//        ServiceBuilder.getContactsService().extractPhoneNumbers(phoneNumbers).enqueue(new retrofit2.Callback<ContactsResponse>() {
//            @Override
//            public void onResponse(Call<ContactsResponse> call, Response<ContactsResponse> response) {
//                ContactsResponse contacts =  response.body();
//                Gson gson = new Gson();
//                Log.d("!!!!", response.body().toString());
//                String jsonString = gson.toJson(contacts.getData());
//                try {
//                    JSONObject jsonObject = new JSONObject(jsonString);
//
//                    //return promise object
//                    promise.resolve(ModelConverterUtils.convertJsonToMap(jsonObject));
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    promise.reject(e);
//                }
//
//                //WritableMap collectionTransaction1 = ModelConverterUtils.convertJsonToMap()
//            }
//
//            @Override
//            public void onFailure(Call<ContactsResponse> call, Throwable t) {
//                promise.reject(t);
//            }
//        });
//    }


}
