package com.humaniq.apilib.react;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
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
import com.humaniq.apilib.network.models.request.wallet.UserTransaction;
import com.humaniq.apilib.network.models.response.contacts.Contact;
import com.humaniq.apilib.storage.Prefs;
import com.humaniq.apilib.utils.ModelConverterUtils;
import com.humaniq.apilib.network.models.response.contacts.ContactsResponse;
import com.humaniq.apilib.network.service.providerApi.ServiceBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.System.in;

/**
 * Created by gritsay on 7/17/17.
 */

public class ContactsModule extends ReactContextBaseJavaModule {
  private final String LOG_TAG = "ContactsModule";
  MyContentObserver contentObserver = new MyContentObserver();

  public ContactsModule(ReactApplicationContext reactContext) {
    super(reactContext);
    new Prefs(reactContext);
    ServiceBuilder.init(Constants.CONTACTS_BASE_URL, reactContext);
  }

  


  @Override public String getName() {
    return "HumaniqContactsApiLib";
  }

  private List<String> getAllContacts() {
    ArrayList<String> result = null;

    Log.d(LOG_TAG, "getContact()");
    ContentResolver cr = getReactApplicationContext().getContentResolver();
    Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

    if (cur.getCount() > 0) {
      result = new ArrayList<String>();
      Log.d(LOG_TAG, "many contacts");
      while (cur.moveToNext()) {
        String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
        //                String name = cur.getString(cur.getColumnIndex(
        //                        ContactsContract.Contacts.DISPLAY_NAME));
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

    return result;
  }


  @ReactMethod public void extractSinglePhoneNumber(String phonenumber, final Promise promise) {
    if (phonenumber!= null && !phonenumber.isEmpty()) {

      ArrayList<String> phone = new ArrayList<String>();
      phone.add(phonenumber);
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
          }

        }

        @Override public void onFailure(Call<ContactsResponse> call, Throwable t) {
          Log.d(LOG_TAG, "onFailure = " + t);
        }
      });
    } else {
      promise.reject("-1", "Haven't any contacts on mobile device");
    }
  }

  @ReactMethod public void extractAllPhoneNumbers(final Promise promise) {
    if (getAllContacts() != null) {
      ServiceBuilder.getContactsService().extractPhoneNumbers(getAllContacts()).enqueue(new Callback<ContactsResponse>() {
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
          }

        }

        @Override public void onFailure(Call<ContactsResponse> call, Throwable t) {
          Log.d(LOG_TAG, "onFailure = " + t);
        }
      });
    } else {
      promise.reject("-1", "Haven't any contacts on mobile device");
    }
  }

  public void synchronizePhoneNumber(ArrayList<String> contact) {
    ServiceBuilder.getContactsService()
        .extractPhoneNumbers(contact)
        .enqueue(new Callback<ContactsResponse>() {
          @Override
          public void onResponse(Call<ContactsResponse> call, Response<ContactsResponse> response) {
            if (!response.isSuccessful()) {
              Log.d(LOG_TAG, "OnResponse - Error request");
              Log.d(LOG_TAG, response.errorBody().toString());
            } else {
              Log.d(LOG_TAG, "OnResponse - Success request");
            }
          }

          @Override public void onFailure(Call<ContactsResponse> call, Throwable t) {
            Log.d(LOG_TAG, "onFailure = " + t);
          }
        });
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
