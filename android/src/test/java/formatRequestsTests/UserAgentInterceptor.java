package formatRequestsTests;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by gritsay on 7/21/17.
 */

public class UserAgentInterceptor implements Interceptor {

  private final String userAgent;

  public UserAgentInterceptor(String userAgent) {
    this.userAgent = userAgent;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    //
    //Request request = chain.request().newBuilder()
    //    .addHeader("Accept-Language", Locale.getDefault().getLanguage())
    //    .addHeader("Accept", RestApi.VERSION_HEADER + RestApi.API_VERSION)
    //    .build();
    //return chain.proceed(request);

    Request originalRequest = chain.request();
    Request requestWithUserAgent =
        originalRequest.newBuilder()
            .removeHeader("User-Agent")
            .addHeader("User-Agent", userAgent)
            .build();
    return chain.proceed(requestWithUserAgent);
  }
}
