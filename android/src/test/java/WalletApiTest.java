
import com.humaniq.apilib.BuildConfig;
import com.humaniq.apilib.Constants;
import com.humaniq.apilib.network.models.request.wallet.UserTransaction;
import com.humaniq.apilib.network.models.response.BaseResponse;
import com.humaniq.apilib.network.service.providerApi.ServiceBuilder;
import com.humaniq.apilib.network.service.WalletService;
import java.io.IOException;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.assertTrue;

/**
 * Created by ognev on 7/19/17.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)

public class WalletApiTest {


  @Test public void getTransactions() throws Exception {

    try {
      ServiceBuilder.init(Constants.CONTACTS_BASE_URL, RuntimeEnvironment.application);
      WalletService apiEndpoints = ServiceBuilder.getWalletService();

      Call<BaseResponse<List<UserTransaction>>> call = apiEndpoints.getUserTransactions(
          "223344556677", 0,10);

      //Magic is here at .execute() instead of .enqueue()
      Response<BaseResponse<List<UserTransaction>>> response = call.execute();
      BaseResponse authResponse = response.body();

      assertTrue(response.isSuccessful());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}