package com.humaniq.apilib.fcm;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.humaniq.apilib.Constants;
import com.humaniq.apilib.network.models.request.wallet.UserTransaction;
import com.humaniq.apilib.network.models.response.BaseResponse;
import com.humaniq.apilib.network.service.WalletService;
import com.humaniq.apilib.network.service.providerApi.ServiceBuilder;
import com.humaniq.apilib.storage.Prefs;
import io.jsonwebtoken.lang.RuntimeEnvironment;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HumaniqFirebaseMessagingService extends FirebaseMessagingService {

  private static final String TAG = "HumaniqFirebaseMessagingService";

  @Override public void onMessageReceived(RemoteMessage remoteMessage) {
    Intent i = new Intent("com.humaniq.apilib.fcm.ReceiveNotification");
    i.putExtra("data", remoteMessage);

    showNotification(remoteMessage.getData().get("hash"));
    sendOrderedBroadcast(i, null);
  }

  private void showNotification(final String hash) {
    //Intent intent = new Intent(this, MainActivity.class);
    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
    //        PendingIntent.FLAG_ONE_SHOT);

    new Prefs(getApplicationContext());
    ServiceBuilder.init(Constants.BASE_URL, getApplicationContext());
    try {

      WalletService service = ServiceBuilder.getWalletService();
      Call<BaseResponse<UserTransaction>> call =
          service.getUserTransaction(Prefs.getAccountId(), hash);

      Response<BaseResponse<UserTransaction>> response = call.execute();
      BaseResponse<UserTransaction> baseResponse = response.body();

      if (response.body() != null) {

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
            new NotificationCompat.Builder(getApplicationContext())
                //.setSmallIcon(R.)
                .setContentTitle("Humaniq")
                .setContentText("You got " + baseResponse.data.getAmount() / 100000000 + " HMQ")
                .setAutoCancel(true)
                .setSound(defaultSoundUri);
        //.setContentIntent(pendingIntent);

        NotificationManager notificationManager =
            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder.setAutoCancel(true);

        notificationManager.notify(hash.hashCode() /* ID of notification */,
            notificationBuilder.build());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}