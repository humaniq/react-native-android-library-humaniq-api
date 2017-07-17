package com.humaniq.apilib;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;

import com.facebook.common.internal.Files;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;


public class DownloadModule extends ReactContextBaseJavaModule {
    private long enqueue;
    private DownloadManager dm;
    private Promise downloadPromise;

    public DownloadModule(ReactApplicationContext reactContext) {
        super(reactContext);
        new Prefs(reactContext);
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

                        Prefs.saveDownloadedUri(uriString);

                        String fileName =
                                c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));

                        File file = new File(uriString);

                        FileOutputStream outputStream = null;
                        try {
                            outputStream =
                                    getReactApplicationContext().
                                            openFileOutput(fileName, Context.MODE_PRIVATE);
                            outputStream.write(Files.toByteArray(file));
                            outputStream.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        file.delete();

                        WritableMap writableMap = new WritableNativeMap();
                        writableMap.putString("uri", getReactApplicationContext()
                                .getFilesDir() + "/" + fileName);

                        Prefs.saveLocalUri(getReactApplicationContext()
                                .getFilesDir() + "/" + fileName);
                        downloadPromise.resolve(writableMap);
                    } else if(DownloadManager.STATUS_FAILED == c
                            .getInt(columnIndex)) {
                        downloadPromise.reject(new Throwable("error"));
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

    @ReactMethod
    public void downloadVideoFile(String uri, final Promise downloadPromise) {
        if(Prefs.isUriAlreadyDownloaded(uri)) {
            WritableMap localUri = new WritableNativeMap();
            localUri.putString("uri", Prefs.getLocalUri());
            downloadPromise.resolve(localUri);
        } else {
            this.downloadPromise = downloadPromise;
            if (getReactApplicationContext() != null) {
                getReactApplicationContext().
                        registerReceiver(receiver, new IntentFilter(
                                DownloadManager.ACTION_DOWNLOAD_COMPLETE));

                dm = (DownloadManager) getReactApplicationContext()
                        .getSystemService(getReactApplicationContext().DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(uri));
                request.setVisibleInDownloadsUi(false);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
                request.allowScanningByMediaScanner();

                enqueue = dm.enqueue(request);
            }
        }
    }

}