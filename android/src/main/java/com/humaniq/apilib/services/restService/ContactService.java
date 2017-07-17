package com.humaniq.apilib.services.restService;

import com.google.gson.JsonObject;
import com.humaniq.apilib.models.contacts.response.ContactsResponse;
import com.humaniq.apilib.models.response.BaseResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by gritsay on 7/17/17.
 */

public interface ContactService {
    @POST("/extract_registered_phone_numbers")
    Call<ContactsResponse> extractPhoneNumbers(@Body ArrayList<String> data);


}
