package com.humaniq.apilib.react;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import com.facebook.common.internal.Files;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.humaniq.apilib.Constants;
import com.humaniq.apilib.storage.Prefs;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

/*
       ToastModule helps to download files to internal storage and gives URI to react native callback
 */

public class DownloadModule extends ReactContextBaseJavaModule {


  private long enqueue;
  private DownloadManager dm;
  private Promise downloadPromise;
  private String downloadUri;
  private final ScheduledThreadPoolExecutor executor =
      new ScheduledThreadPoolExecutor(5);
  private Runnable progressRunnable;


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
    progressRunnable  = new Runnable() {
      @Override
      public void run() {
        checkProgress();
      }
    };

    this.executor.scheduleWithFixedDelay(progressRunnable, 1L, 2, TimeUnit.SECONDS);

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

        executor.remove(progressRunnable);

        Prefs.setDownloading(false);
      } else if (DownloadManager.STATUS_FAILED == c.getInt(columnIndex)) {
        downloadPromise.reject(new Throwable("error"));
        executor.remove(progressRunnable);
      }
    }
  }



  /*
Sends an event OF PROGRESS CHANGED to the JS module.
*/
  private void sendEvent(@Nullable WritableMap params) {
    this.getReactApplicationContext().
        getJSModule(DeviceEventManagerModule.
            RCTDeviceEventEmitter.class)
        .emit(Constants.EVENT_PROGRESS_CHANGED, params);
  }

  /**
   * Checks download progress.
   */
  private void checkProgress() {
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
        WritableMap writableMap = new WritableNativeMap();
        writableMap.putInt("progress", progress);
        sendEvent(writableMap);

    if(progress == 100) {
      executor.remove(progressRunnable);
    }

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