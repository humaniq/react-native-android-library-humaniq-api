package com.humaniq.apilib.fcm;

import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class HumaniqFirebaseInstanceIDService extends FirebaseInstanceIdService {

  private static final String TAG = "MyFirebaseIIDService";

  @Override public void onTokenRefresh() {
    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
    Log.d(TAG, "Refreshed token: " + refreshedToken);

    sendRegistrationToServer(refreshedToken);
    //Toast.makeText(getApplicationContext(), refreshedToken, Toast.LENGTH_SHORT)
    //    .show();
  }

  private void sendRegistrationToServer(String token) {
    // TODO: Implement this method to send token to your app server.
  }
}