package com.humaniq.apilib.utils;

import com.facebook.react.bridge.WritableMap;
import com.facebook.stetho.common.ThreadBound;
import java.io.IOException;
import okhttp3.ResponseBody;

/**
 * Created by ognev on 7/29/17.
 */

public class ResponseWrapperUtils {
  public static Throwable wrapErrorBody(ResponseBody errorBody) {
    Throwable throwable = null;
    try {
      throwable = new Throwable(errorBody.string());
    } catch (Exception e) {
      e.printStackTrace();
      throwable = new Throwable("unknown error");
    }

    return throwable;
  }
}
