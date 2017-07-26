package com.humaniq.apilib.network.models.response;

/**
 * Created by ognev on 7/26/17.
 */

public class BasePayload<T> {

  public int code;

  public String message;

  public T payload;
}
