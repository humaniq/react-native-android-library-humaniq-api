package com.humaniq.apilib.fcm;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.humaniq.apilib.Constants;

public class HumaniqFirebaseMessagingService extends FirebaseMessagingService {

  private static final String TAG = "HumaniqFirebaseMessagingService";

  @Override public void onMessageReceived(RemoteMessage remoteMessage) {
    Intent i = new Intent("com.humaniq.apilib.fcm.ReceiveNotification");
    i.putExtra("data", remoteMessage);
    sendOrderedBroadcast(i, null);
  }


}