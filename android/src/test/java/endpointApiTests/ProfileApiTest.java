package endpointApiTests;

import android.content.res.Resources;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.humaniq.apilib.BuildConfig;
import com.humaniq.apilib.Constants;
import com.humaniq.apilib.R;
import com.humaniq.apilib.network.models.request.FcmCredentials;
import com.humaniq.apilib.network.models.request.ValidateRequest;
import com.humaniq.apilib.network.models.request.profile.AccountAvatar;
import com.humaniq.apilib.network.models.request.profile.AccountPassword;
import com.humaniq.apilib.network.models.request.profile.AccountPerson;
import com.humaniq.apilib.network.models.request.profile.UserId;
import com.humaniq.apilib.network.models.request.wallet.UserTransaction;
import com.humaniq.apilib.network.models.response.BasePayload;
import com.humaniq.apilib.network.models.response.BaseResponse;
import com.humaniq.apilib.network.models.response.FacialImage;
import com.humaniq.apilib.network.models.response.FacialImageValidation;
import com.humaniq.apilib.network.models.response.TransactionResponse;
import com.humaniq.apilib.network.models.response.ValidationResponse;
import com.humaniq.apilib.network.models.response.profile.AccountAvatarResponse;
import com.humaniq.apilib.network.models.response.profile.AccountProfile;
import com.humaniq.apilib.network.models.response.profile.DeauthModel;
import com.humaniq.apilib.network.models.response.profile.ExchangeModelHmq;
import com.humaniq.apilib.network.models.response.profile.ExchangeModelUsd;
import com.humaniq.apilib.network.service.AuthorizationService;
import com.humaniq.apilib.network.service.BlockchainService;
import com.humaniq.apilib.network.service.ProfileService;
import com.humaniq.apilib.network.service.ValidationService;
import com.humaniq.apilib.network.service.WalletService;
import com.humaniq.apilib.network.service.providerApi.ServiceBuilder;
import com.humaniq.apilib.storage.Prefs;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.Request;
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

@RunWith(RobolectricTestRunner.class) @Config(constants = BuildConfig.class)
public class ProfileApiTest {

  @Before public void init() {
    ShadowLog.stream = System.out;
  }

