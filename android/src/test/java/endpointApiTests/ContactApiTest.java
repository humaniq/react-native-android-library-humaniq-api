package endpointApiTests;

import com.google.gson.Gson;
import com.humaniq.apilib.BuildConfig;
import com.humaniq.apilib.Constants;
import com.humaniq.apilib.network.models.response.contacts.ContactsResponse;
import com.humaniq.apilib.network.service.ContactService;
import com.humaniq.apilib.network.service.providerApi.ServiceBuilder;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.assertTrue;

/**
 * Created by gritsay on 7/20/17.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ContactApiTest {


  @Test public void extractContactsSuccess() throws Exception {
    try {
      ServiceBuilder.init(Constants.CONTACTS_BASE_URL, RuntimeEnvironment.application);
      ContactService apiEndpoints = ServiceBuilder.getContactsService();
      ArrayList<String> phones = new ArrayList<String>();
      phones.add("");
      Call<ContactsResponse> call = apiEndpoints.extractPhoneNumbers(phones);
      Response<ContactsResponse> response = call.execute();
      ContactsResponse extractContactResponse = response.body();
      System.out.println(new Gson().toJson(response.body()));
      assertTrue(response.isSuccessful());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
