package com.humaniq.apilib.models.contacts.response;

import java.util.ArrayList;

/**
 * Created by gritsay on 7/17/17.
 */

public class ContactsResponse {
    private boolean success;
    private ArrayList<String> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }
}

