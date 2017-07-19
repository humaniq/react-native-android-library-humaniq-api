package com.humaniq.apilib.services.restService;

import com.humaniq.apilib.models.contacts.response.ContactsResponse;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by gritsay on 7/17/17.
 */

public interface ContactService {
  @POST("/contact-checker/api/v1/extract_registered_phone_numbers")
  Call<ContactsResponse> extractPhoneNumbers(@Body ArrayList<String> data);
}
