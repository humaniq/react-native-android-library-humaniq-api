package com.humaniq.apilib.fcm;

import android.content.Intent;
import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.JsonObject;
import com.humaniq.apilib.Constants;
import com.humaniq.apilib.network.models.request.FcmCredentials;
import com.humaniq.apilib.network.models.response.BaseResponse;
import com.humaniq.apilib.network.service.providerApi.ServiceBuilder;
import com.humaniq.apilib.storage.Prefs;
import java.io.IOException;
import retrofit2.Response;

public class HumaniqFirebaseInstanceIDService extends FirebaseInstanceIdService {

  private static final String TAG = "MyFirebaseIIDService";

  @Override public void onTokenRefresh() {
    new Prefs(getApplicationContext());

    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
    Log.d(TAG, "Refreshed token: " + refreshedToken);
    Prefs.saveFCMToken(refreshedToken);

    if(Prefs.hasToken()) {
      try {
        sendRegistrationToServer(refreshedToken);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    try {
      sendRegistrationToServer(refreshedToken);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void sendRegistrationToServer(String token) throws IOException {

    ServiceBuilder.init(Constants.BASE_URL, getApplicationContext());
    FcmCredentials fcmCredentials = new FcmCredentials();
    fcmCredentials.setAccountId(Long.valueOf("1570123796151534997"));
    fcmCredentials.setToken("0x10fb68cbc45038476b93d921f46eaf59c82e9a210b8eebb9a9137ad12c2e826d");

    Response<BaseResponse<Object>> response =
        ServiceBuilder.getFcmService().saveFcmToken(fcmCredentials).execute();

    Intent i = new Intent("com.humaniq.apilib.fcm.ReceiveNotification");
    i.putExtra("registration", response.code() + "");
    sendOrderedBroadcast(i, null);
  }
  //
  //private void sendNotification(String messageBody) {
  //  //Intent intent = new Intent(this, MainActivity.class);
  //  //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
  //  //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
  //  //        PendingIntent.FLAG_ONE_SHOT);
  //
  //  Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
  //  NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
  //      //.setSmallIcon(R.)
  //      .setContentTitle("FCM Message")
  //      .setContentText(messageBody)
  //      .setAutoCancel(true)
  //      .setSound(defaultSoundUri);
  //  //.setContentIntent(pendingIntent);
  //
  //  NotificationManager notificationManager =
  //      (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
  //
  //  notificationBuilder.setAutoCancel(true);
  //
  //  notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
  //}
}