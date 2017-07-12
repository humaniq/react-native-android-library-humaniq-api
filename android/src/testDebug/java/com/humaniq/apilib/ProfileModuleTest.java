package com.humaniq.apilib;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.humaniq.apilib.constructor.ModelUtils;
import com.humaniq.apilib.models.Transaction;
import com.humaniq.apilib.models.response.BaseResponse;
import com.humaniq.apilib.services.ServiceBuilder;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ognev on 7/12/17.
 */
public class ProfileModuleTest extends TestCase {
    private CountDownLatch lock = new CountDownLatch(1);
    public void testGetTransactions() throws Exception {

        ServiceBuilder.init();
        ServiceBuilder.getProfileService().getAddressTransactions("0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")
                .enqueue(new Callback<BaseResponse<List<Transaction>>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<List<Transaction>>> call,
                                           Response<BaseResponse<List<Transaction>>> response) {

                        try {
                            WritableArray array = new WritableNativeArray();

                            for(Transaction transaction : response.body().data) {
                                WritableMap collectionTransaction = ModelUtils.convertJsonToMap(new JSONObject(transaction.toJsonString()));
                                collectionTransaction.putString("transaction", collectionTransaction.getString("transaction"));
                                collectionTransaction.putInt("type", collectionTransaction.getInt("type"));
                                collectionTransaction.putInt("status", collectionTransaction.getInt("status"));
                                collectionTransaction.putInt("amount", collectionTransaction.getInt("amount"));
                                collectionTransaction.putInt("receivedTransactions", collectionTransaction.getInt("receivedTransactions"));
                                collectionTransaction.putInt("sentTransactions", collectionTransaction.getInt("sentTransactions"));
                                collectionTransaction.putInt("invalidTransactions", collectionTransaction.getInt("invalidTransactions"));
                                collectionTransaction.putInt("timestamp", collectionTransaction.getInt("timestamp"));
                                array.pushMap(collectionTransaction);
                            }

                            array.toString();
//                            promise.resolve(array);
                        } catch (JSONException e) {
//                            promise.reject("", e);
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<List<Transaction>>> call,
                                          Throwable t) {

//                        promise.reject("", t);
                    }
                });

        lock.await(200000, TimeUnit.MILLISECONDS);
    }

}