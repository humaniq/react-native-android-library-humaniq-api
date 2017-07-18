package com.humaniq.apilib;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

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
    private final String LOG_TAG = "ContactsModule";
    public ContactsModule(ReactApplicationContext reactContext) {
        super(reactContext);
        ServiceBuilder.init(C.CONTACTS_BASE_URL);
    }

    @Override
    public String getName() {
        return "HumaniqContactsApiLib";
    }

    private ArrayList<String> getAllContacts() {
        ArrayList<String> result = null;

        Log.d(LOG_TAG, "getContact()");
        ContentResolver cr = getReactApplicationContext().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cur.getCount() > 0) {
            result = new ArrayList<String>();
            Log.d(LOG_TAG,"many contacts");
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
//                String name = cur.getString(cur.getColumnIndex(
//                        ContactsContract.Contacts.DISPLAY_NAME));
                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        result.add(phoneNo);
                    }
                    pCur.close();
                }
            }
        } else Log.d(LOG_TAG, "No contacts");

        return result;
    }

    @ReactMethod
    public void extractPhoneNumbers(final Promise promise) {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("+79620505555");
        ServiceBuilder.getContactsService().extractPhoneNumbers(getAllContacts()).enqueue(new Callback<ContactsResponse>() {
            @Override
            public void onResponse(Call<ContactsResponse> call, Response<ContactsResponse> response) {
                if (!response.isSuccessful()) {
                    Log.d(LOG_TAG, "OnResponse - Error request");
                    Log.d(LOG_TAG, response.errorBody().toString());

                } else {
                    Log.d(LOG_TAG, "OnResponse - Success request");
                    ContactsResponse res = response.body();


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
                Log.d(LOG_TAG, "onFailure = " + t);
            }
        });
    }

}
