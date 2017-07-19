package com.humaniq.apilib.services.restService;

/**
 * Created by gritsay on 7/12/17.
 */

import okhttp3.OkHttpClient;
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

  public static void init(String baseURL) {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

    retrofit = new Retrofit.Builder().baseUrl(baseURL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
  }

  public static WalletService getProfileService() {
    return retrofit.create(WalletService.class);
  }

  public static ContactService getContactsService() {
    return retrofit.create(ContactService.class);
  }
}
