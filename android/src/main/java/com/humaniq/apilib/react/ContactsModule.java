package com.humaniq.apilib.react;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
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
import com.humaniq.apilib.network.models.response.contacts.Contact;
import com.humaniq.apilib.storage.Prefs;
import com.humaniq.apilib.utils.ModelConverterUtils;
import com.humaniq.apilib.network.models.response.contacts.ContactsResponse;
import com.humaniq.apilib.network.service.providerApi.ServiceBuilder;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by gritsay on 7/17/17.
 */

public class ContactsModule extends ReactContextBaseJavaModule {
  private final String LOG_TAG = "ContactsModule";
  private final MixpanelAPI mixpanel;
  MyContentObserver contentObserver = new MyContentObserver();

  public ContactsModule(ReactApplicationContext reactContext) {
    super(reactContext);
    new Prefs(reactContext);
    ServiceBuilder.init(Constants.BASE_URL, reactContext);

    mixpanel = MixpanelAPI.getInstance(reactContext, Constants.MIXPANEL_TOKEN);
    mixpanel.identify(Prefs.getAccountId());
    mixpanel.alias(Prefs.getAccountId(), null);
    mixpanel.getPeople().identify(mixpanel.getDistinctId());
  }

  @Override public String getName() {
    return "HumaniqContactsApiLib";
  }

