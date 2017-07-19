package com.humaniq.apilib;

import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.humaniq.apilib.constructor.ModelConverterUtils;
import com.humaniq.apilib.models.Transaction;

import com.humaniq.apilib.models.UserTransaction;
import com.humaniq.apilib.models.profile.UserId;
import com.humaniq.apilib.models.profile.response.DeauthModel;
import com.humaniq.apilib.models.response.BaseResponse;
import com.humaniq.apilib.services.restService.ContactService;
import com.humaniq.apilib.services.restService.ProfileService;
import com.humaniq.apilib.services.restService.ServiceBuilder;
import com.humaniq.apilib.services.restService.WalletService;
import java.io.IOException;
import java.util.List;
import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by ognev on 7/12/17.
 */
public class ProfileModuleTest extends TestCase {
    private CountDownLatch lock = new CountDownLatch(1);
    public void testGetTransactions() throws Exception {

        ServiceBuilder.init(C.CONTACTS_BASE_URL);
        WalletService apiEndpoints = ServiceBuilder.getWalletService();

        Call<BaseResponse<List<UserTransaction>>> call = apiEndpoints.getUserTransactions(
            "223344556677", 0,10);

        try {
            //Magic is here at .execute() instead of .enqueue()
            Response<BaseResponse<List<UserTransaction>>> response = call.execute();
            BaseResponse authResponse = response.body();

            assertTrue(response.isSuccessful());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}