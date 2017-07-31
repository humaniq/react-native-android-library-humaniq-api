package com.humaniq.apilib.network.service;

import com.humaniq.apilib.network.models.response.contacts.ContactsResponse;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by gritsay on 7/17/17.
 */

public interface ContactService {

  @POST("/contact-checker/api/v1/extract_registered_phone_numbers")
  Call<ContactsResponse> extractPhoneNumbers(@Body List<String> data);

  @POST("/contact-checker/api/v1/extract_registered_phone_numbers")
  Call<ContactsResponse> extractPhoneNumber(@Body List<String> data);
}
