package com.humaniq.apilib.react;

import android.support.annotation.Nullable;
import android.util.Log;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.humaniq.apilib.Constants;
import com.humaniq.apilib.storage.Prefs;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by ognev on 7/24/17.
 */

public class MqttModule extends ReactContextBaseJavaModule {
  private static final String LOG_TAG = MqttModule.class.getSimpleName();
  final String serverUri =  "ws://broker.hivemq.com:8000";
  private MqttAndroidClient mqttAndroidClient;

  public MqttModule(ReactApplicationContext reactContext) {
    super(reactContext);
    new Prefs(reactContext);
  }



  @Override public String getName() {
    return "HumaniqMqttModule";
  }

  private void sendMessageToReact(@Nullable WritableMap params) {
    this.getReactApplicationContext().
        getJSModule(DeviceEventManagerModule.
            RCTDeviceEventEmitter.class)
        .emit(Constants.EVENT_MESSAGE_RECEIVED, params);
  }

  private void sendMessageDeliveryStatusToReact(@Nullable WritableMap params) {
    this.getReactApplicationContext().
        getJSModule(DeviceEventManagerModule.
            RCTDeviceEventEmitter.class)
        .emit(Constants.EVENT_MESSAGE_DELIVERED, params);
  }


  @ReactMethod public void connectMqtt(String clientId, final String topic) {
    mqttAndroidClient = new MqttAndroidClient(getReactApplicationContext(), serverUri, clientId);
    mqttAndroidClient.setCallback(new MqttCallbackExtended() {
      @Override
      public void connectComplete(boolean reconnect, String serverURI) {

        if (reconnect) {
          Log.d(LOG_TAG, "Reconnected to : " + serverURI);

          // Because Clean Session is true, we need to re-subscribe
          subscribeToTopic(topic);
        } else {
          Log.d(LOG_TAG, "Connected to: " + serverURI);
        }
      }

      @Override
      public void connectionLost(Throwable cause) {
        Log.d(LOG_TAG, "The Connection was lost.");
      }

      @Override
      public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.d(LOG_TAG, "Incoming message: " + new String(message.getPayload()));
      }

      @Override
      public void deliveryComplete(IMqttDeliveryToken token) {
        WritableMap messageMap = new WritableNativeMap();
        messageMap.putBoolean("delivered", true);
        sendMessageDeliveryStatusToReact(messageMap);
      }
    });

    MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
    mqttConnectOptions.setAutomaticReconnect(true);
    mqttConnectOptions.setCleanSession(false);

    try {
      //addToHistory("Connecting to " + serverUri);
      mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
          DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
          disconnectedBufferOptions.setBufferEnabled(true);
          disconnectedBufferOptions.setBufferSize(100);
          disconnectedBufferOptions.setPersistBuffer(false);
          disconnectedBufferOptions.setDeleteOldestMessages(false);
          mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
          subscribeToTopic(topic);
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
          Log.d(LOG_TAG, "Failed to connect to: " + serverUri);
        }
      });


    } catch (MqttException ex){
      ex.printStackTrace();
    }
  }




  // todo subscribing
  public void subscribeToGroupTopic(String groupTopic) {

  }

  private void subscribeToTopic(String subscriptionTopic){
    try {
      mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
          Log.d(LOG_TAG, "Subscribed!");
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
          Log.d(LOG_TAG, "Failed to subscribe");
        }
      });

      mqttAndroidClient.subscribe("", 0, new IMqttMessageListener() {
        @Override
        public void messageArrived(String topic, final MqttMessage message) throws Exception {
          // message Arrived!
          getReactApplicationContext().runOnUiQueueThread(new Runnable() {
            @Override public void run() {
              WritableMap messageMap = new WritableNativeMap();
              messageMap.putString("message", message.toString());
              sendMessageToReact(messageMap);
            }
          });
          Log.d(LOG_TAG, "Message: " + topic + " : " + new String(message.getPayload()));

        }
      });

    } catch (MqttException ex){
      Log.d(LOG_TAG,"Exception whilst subscribing");
      System.err.println();
      ex.printStackTrace();
    }
  }

  @ReactMethod public void sendMessage(String accountId, String messageBody) {

    try {
      MqttMessage message = new MqttMessage();
      message.setPayload(messageBody.getBytes());
      mqttAndroidClient.publish(accountId, message);
      if(!mqttAndroidClient.isConnected()) {
        Log.d(LOG_TAG, mqttAndroidClient.getBufferedMessageCount() + " messages in buffer.");
      }
    } catch (MqttException e) {
      Log.d(LOG_TAG, "Error Publishing: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