  @ReactMethod public void extractSinglePhoneNumber(String phonenumber, final Promise promise) {
    if (phonenumber!= null && !phonenumber.isEmpty()) {



      ArrayList<String> phone = new ArrayList<String>();
      phone.add(phonenumber);

      final JSONObject props = new JSONObject();
      try {
        props.put("name", "extractSinglePhoneNumber");
        props.put("request api", "/contact-checker/api/v1/extract_registered_phone_numbers");
        props.put("request_body", phone.toArray().toString());
      } catch (Exception e) {
        e.printStackTrace();
      }

      ServiceBuilder.getContactsService().extractPhoneNumbers(phone).enqueue(new Callback<ContactsResponse>() {
        @Override public void onResponse(Call<ContactsResponse> call, Response<ContactsResponse> response) {
          if (response.body() != null && !"".equals(response.body()))  {
            Log.d(LOG_TAG, "OnResponse - Success request");
            try {
              ContactsResponse res = response.body();
              WritableArray array = new WritableNativeArray();
              for (Contact contact : res.getData()) {
                WritableMap phonesWithId =  ModelConverterUtils.
                    convertJsonToMap(new JSONObject(new Gson().toJson(contact, Contact.class)));
                array.pushMap(phonesWithId);
              }
              promise.resolve(array);

              try {
                props.put("method", "extractSinglePhoneNumber");
                props.put("response", new Gson().toJson(response.body()));
                props.put("code", response.code());
                mixpanel.track("extractSinglePhoneNumber", props);
              } catch (Exception e) {
                e.printStackTrace();
              }
            } catch (JSONException e) {
              e.printStackTrace();
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
              props.put("method", "extractSinglePhoneNumber");
              props.put("response", response.errorBody().string());
              props.put("code", response.code());
              mixpanel.track("extractSinglePhoneNumber", props);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }

        }

        @Override public void onFailure(Call<ContactsResponse> call, Throwable t) {
          Log.d(LOG_TAG, "onFailure = " + t);

          try {
            props.put("method", "extractSinglePhoneNumber");
            props.put("response", t.getMessage());
            props.put("code", ((HttpException) t).code());
            mixpanel.track("extractSinglePhoneNumber", props);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });
    } else {
      promise.reject("-1", "Haven't any contacts on mobile device");
    }
  }

  @ReactMethod public void extractAllPhoneNumbers(final Promise promise) {
    new ContactsQueryAsync(promise)
        .execute();
  }

  public void synchronizePhoneNumber(ArrayList<String> contact) {
    final JSONObject props = new JSONObject();
    try {
      props.put("name", "synchronizePhoneNumber");
      props.put("request api", "/contact-checker/api/v1/extract_registered_phone_numbers");
      props.put("request_body", contact.toArray().toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
    ServiceBuilder.getContactsService()
        .extractPhoneNumbers(contact)
        .enqueue(new Callback<ContactsResponse>() {
          @Override
          public void onResponse(Call<ContactsResponse> call, Response<ContactsResponse> response) {
            if (!response.isSuccessful()) {
              Log.d(LOG_TAG, "OnResponse - Error request");
              Log.d(LOG_TAG, response.errorBody().toString());

              try {
                props.put("method", "synchronizePhoneNumber");
                props.put("response", new Gson().toJson(response.body()));
                props.put("code", response.code());
                mixpanel.track("synchronizePhoneNumber", props);
              } catch (Exception e) {
                e.printStackTrace();
              }
            } else {

              try {
                props.put("method", "synchronizePhoneNumber");
                props.put("response", response.errorBody().string());
                props.put("code", response.code());
                mixpanel.track("synchronizePhoneNumber", props);
              } catch (Exception e) {
                e.printStackTrace();
              }
              Log.d(LOG_TAG, "OnResponse - Success request");
            }
          }

          @Override public void onFailure(Call<ContactsResponse> call, Throwable t) {
            Log.d(LOG_TAG, "onFailure = " + t);

            try {
              props.put("method", "synchronizePhoneNumber");
              props.put("response", t.getMessage());
              props.put("code", ((HttpException) t).code());
              mixpanel.track("synchronizePhoneNumber", props);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
  }

  private class ContactsQueryAsync extends AsyncTask<Void, Void, Response<ContactsResponse>> {
    private final Promise promise;

    public ContactsQueryAsync(Promise promise) {
      this.promise = promise;
    }

    @Override protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override protected Response<ContactsResponse> doInBackground(Void... voids) {


      ArrayList<String> result = null;

      Log.d(LOG_TAG, "getContact()");
      ContentResolver cr = getReactApplicationContext().getContentResolver();
      Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

      if (cur.getCount() > 0) {
        result = new ArrayList<String>();
        Log.d(LOG_TAG, "many contacts");
        while (cur.moveToNext()) {
          String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
          if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
            Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);
            while (pCur.moveToNext()) {
              String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
              result.add(phoneNo);
            }
            pCur.close();
          }
        }
      } else {
        Log.d(LOG_TAG, "No contacts");
      }

      if(result == null) {
          return null;
      }

      Response<ContactsResponse> response = null;
      Call<ContactsResponse> call = ServiceBuilder.getContactsService().extractPhoneNumbers(result);
        try {
          response = call.execute();
        } catch (IOException e) {
          e.printStackTrace();
        }

        return response;
    }

    @Override protected void onPostExecute(Response<ContactsResponse> response) {
      super.onPostExecute(response);
      if(response != null) {
        switch (response.code()) {
          case 200:
          case 201:
          case 202:
          case 203: {
            if (response.body() != null && !"".equals(response.body())) {
              Log.d(LOG_TAG, "OnResponse - Success request");
              try {
                ContactsResponse res = response.body();
                WritableArray array = new WritableNativeArray();
                for (Contact contact : res.getData()) {
                  WritableMap phonesWithId = ModelConverterUtils.
                      convertJsonToMap(new JSONObject(new Gson().toJson(contact, Contact.class)));
                  array.pushMap(phonesWithId);
                }
                promise.resolve(array);
              } catch (JSONException e) {
                e.printStackTrace();
                promise.reject(e);
              }
            }
          }
          break;

          case 403:
          case 401:
            WritableMap writableMap = new WritableNativeMap();
            writableMap.putInt("code", 401);
            promise.resolve(writableMap);
            break;
          default:
            try {
              promise.reject(String.valueOf(response.code()),
                  new Throwable(response.errorBody().string()));
            } catch (IOException e) {
              e.printStackTrace();
            }
            break;
        }
      } else {
        promise.reject("-1", "Haven't any contacts on mobile device");
      }
    }
  }

  private class MyContentObserver extends ContentObserver {

    public MyContentObserver() {
      super(null);
    }

    @Override public void onChange(boolean selfChange, Uri uri) {
      ArrayList<String> phoneNumber = new ArrayList<>();
      super.onChange(selfChange, uri);
      Log.d(LOG_TAG, "Contact has changed");

      Cursor cursor = getReactApplicationContext().getContentResolver()
          .query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

      if (!cursor.moveToFirst()) return;

      cursor.moveToLast();
      String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
      String phone =
          cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
      Log.d(LOG_TAG, "id = " + name + "\n name = " + name);
      phoneNumber.add(phone);
      synchronizePhoneNumber(phoneNumber);
    }
  }
}
