package com.humaniq.apilib.services;

/**
 * Created by gritsay on 7/12/17.
 */

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.humaniq.apilib.C;


/**
 * Builder for retrofit
 */

public class ServiceBuilder {

    private static Retrofit retrofit;

    private ServiceBuilder() {

    }

    public static void init() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(C.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static WalletService getProfileService() {
        return retrofit.create(WalletService.class);
    }
}
