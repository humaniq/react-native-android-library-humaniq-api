package com.humaniq.apilib.network.models.request.profile;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ognev on 7/24/17.
 */

public class AccountPerson {

  @SerializedName("account_id")
  private String accountId;

  @SerializedName("person")
  private Person person;

  public static class Person {
    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("middle_name")
    private String middleName;

    @SerializedName("suffix")
    private String suffix;

    @SerializedName("prefix")
    private String prefix;

    @SerializedName("nickname")
    private String nickname;


    public String getFirstName() {
      return firstName;
    }

    public String getLastName() {
      return lastName;
    }

    public String getMiddleName() {
      return middleName;
    }

    public String getSuffix() {
      return suffix;
    }

    public String getPrefix() {
      return prefix;
    }

    public String getNickname() {
      return nickname;
    }

    public void setFirstName(String firstName) {
      this.firstName = firstName;
    }

    public void setLastName(String lastName) {
      this.lastName = lastName;
    }

    public void setMiddleName(String middleName) {
      this.middleName = middleName;
    }

    public void setSuffix(String suffix) {
      this.suffix = suffix;
    }

    public void setPrefix(String prefix) {
      this.prefix = prefix;
    }

    public void setNickname(String nickname) {
      this.nickname = nickname;
    }
  }

  public String getAccountId() {
    return accountId;
  }

  public Person getPerson() {
    return person;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public void setPerson(Person person) {
    this.person = person;
  }
}
