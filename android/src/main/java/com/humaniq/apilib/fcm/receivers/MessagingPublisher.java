//package com.humaniq.apilib.fcm.receivers;
//
//import android.app.Application;
//import android.app.NotificationManager;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.support.v4.app.NotificationCompat;
//import com.facebook.react.modules.core.DeviceEventManagerModule;
//import com.humaniq.apilib.Constants;
//
//public class MessagingPublisher extends BroadcastReceiver {
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        new FIRLocalMessagingHelper((Application) context.getApplicationContext()).sendNotification(intent.getExtras());
//    }
//
//
//    private void sendNotification(String messageBody) {
//        //Intent intent = new Intent(this, MainActivity.class);
//        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//        //        PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//            //.setSmallIcon(R.)
//            .setContentTitle("FCM Message")
//            .setContentText(messageBody)
//            .setAutoCancel(true)
//            .setSound(defaultSoundUri);
//        //.setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationBuilder.setAutoCancel(true);
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//
//        getReactApplicationContext().
//            getJSModule(DeviceEventManagerModule.
//                RCTDeviceEventEmitter.class)
//            .emit(Constants.EVENT_PROGRESS_CHANGED, params);
//    }
//}