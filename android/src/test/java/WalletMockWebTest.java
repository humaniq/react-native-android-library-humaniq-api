import com.google.gson.Gson;
import com.humaniq.apilib.BuildConfig;
import com.humaniq.apilib.Constants;
import com.humaniq.apilib.network.models.request.wallet.Balance;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.List;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.maven.artifact.ant.Repository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by ognev on 7/20/17.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class WalletMockWebTest {
  private MockWebServer server;
    @Test public void testGetBalance() throws IOException, InterruptedException {
      server = new MockWebServer();
      server.enqueue(new MockResponse().setBody("{"
          + "    \"success\": true,"
          + "    \"data\": {"
          + "        \"token\": {"
          + "            \"currency\": \"HMQ\","
          + "            \"amount\": 190.6"
          + "        },"
          + "        \"default\": {"
          + "            \"currency\": \"USD\","
          + "            \"amount\": 27.7323"
          + "        },"
          + "        \"local\": {"
          + "            \"currency\": \"SOS\","
          + "            \"amount\": 15141.841623783"
          + "        }"
          + "    }"
          + "}"));

      URL url = server.url("/").url();
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestProperty("Accept-Language", "en-US");
      InputStream in = connection.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      assertEquals(HttpURLConnection.HTTP_OK, connection.getResponseCode());
      Balance balance = new Gson().fromJson(reader.readLine(), Balance.class);

      assertTrue(balance != null);

      RecordedRequest request = server.takeRequest();
      assertEquals("GET / HTTP/1.1", request.getRequestLine());
      assertEquals("en-US", request.getHeader("Accept-Language"));
      //System.out.println(server.takeRequest().);
    }
}
