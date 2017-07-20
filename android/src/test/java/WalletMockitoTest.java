import com.humaniq.apilib.BuildConfig;
import com.humaniq.apilib.Constants;
import java.io.IOException;
import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.List;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.maven.artifact.ant.Repository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by ognev on 7/20/17.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class WalletMockitoTest   {
  private MockWebServer server;
    @Test public void testGetBalance() throws IOException {
      MockWebServer server = new MockWebServer();

      server.enqueue(new MockResponse().setBody("hello, world!"));
      server.enqueue(new MockResponse().setBody("sup, bra?"));
      server.enqueue(new MockResponse().setBody("yo dog"));

      server.start();

      HttpUrl baseUrl = server.url(Constants.BASE_URL);
      System.out.println(server.takeRequest().);
    }
}
