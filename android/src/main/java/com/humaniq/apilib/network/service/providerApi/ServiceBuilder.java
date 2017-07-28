package com.humaniq.apilib.network.service.providerApi;

/**
 * Created by gritsay on 7/12/17.
 */

import android.content.Context;
import com.humaniq.apilib.network.service.AuthorizationService;
import com.humaniq.apilib.network.service.BlockchainService;
import com.humaniq.apilib.utils.SelfSigningClientBuilder;
import com.humaniq.apilib.network.service.ContactService;
import com.humaniq.apilib.network.service.ProfileService;
import com.humaniq.apilib.network.service.WalletService;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Builder for retrofit
 */

public class ServiceBuilder {

  private static Retrofit retrofit;

  private ServiceBuilder() {

  }

  public static Retrofit getRetrofit() {
    return retrofit;
  }

  public static void init(String baseURL, Context context) {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    //OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

    retrofit = new Retrofit.Builder().baseUrl(baseURL)
        //.client(client)
        .client(SelfSigningClientBuilder.createClient(context))
        .addConverterFactory(GsonConverterFactory.create())
        .build();
  }

  public static WalletService getWalletService() {
    return retrofit.create(WalletService.class);
  }

  public static ProfileService getProfileService() {
    return retrofit.create(ProfileService.class);
  }

  public static ContactService getContactsService() {
    return retrofit.create(ContactService.class);
  }
  public static BlockchainService getBlockchainService() {
    return retrofit.create(BlockchainService.class);
  }

  public static AuthorizationService getAuthorizationService() {
    return retrofit.create(AuthorizationService.class);
  }
}
