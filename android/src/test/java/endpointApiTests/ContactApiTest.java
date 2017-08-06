package endpointApiTests;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import com.google.gson.Gson;
import com.humaniq.apilib.BuildConfig;
import com.humaniq.apilib.Constants;
import com.humaniq.apilib.network.models.response.contacts.ContactsResponse;
import com.humaniq.apilib.network.service.ContactService;
import com.humaniq.apilib.network.service.providerApi.ServiceBuilder;
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


  private List<String> getAllContacts() {
    ArrayList<String> result = null;

    ContentResolver cr = RuntimeEnvironment.application.getContentResolver();
    Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

    if (cur.getCount() > 0) {
      result = new ArrayList<String>();
      while (cur.moveToNext()) {
        String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
        //                String name = cur.getString(cur.getColumnIndex(
        //                        ContactsContract.Contacts.DISPLAY_NAME));
        if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
          Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
              ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);
          while (pCur.moveToNext()) {
            String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            result.add(phoneNo);
          }
          pCur.close();
        }
      }
    } else {
      //Log.d(LOG_TAG, "No contacts");
    }

    return result;
  }

  @Test public void extractContactsSuccess() throws Exception {
    try {
      ServiceBuilder.init(Constants.BASE_URL, RuntimeEnvironment.application);
      ContactService apiEndpoints = ServiceBuilder.getContactsService();
      ArrayList<String> phones = new ArrayList<String>();
      phones.addAll(getAllContacts());
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