  @Test public void deauthenticateUserRightResponse() {
    try {
      new Prefs(RuntimeEnvironment.application);
      ServiceBuilder.init(Constants.BASE_URL, RuntimeEnvironment.application);
      ProfileService apiEndpoints = ServiceBuilder.getProfileService();

      Call<DeauthModel> call = apiEndpoints.deauthenticateUser(new UserId("1575984550457116577"));
      Response<DeauthModel> response = call.execute();
      DeauthModel extractContactResponse = response.body();
      System.out.println(response.errorBody().string());
      //ShadowLog.v("tag", response.errorBody().string());
      assertTrue(response.isSuccessful());
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  @Test public void testUpdateUserPerson() {
    new Prefs(RuntimeEnvironment.application);
    ServiceBuilder.init(Constants.BASE_URL, RuntimeEnvironment.application);

    try {
      ProfileService service = ServiceBuilder.getProfileService();

      AccountPerson accountPerson = new AccountPerson();
      accountPerson.setAccountId("1569731856058811465");
      AccountPerson.Person person = new AccountPerson.Person();
      person.setFirstName("Anton");
      person.setLastName("Mozgovoy");
      accountPerson.setPerson(person);

      Call<BasePayload<AccountPerson>> call = service.updateAccountPerson(accountPerson);

      Response<BasePayload<AccountPerson>> response = call.execute();

      BasePayload<AccountPerson> baseResponse = response.body();

      assertTrue(response.isSuccessful());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test public void testUpdateAccountPassword() {
    new Prefs(RuntimeEnvironment.application);
    ServiceBuilder.init(Constants.BASE_URL, RuntimeEnvironment.application);

    try {
      ProfileService service = ServiceBuilder.getProfileService();

      AccountPassword accountPassword = new AccountPassword();
      accountPassword.setAccountId("1570909452079465500");
      accountPassword.setOldPassword("1234567");
      accountPassword.setNewPassword("1234567");
      //AccountPerson.Person person = new AccountPerson.Person();
      //person.setFirstName("Anton");
      //person.setLastName("Mozgovoy");
      //accountPerson.setPerson(person);

      Call<BasePayload<Object>> call = service.updateAccountPassword(accountPassword);

      Response<BasePayload<Object>> response = call.execute();

      BasePayload<Object> baseResponse = response.body();

      assertTrue(response.isSuccessful());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test public void testUpdateAccountAvatar() {
    new Prefs(RuntimeEnvironment.application);
    ServiceBuilder.init(Constants.BASE_URL, RuntimeEnvironment.application);


    try {
      Resources res = RuntimeEnvironment.application.getResources();
      InputStream in_s = res.openRawResource(R.raw.ava);

      byte[] b = new byte[in_s.available()];
      in_s.read(b);
      base64 = new String(b);
    } catch (Exception e) {
      // e.printStackTrace();
    }

    try {
      ProfileService service = ServiceBuilder.getProfileService();

      AccountAvatar accountPassword = new AccountAvatar();
      accountPassword.setAccountId("1587550012009612315");
      accountPassword.setFacialImage(base64);
      //AccountPerson.Person person = new AccountPerson.Person();
      //person.setFirstName("Anton");
      //person.setLastName("Mozgovoy");
      //accountPerson.setPerson(person);

      Call<BasePayload<AccountAvatarResponse>> call = service.updateAccountAvatar(accountPassword);

      Response<BasePayload<AccountAvatarResponse>> response = call.execute();

      BasePayload<AccountAvatarResponse> baseResponse = response.body();

      assertTrue(response.isSuccessful());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test public void testValidateAccountAvatar() {
    new Prefs(RuntimeEnvironment.application);
    ServiceBuilder.init(Constants.BASE_URL, RuntimeEnvironment.application);


    try {
      Resources res = RuntimeEnvironment.application.getResources();
      InputStream in_s = res.openRawResource(R.raw.ava);

      byte[] b = new byte[in_s.available()];
      in_s.read(b);
      base64 = new String(b);
    } catch (Exception e) {
      // e.printStackTrace();
    }

    try {
      ValidationService service = ServiceBuilder.getValidationService();

      ValidateRequest validateRequest = new ValidateRequest();
      validateRequest.setFacialImage(base64);
      //validateRequest.setFacialImageId();
      AccountAvatar accountPassword = new AccountAvatar();
      accountPassword.setAccountId("1587550012009612315");
      accountPassword.setFacialImage(base64);
      //AccountPerson.Person person = new AccountPerson.Person();
      //person.setFirstName("Anton");
      //person.setLastName("Mozgovoy");
      //accountPerson.setPerson(person);

      //Call<BasePayload<AccountAvatarResponse>> call = service.validate(accountPassword);
      //
      //Response<BasePayload<AccountAvatarResponse>> response = call.execute();
      //
      //BasePayload<AccountAvatarResponse> baseResponse = response.body();
      //
      //assertTrue(response.isSuccessful());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test public void testGetTransaction() {
    new Prefs(RuntimeEnvironment.application);
    ServiceBuilder.init(Constants.BASE_URL, RuntimeEnvironment.application);

    try {
      WalletService service = ServiceBuilder.getWalletService();
      Call<BaseResponse<UserTransaction>> call = service.getUserTransaction("1570909452079465500",
          "0xb90c79d12f093c1962bf213397d7ca6a1fd295f7b957328ee23593e0d8047b55");
      Response<BaseResponse<UserTransaction>> payload = call.execute();
      Log.d("profile", payload.body().toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //@Test public void testGetTransaction() {
  //  new Prefs(RuntimeEnvironment.application);
  //  ServiceBuilder.init(Constants.BASE_URL, RuntimeEnvironment.application);
  //
  //  try {
  //    WalletService service = ServiceBuilder.getWalletService();
  //    Call<BaseResponse<UserTransaction>> call = service.getUserTransaction("1570909452079465500",
  //        "0xb90c79d12f093c1962bf213397d7ca6a1fd295f7b957328ee23593e0d8047b55");
  //    Response<BaseResponse<UserTransaction>> payload = call.execute();
  //    Log.d("profile", payload.body().toString());
  //  } catch (Exception e) {
  //    e.printStackTrace();
  //  }
  //}

  @Test public void testGetWalletAddress() {
    new Prefs(RuntimeEnvironment.application);
    ServiceBuilder.init(Constants.BASE_URL, RuntimeEnvironment.application);

    try {
      BlockchainService service = ServiceBuilder.getBlockchainService();
      Call<BaseResponse<Object>> call = service.getUserAddressState("1587706100549944594");
      Response<BaseResponse<Object>> payload = call.execute();
      Log.d("profile", payload.body().data.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
 //@Test public void testGetWalletBalance() {
 //   new Prefs(RuntimeEnvironment.application);
 //   ServiceBuilder.init(Constants.BASE_URL, RuntimeEnvironment.application);
 //
 //   try {
 //     WalletS service = ServiceBuilder.getBlockchainService();
 //     Call<BaseResponse<Object>> call = service.getUserAddressState("1587706100549944594");
 //     Response<BaseResponse<Object>> payload = call.execute();
 //     Log.d("profile", payload.body().data.toString());
 //   } catch (Exception e) {
 //     e.printStackTrace();
 //   }
 // }

  String facialImageId;

  @Test public void testValidationSteps() {
    new Prefs(RuntimeEnvironment.application);
    ServiceBuilder.init(Constants.BASE_URL, RuntimeEnvironment.application);

    try {
      Resources res = RuntimeEnvironment.application.getResources();
      InputStream in_s = res.openRawResource(R.raw.ava);

      byte[] b = new byte[in_s.available()];
      in_s.read(b);
      base64 = new String(b);
    } catch (Exception e) {
      // e.printStackTrace();
    }

    try {

      ValidationService validationService = ServiceBuilder.getValidationService();

      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("facial_image", base64);
      Call<BasePayload<FacialImage>> call = validationService.isRegistered(jsonObject);
      Response<BasePayload<FacialImage>> payloadResponse = call.execute();
      facialImageId = payloadResponse.body().payload.getFacialImageId();
      JsonObject jsonObject1 = new JsonObject();
      jsonObject1.addProperty("facial_image_id", facialImageId);
      Call<BasePayload<FacialImageValidation>> call1 =
          validationService.createValidation(jsonObject1);
      Response<BasePayload<FacialImageValidation>> payloadResponse1 = call1.execute();

      ValidateRequest validateRequest = new ValidateRequest();
      validateRequest.setFacialImageId(
          payloadResponse1.body().payload.getFacialImageValidationId());
      validateRequest.setFacialImage(base64);
      Call<BasePayload<ValidationResponse>> call2 = validationService.validate(validateRequest);
      Response<BasePayload<ValidationResponse>> payloadResponse2 = call2.execute();

      System.out.println(payloadResponse2.body().payload.getMessage());
      assertTrue(payloadResponse2.isSuccessful());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String base64;

  @Test public void testCreateTransaction() {
    new Prefs(RuntimeEnvironment.application);
    ServiceBuilder.init(Constants.BASE_URL, RuntimeEnvironment.application);//1571772003105376001
//1571923392783714240
    try {//
      Call call = ServiceBuilder.getWalletService()//
          .createTransaction("4444444444010", "1576037290491249690", null, 100000000);

      Response<BaseResponse<TransactionResponse>> response = call.execute();

      assertTrue(response.isSuccessful());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test public void testGetProfiles() {
    new Prefs(RuntimeEnvironment.application);
    ServiceBuilder.init(Constants.BASE_URL, RuntimeEnvironment.application);

    //ReadableArray<String> accountIds= new ArrayList<>();
    //accountIds.add("1570909452079465500");
    //accountIds.add("1570909452079465501");
    //accountIds.add("1570909452079465502");
    //String query = "";
    //for(int i = 0; i < accountIds.size(); i++) {
    //  query += accountIds.get(i) + "&account_id=";
    //}

    //try {
    //  ProfileService service = ServiceBuilder.getProfileService();
    //  Call<BasePayload<AccountProfile.List>> call = service.getAccountProfiles(accountIds);
    //  Response<BasePayload<AccountProfile.List>> payload = call.execute();
    //  Log.d("profile", payload.body().payload.toString());
    //} catch (Exception e) {
    //  e.printStackTrace();
    //}
  }

  @Test public void testSaveFcmCredentials() {
    new Prefs(RuntimeEnvironment.application);
    Prefs.saveJwtToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiI0U0lDWk05MmQ3Rnd4VG9lMGlwZUN3ZGRISXNWN1hNMEhmM1ppdUs4Y2hkWEwya3RxdVdJVndNRGlhc3ZRWGtWQ3pDVlV6Y21KSUplTUtEbWkxaXBNT2RlVnJEN1lLaWhrQVJBcmt3SGI1cWU2bTJPUFF4c0JVdTk0Rm1veEtacSJ9.W4AQ4XSV_19IJmELXuyBrbTxNZF28b2wLGVi-tfJjps");
    ServiceBuilder.init(Constants.BASE_URL, RuntimeEnvironment.application);

    try {
      FcmCredentials fcmCredentials = new FcmCredentials();
      fcmCredentials.setAccountId(Long.valueOf("1570123796151534997"));
      fcmCredentials.setToken(
          "e1sAmGApuFg:APA91bFhncSCrYlxuk10Zkcfy4M672gGQN2212MAj2AXTiu2favquLfyRrJkqppSNoc-Cz_tU7orSmeBE5Rmp5xCSEWWzkzn3R3hGyVOEZ2E0_BnbOhSug-fmgHCF-grAVSs3okUegGE");

      Response<BaseResponse<Object>> response =
          ServiceBuilder.getFcmService().saveFcmToken(fcmCredentials).execute();

      assertTrue(response.isSuccessful());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test public void testGetExchangeHmq() {
    new Prefs(RuntimeEnvironment.application);
    ServiceBuilder.init(Constants.BASE_URL, RuntimeEnvironment.application);

    try {
      WalletService service = ServiceBuilder.getWalletService();
      Call<BaseResponse<ExchangeModelHmq>> call = service.getExchangeHmq("20");
      Response<BaseResponse<ExchangeModelHmq>> response = call.execute();

      System.out.println(new Gson().toJson(response.body()));
      ExchangeModelHmq exchangeModel = response.body().data;
      System.out.println(exchangeModel.getHMQ());

      assertTrue(response.isSuccessful());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  @Test public void testGetExchangeUsd() {
    new Prefs(RuntimeEnvironment.application);
    ServiceBuilder.init(Constants.BASE_URL, RuntimeEnvironment.application);

    try {
      WalletService service = ServiceBuilder.getWalletService();
      Call<BaseResponse<ExchangeModelUsd>> call = service.getExchangeUsd("20");
      Response<BaseResponse<ExchangeModelUsd>> response = call.execute();

      System.out.println(new Gson().toJson(response.body()));
      ExchangeModelUsd exchangeModel = response.body().data;
      System.out.println(exchangeModel.getUSD());

      assertTrue(response.isSuccessful());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  //@Test public void testIsRegistered() {
  //  new Prefs(RuntimeEnvironment.application);
  //  ServiceBuilder.init(Constants.BASE_URL, RuntimeEnvironment.application);
  //
  //  try {
  //    ValidationService service = ServiceBuilder.getValidationService();
  //    JsonObject jsonObject = new JsonObject();
  //
  //    Call<BaseResponse<ExchangeModelUsd>> call = service.isRegistered("20");
  //    Response<BaseResponse<ExchangeModelUsd>> response = call.execute();
  //
  //    System.out.println(new Gson().toJson(response.body()));
  //    ExchangeModelUsd exchangeModel = response.body().data;
  //    System.out.println(exchangeModel.getUSD());
  //
  //    assertTrue(response.isSuccessful());
  //  } catch (Exception e) {
  //    e.printStackTrace();
  //  }
  //}
}
