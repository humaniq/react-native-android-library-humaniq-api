package com.humaniq.apilib;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.facebook.common.internal.Files;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.os.Environment.DIRECTORY_DOWNLOADS;


public class DownloadModule extends ReactContextBaseJavaModule {
    private long enqueue;
    private DownloadManager dm;
    private Promise downloadPromise;

    private static final int PROGRESS_DELAY = 1000;
    Handler handler;
    private boolean isProgressCheckerRunning = false;
    private String downloadUri;

    public DownloadModule(ReactApplicationContext reactContext) {
        super(reactContext);
        new Prefs(reactContext);
//        handler = new Handler();
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

                        Prefs.saveDownloadedUri(downloadUri);

                        final String fileName =
                                c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));

                        File file = new File(Uri.parse(uriString).getPath());

                        File file1 = new File(
                                getReactApplicationContext().getCacheDir(), fileName);
                        if(!file1.exists()) {
                            try {
                                file1.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        FileOutputStream outputStream = null;
                        try {
                            outputStream = new FileOutputStream(file1);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        try {
                            outputStream.write(Files.toByteArray(file));
                            outputStream.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if(!file.delete()) {
                            file.delete();

                        }
//
                        File outFile = new File(
                                getReactApplicationContext()
                                        .getCacheDir(), fileName);
                        WritableMap writableMap = new WritableNativeMap();
                        writableMap.putString("uri", outFile.getAbsolutePath());

                        Prefs.saveLocalUri(outFile.getAbsolutePath());
                        downloadPromise.resolve(writableMap);

                        stopProgressChecker();


                    } else if(DownloadManager.STATUS_FAILED == c
                            .getInt(columnIndex)) {
                        downloadPromise.reject(new Throwable("error"));
                        stopProgressChecker();
                    }
                }
            }
        }
    };


    /*
Sends an event to the JS module.
 */
    private void sendEvent(String eventName, @Nullable WritableMap params) {
        this.getReactApplicationContext().
                getJSModule(DeviceEventManagerModule.
                        RCTDeviceEventEmitter.class).emit("RNFileUploader-" + eventName, params);
    }


    /**
     * Checks download progress.
     */
    private void checkProgress() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterByStatus(~(DownloadManager.STATUS_FAILED | DownloadManager.STATUS_SUCCESSFUL));
        Cursor cursor = dm.query(query);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return;
        }
        do {
            long reference = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
            final long progress = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            // do whatever you need with the progress
//            WritableMap writableMap = new WritableNativeMap();
//            writableMap.putString("uri", getReactApplicationContext()
//                    .getFilesDir() + "/" + fileName);
            getReactApplicationContext().runOnUiQueueThread(new Runnable() {
                @Override
                public void run() {
                    WritableMap writableMap = new WritableNativeMap();
                    writableMap.putInt("progress", (int) progress);
                    sendEvent("progress", writableMap);

                }
            });
        } while (cursor.moveToNext());
        cursor.close();
    }

    /**
     * Starts watching download progress.
     *
     * This method is safe to call multiple times. Starting an already running progress checker is a no-op.
     */
    private void startProgressChecker() {
//        if (!isProgressCheckerRunning) {
//            progressChecker.run();
//            isProgressCheckerRunning = true;
//        }
    }

    /**
     * Stops watching download progress.
     */
    private void stopProgressChecker() {
//        handler.removeCallbacks(progressChecker);
//        isProgressCheckerRunning = false;
    }

    /**
     * Checks download progress and updates status, then re-schedules itself.
     */
    private Runnable progressChecker = new Runnable() {
        @Override
        public void run() {
            try {
                checkProgress();
            } finally {
                handler.postDelayed(progressChecker, PROGRESS_DELAY);
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
        this.downloadUri = uri;
        if(Prefs.isUriAlreadyDownloaded(uri)) {
            Toast.makeText(getReactApplicationContext(), Prefs.getLocalUri(), Toast.LENGTH_SHORT).show();
            WritableMap localUri = new WritableNativeMap();
            localUri.putString("uri", Prefs.getLocalUri());
            downloadPromise.resolve(localUri);
        } else {
            Toast.makeText(getReactApplicationContext(), "download", Toast.LENGTH_SHORT).show();
            this.downloadPromise = downloadPromise;
            if (getReactApplicationContext() != null) {

                getReactApplicationContext().
                        registerReceiver(receiver, new IntentFilter(
                        DownloadManager.ACTION_DOWNLOAD_COMPLETE));

                dm = (DownloadManager)getReactApplicationContext()
                .getSystemService(DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse("https://archive.org/download/1mbFile/1mb.mp4"));
                request.setVisibleInDownloadsUi(false);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
                request.allowScanningByMediaScanner();

                request.setDestinationInExternalFilesDir(getReactApplicationContext(), DIRECTORY_DOWNLOADS,
                        "instruction.mp4");


                enqueue = dm.enqueue(request);

                startProgressChecker();
            }
        }
    }

}