//package com.humaniq.apilib;
//
//
//import android.content.Context;
//import com.humaniq.apilib.models.UserTransaction;
//import com.humaniq.apilib.models.response.BaseResponse;
//import com.humaniq.apilib.services.restService.ServiceBuilder;
//import com.humaniq.apilib.services.restService.WalletService;
//import java.io.IOException;
//import java.util.List;
//import org.junit.runner.RunWith;
//import retrofit2.Call;
//import retrofit2.Response;
//
//import static org.junit.Assert.assertTrue;
//
///**
// * Created by ognev on 7/19/17.
// */
//
//@RunWith(MockitoJUnitRunner.class)
//
//public class WalletApiTest {
//
//  @Mock Context mMockContext;
//
//  @org.junit.Test public void getTransactions() throws Exception {
//
//    try {
//      ServiceBuilder.init(C.CONTACTS_BASE_URL, mMockContext);
//      WalletService apiEndpoints = ServiceBuilder.getWalletService();
//
//      Call<BaseResponse<List<UserTransaction>>> call = apiEndpoints.getUserTransactions(
//          "223344556677", 0,10);
//
//      //Magic is here at .execute() instead of .enqueue()
//      Response<BaseResponse<List<UserTransaction>>> response = call.execute();
//      BaseResponse authResponse = response.body();
//
//      assertTrue(response.isSuccessful());
//
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
//}