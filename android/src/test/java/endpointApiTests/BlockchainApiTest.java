package endpointApiTests;

import com.facebook.stetho.inspector.protocol.module.Network;
import com.humaniq.apilib.BuildConfig;
import com.humaniq.apilib.Constants;
import com.humaniq.apilib.network.models.request.blockchain.TransferRequest;
import com.humaniq.apilib.network.models.request.wallet.AddressState;
import com.humaniq.apilib.network.models.response.BaseResponse;
import com.humaniq.apilib.network.models.response.blockchain.TransferResponse;
import com.humaniq.apilib.network.service.BlockchainService;
import com.humaniq.apilib.network.service.providerApi.ServiceBuilder;
import com.humaniq.apilib.storage.Prefs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.assertTrue;

/**
 * Created by ognev on 7/29/17.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class BlockchainApiTest {

  @Test public void testGetUserAddressState() {
    try {
      new Prefs(RuntimeEnvironment.application);
      ServiceBuilder.init(Constants.CONTACTS_BASE_URL, RuntimeEnvironment.application);
      BlockchainService blockchainService = ServiceBuilder.getBlockchainService();
      Call<BaseResponse<Object>> responseCall = blockchainService.getUserAddressState("223344556677");
      Response<BaseResponse<Object>> response = responseCall.execute();
      //BaseResponse<AddressState> baseResponse = response.body();
      assertTrue(response.isSuccessful());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test public void testTransferHmq() {
    try {
      new Prefs(RuntimeEnvironment.application);
      ServiceBuilder.init(Constants.CONTACTS_BASE_URL, RuntimeEnvironment.application);
      BlockchainService blockchainService = ServiceBuilder.getBlockchainService();

      TransferRequest transferRequest = new TransferRequest("223344556677","223344556690",900);

      Call<TransferResponse> responseCall = blockchainService.transferHmq(transferRequest);
      Response<TransferResponse> response = responseCall.execute();
      //BaseResponse<AddressState> baseResponse = response.body();
      assertTrue(response.isSuccessful());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
