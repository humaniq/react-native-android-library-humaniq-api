
import com.google.gson.Gson;
import com.humaniq.apilib.BuildConfig;
import com.humaniq.apilib.Constants;
import com.humaniq.apilib.network.models.request.profile.UserId;
import com.humaniq.apilib.network.models.request.wallet.Balance;
import com.humaniq.apilib.network.models.request.wallet.UserTransaction;
import com.humaniq.apilib.network.models.response.BaseResponse;
import com.humaniq.apilib.network.models.response.profile.DeauthModel;
import com.humaniq.apilib.network.service.ProfileService;
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


  @Test public void testGetBalance() throws Exception {
    try {
      ServiceBuilder.init(Constants.CONTACTS_BASE_URL, RuntimeEnvironment.application);
      WalletService apiEndpoints = ServiceBuilder.getWalletService();

      Call<BaseResponse<Balance>> call = apiEndpoints.getUserBalance("223344556677");

      //Magic is here at .execute() instead of .enqueue()
      Response<BaseResponse<Balance>> response = call.execute();
      BaseResponse data = response.body();
      System.out.println(new Gson().toJson(data.data));

      assertTrue(response.isSuccessful());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  @Test public void testCreateTransaction() throws Exception {
    try {
      ServiceBuilder.init(Constants.CONTACTS_BASE_URL, RuntimeEnvironment.application);
      WalletService apiEndpoints = ServiceBuilder.getWalletService();

      Call<BaseResponse<Object>> call = apiEndpoints.
          createTransaction("223344556677", "223344556677", 20f);

      //Magic is here at .execute() instead of .enqueue()
      Response<BaseResponse<Object>> response = call.execute();
      BaseResponse data = response.body();

      assertTrue(response.isSuccessful());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test public void testGetTransactions() throws Exception {

    try {
      ServiceBuilder.init(Constants.CONTACTS_BASE_URL, RuntimeEnvironment.application);
      WalletService apiEndpoints = ServiceBuilder.getWalletService();

      Call<BaseResponse<List<UserTransaction>>> call = apiEndpoints.getUserTransactions(
          "223344556677", 0,10);

      //Magic is here at .execute() instead of .enqueue()
      Response<BaseResponse<List<UserTransaction>>> response = call.execute();
      BaseResponse data = response.body();
      if(response.isSuccessful())
      System.out.println(new Gson().toJson(data.data));

      assertTrue(response.isSuccessful());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }



}