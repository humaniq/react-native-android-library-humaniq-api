package com.humaniq.apilib;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
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

import static android.os.Environment.DIRECTORY_DOWNLOADS;

/*
       Module helps to download files to internal storage and gives URI to react native callback
 */

public class DownloadModule extends ReactContextBaseJavaModule {
  private static final int PROGRESS_DELAY = 500;
  Handler handler;
  private long enqueue;
  private DownloadManager dm;
  private Promise downloadPromise;
  private boolean isProgressCheckerRunning = false;
  private String downloadUri;
  /**
   * Checks download progress and updates status, then re-schedules itself.
   */
  private Runnable progressChecker = new Runnable() {
    @Override public void run() {
      try {
        checkProgress();
      } finally {
        handler.postDelayed(progressChecker, PROGRESS_DELAY);
      }
    }
  };
  BroadcastReceiver receiver = new BroadcastReceiver() {
    @Override public void onReceive(Context context, Intent intent) {
      String action = intent.getAction();
      if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
        saveToLocalStorage();
      }
    }
  };

  public DownloadModule(ReactApplicationContext reactContext) {
    super(reactContext);
    new Prefs(reactContext);
    //        Looper.prepare();
    //        handler = new Handler();
  }

  private void saveToLocalStorage() {
    DownloadManager.Query query = new DownloadManager.Query();
    query.setFilterById(enqueue);
    Cursor c = dm.query(query);
    if (c.moveToFirst()) {
      int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
      if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {

        String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));

        Prefs.saveDownloadedUri(downloadUri);

        final String fileName = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));

        File file = new File(Uri.parse(uriString).getPath());

        File file1 = new File(getReactApplicationContext().getFilesDir(), fileName);
        if (!file1.exists()) {
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

        if (!file.delete()) {
          file.delete();
        }
        //
        File outFile = new File(getReactApplicationContext().getFilesDir(), fileName);
        WritableMap writableMap = new WritableNativeMap();
        writableMap.putString("uri", outFile.getAbsolutePath());

        Prefs.saveLocalUri(outFile.getAbsolutePath());
        downloadPromise.resolve(writableMap);

        stopProgressChecker();

        Prefs.setDownloading(false);
      } else if (DownloadManager.STATUS_FAILED == c.getInt(columnIndex)) {
        downloadPromise.reject(new Throwable("error"));
        stopProgressChecker();
      }
    }
  }

  /*
Sends an event to the JS module.
*/
  private void sendEvent(String eventName, @Nullable WritableMap params) {
    this.getReactApplicationContext().
        getJSModule(DeviceEventManagerModule.
            RCTDeviceEventEmitter.class).emit(eventName, params);
  }

  /**
   * Checks download progress.
   */
  private void checkProgress() {
    handler.postDelayed(progressChecker, PROGRESS_DELAY);

    DownloadManager.Query query = new DownloadManager.Query();
    query.setFilterById(enqueue);
    Cursor cursor = dm.query(query);
    if (!cursor.moveToFirst()) {
      cursor.close();
      return;
    }
    long reference = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));

    int bytes_downloaded =
        cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
    int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

    int progress = (int) ((bytes_downloaded * 100l) / bytes_total);
    //        Toast.makeText(getReactApplicationContext(), "progress: " + (++progress), Toast.LENGTH_SHORT)
    //                .show();
    final int finalProgress = progress;
    getReactApplicationContext().runOnUiQueueThread(new Runnable() {
      @Override public void run() {
        WritableMap writableMap = new WritableNativeMap();
        writableMap.putInt("progress", finalProgress);
        sendEvent("progress", writableMap);
      }
    });
  }

  /**
   * Starts watching download progress.
   * <p>
   * This method is safe to call multiple times. Starting an already running progress checker is a
   * no-op.
   */
  private void startProgressChecker() {
    if (!isProgressCheckerRunning) {
      progressChecker.run();
      isProgressCheckerRunning = true;
    }
  }

  /**
   * Stops watching download progress.
   */
  private void stopProgressChecker() {
    handler.removeCallbacks(progressChecker);
    isProgressCheckerRunning = false;
  }

  @Override public String getName() {
    return "HumaniqDownloadFileLib";
  }

  @Override public Map<String, Object> getConstants() {
    return new HashMap<>();
  }

  @ReactMethod public void downloadVideoFile(String uri, final Promise downloadPromise) {
    this.downloadUri = uri;
    this.downloadPromise = downloadPromise;
    dm = (DownloadManager) getReactApplicationContext().
        getSystemService(Context.DOWNLOAD_SERVICE);

    getReactApplicationContext().
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    if (Prefs.isUriAlreadyDownloaded(uri)) {
      //            Toast.makeText(getReactApplicationContext(), Prefs.getLocalUri(), Toast.LENGTH_SHORT).show();
      WritableMap localUri = new WritableNativeMap();
      localUri.putString("uri", Prefs.getLocalUri());
      downloadPromise.resolve(localUri);
    } else {
      // if file downloaded in background, save it to internal storage
      Cursor c = dm.query(new DownloadManager.Query().setFilterById(Prefs.getDownloadId()));
      if (c.moveToFirst()) {
        int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
        switch (status) {
          case DownloadManager.STATUS_PAUSED:
            break;
          case DownloadManager.STATUS_PENDING:
            break;
          case DownloadManager.STATUS_RUNNING:
            break;
          case DownloadManager.STATUS_SUCCESSFUL:
            Prefs.setDownloading(false);
            enqueue = Prefs.getDownloadId();
            saveToLocalStorage();
            break;
          case DownloadManager.STATUS_FAILED:
            break;
        }
      }

      if (!Prefs.isDownloading()) {
        if (!Prefs.isUriAlreadyDownloaded(uri)) {
          Prefs.setDownloading(true);
          //                    Toast.makeText(getReactApplicationContext(), "download", Toast.LENGTH_SHORT).show();
          DownloadManager.Request request = new DownloadManager.Request(Uri.parse(uri));
          request.setVisibleInDownloadsUi(false);
          request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);

          request.setDestinationInExternalFilesDir(getReactApplicationContext(),
              DIRECTORY_DOWNLOADS, "instruction.mp4");

          enqueue = dm.enqueue(request);
          Prefs.saveDownloadId(enqueue);
        }
      }
    }
  }
}