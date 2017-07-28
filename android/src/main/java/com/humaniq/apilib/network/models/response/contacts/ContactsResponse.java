package com.humaniq.apilib.network.models.response.contacts;

import java.util.ArrayList;

/**
 * Created by gritsay on 7/17/17.
 */

public class ContactsResponse {
  private boolean success;
  private ArrayList<Contact> data;

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public ArrayList<Contact> getData() {
    return data;
  }

  public void setData(ArrayList<Contact> data) {
    this.data = data;
  }
}

