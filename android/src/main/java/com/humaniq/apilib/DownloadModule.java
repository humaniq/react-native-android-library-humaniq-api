package com.humaniq.apilib;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.widget.ImageView;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.humaniq.apilib.constructor.ModelConverterUtils;
import com.humaniq.apilib.services.restService.ServiceBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/*
*
* import HumaniqApiLib from 'react-native-android-library-humaniq-api'
import HumaniqProfileApiLib from 'react-native-android-library-humaniq-api'
* HumaniqProfileApiLib.getTransactions('0x1111111111111111111111111111111111111111').then((addressState) => {
      // console.log(array);
      console.warn(JSON.stringify(addressState))
        //HumaniqApiLib.show(addressState.address);
    });


        HumaniqApiLib.getTransactions('0x1111111111111111111111111111111111111111').then((array) => {
          // console.log(array);
          console.warn(JSON.stringify(array))
            // HumaniqApiLib.show(array[0].name);
        });*/
public class DownloadModule extends ReactContextBaseJavaModule {
    private long enqueue;
    private DownloadManager dm;
    private Promise downloadPromise;

    public DownloadModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                long downloadId = intent.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(enqueue);
                Cursor c = dm.query(query);
                if (c.moveToFirst()) {
                    int columnIndex = c
                            .getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == c
                            .getInt(columnIndex)) {

                        String uriString = c
                                .getString(c
                                        .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        downloadPromise.resolve(uriString);
                    } else if(DownloadManager.STATUS_FAILED == c
                            .getInt(columnIndex)) {
                        downloadPromise.reject("failed");
                    }
                }
            }
        }
    };


    @Override
    public String getName() {
        return "HumaniqDownloadFileLib";
    }

    @Override
    public Map<String, Object> getConstants() {
        return new HashMap<>();
    }

    // http://clips.vorwaerts-gmbh.de/VfE_html5.mp4

    @ReactMethod
    public void downloadVideoFile(String uri, final Promise downloadPromise) {

        this.downloadPromise = downloadPromise;
        getReactApplicationContext().
                registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        dm = (DownloadManager) getReactApplicationContext()
                .getSystemService(getReactApplicationContext().DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse("http://clips.vorwaerts-gmbh.de/VfE_html5.mp4"));
        enqueue = dm.enqueue(request);
    }

    @ReactMethod
    public void resumeDownloadVideoFile(String id, final Promise promise) {
        // MOCK data!
        try {
            WritableMap balanceMock =
                    ModelConverterUtils.convertJsonToMap(new JSONObject(" {" +
                            "    \"HMQ\": 190.6," +
                            "    \"USD\": 22.62422" +
                            "  }"));

            promise.resolve(balanceMock);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}