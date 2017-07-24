package endpointApiTests;

import com.google.gson.Gson;
import com.humaniq.apilib.BuildConfig;
import com.humaniq.apilib.Constants;
import com.humaniq.apilib.network.models.request.profile.AccountPerson;
import com.humaniq.apilib.network.models.request.profile.UserId;
import com.humaniq.apilib.network.models.response.BaseResponse;
import com.humaniq.apilib.network.models.response.contacts.ContactsResponse;
import com.humaniq.apilib.network.models.response.profile.DeauthErrorModel;
import com.humaniq.apilib.network.models.response.profile.DeauthModel;
import com.humaniq.apilib.network.service.ContactService;
import com.humaniq.apilib.network.service.ProfileService;
import com.humaniq.apilib.network.service.providerApi.ServiceBuilder;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.assertTrue;

/**
 * Created by gritsay on 7/20/17.
 */


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ProfileApiTest {

  @Before
  public void init() {
    ShadowLog.stream = System.out;
  }

  @Test public void deauthenticateUserRightResponse(){
    try {
      ServiceBuilder.init(Constants.CONTACTS_BASE_URL, RuntimeEnvironment.application);
      ProfileService apiEndpoints = ServiceBuilder.getProfileService();

      Call<DeauthModel> call = apiEndpoints.deauthenticateUser(new UserId(""));
      Response<DeauthModel> response = call.execute();
      DeauthModel extractContactResponse = response.body();
      System.out.println(response.errorBody().string());
      //ShadowLog.v("tag", response.errorBody().string());
      assertTrue(response.isSuccessful());

  } catch (Exception e) {
      System.out.println(e);
    }
  }

  @Test public void testupdateUserPerson() {
    ServiceBuilder.init(Constants.CONTACTS_BASE_URL, RuntimeEnvironment.application);

    try {
      ProfileService service = ServiceBuilder.getProfileService();

      AccountPerson accountPerson = new AccountPerson();
      accountPerson.setAccountId("");
      AccountPerson.Person person = new AccountPerson.Person();
      person.setFirstName("test");
      person.setLastName("test");
      accountPerson.setPerson(person);

      Call<BaseResponse<Object>> call = service.updateAccountPerson(accountPerson);

      Response<BaseResponse<Object>> response = call.execute();

      BaseResponse<Object> baseResponse = response.body();

      assertTrue(response.isSuccessful());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
