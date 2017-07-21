package formatRequestsTests;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by gritsay on 7/21/17.
 */

public class UserAgentInterceptorTest extends BaseInterceptorTest {
  private static final String USER_AGENT = "some custom test user agent";

  @Test
  public void intercept() throws Exception {

    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.start();
    mockWebServer.enqueue(new MockResponse());

    mockWebServer.enqueue(new MockResponse());

    OkHttpClient okHttpClient = new OkHttpClient()
        .newBuilder()
        .addInterceptor((Interceptor) new UserAgentInterceptor(USER_AGENT))
        .build();
    okHttpClient.newCall(new Request.Builder().url(mockWebServer.url("/")).build()).execute();

    RecordedRequest request = mockWebServer.takeRequest();
    assertEquals(USER_AGENT, request.getHeader("User-Agent"));
  }

}
