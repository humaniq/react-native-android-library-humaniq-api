package com.humaniq.apilib.network.models.response.profile;

import com.google.gson.annotations.SerializedName;
import com.humaniq.apilib.network.models.request.profile.AccountPerson;

/**
 * Created by ognev on 7/26/17.
 */

public class AccountProfile {

  @SerializedName("account_id")
  private String accountId;

  @SerializedName("person")
  private AccountPerson.Person person;

  @SerializedName("phone_number")
  private PhoneNumber phoneNumber;

  @SerializedName("avatar")
  private AccountAvatarResponse.Avatar avatar;

  public String getAccountId() {
    return accountId;
  }

  public AccountPerson.Person getPerson() {
    return person;
  }

  public PhoneNumber getPhoneNumber() {
    return phoneNumber;
  }

  public AccountAvatarResponse.Avatar getAvatar() {
    return avatar;
  }



  private class PhoneNumber {
    @SerializedName("country_code")
    private String countryCode;

    @SerializedName("phone_number")
    private String phoneNumber;

    public String getCountryCode() {
      return countryCode;
    }

    public String getPhoneNumber() {
      return phoneNumber;
    }
  }


}
