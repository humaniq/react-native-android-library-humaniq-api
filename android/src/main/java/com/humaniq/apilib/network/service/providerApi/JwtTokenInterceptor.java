package com.humaniq.apilib.network.service.providerApi;

import com.facebook.infer.annotation.Present;
import com.humaniq.apilib.storage.Prefs;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ognev on 7/28/17.
 */

public class JwtTokenInterceptor implements Interceptor {
  @Override public okhttp3.Response intercept(Chain chain) throws IOException {
      Request request = chain.request();

      if(Prefs.hasToken()) {
      request = request.newBuilder()
          .addHeader("Authorization", "Bearer " + Prefs.getJwtToken())
          .build();
      }

      Response response = chain.proceed(request);

      if(response.code() == 401) { // server currently do not have method to refresh TOKEN
        // TOKEN life have no expire time
        // TODO implement if backend will be ready
          Prefs.clearJwtToken();
        //String newJwtToken =  ServiceBuilder.getAuthorizationService()
        //    .refreshJwtToken(Prefs.getJwtToken()).execute().message();
        //Prefs.saveJwtToken(newJwtToken);
        request = request.newBuilder()
            .addHeader("Authorization", "Bearer" + Prefs.getJwtToken())
            .build();

        return chain.proceed(request);
      }

      return response;
  }
}
