package com.humaniq.apilib.fcm;

import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class HumaniqFirebaseInstanceIDService extends FirebaseInstanceIdService {

  private static final String TAG = "MyFirebaseIIDService";

  @Override public void onTokenRefresh() {
    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
    Log.d(TAG, "Refreshed token: " + refreshedToken);

    sendRegistrationToServer(refreshedToken);
    sendNotification(refreshedToken);
    //Toast.makeText(getApplicationContext(), refreshedToken, Toast.LENGTH_SHORT)
    //    .show();
  }

  private void sendRegistrationToServer(String token) {
    // TODO: Implement this method to send token to your app server.
  }

  private void sendNotification(String messageBody) {
    //Intent intent = new Intent(this, MainActivity.class);
    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
    //        PendingIntent.FLAG_ONE_SHOT);

    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
        //.setSmallIcon(R.)
        .setContentTitle("FCM Message")
        .setContentText(messageBody)
        .setAutoCancel(true)
        .setSound(defaultSoundUri);
    //.setContentIntent(pendingIntent);

    NotificationManager notificationManager =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    notificationBuilder.setAutoCancel(true);

    notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
  }
}