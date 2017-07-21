package formatRequestsTests;

import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;

/**
 * Created by gritsay on 7/21/17.
 */

public abstract class BaseInterceptorTest {
  protected MockWebServer mockWebServer;

  @Before
  public void setUp() throws Exception {
    mockWebServer = new MockWebServer();
    mockWebServer.start();
  }

  @After
  public void tearDown() throws Exception {
    mockWebServer.shutdown();
  }
}
