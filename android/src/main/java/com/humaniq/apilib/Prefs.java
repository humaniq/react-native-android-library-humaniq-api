package com.humaniq.apilib;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
  private static final String URI = "URI";
  private static final String LOCAL = "LOCAL_URI";
  private static final String APP_PREFERENCES = "humaniq_java_api_prefs";
  private static final String IS_DOWNLOADING = "IS_DOWNLOADING";
  private static final String DOWNLOAD_ID = "DOWNLOAD_ID";
  private static SharedPreferences sharedPreferences;

  public Prefs(Context context) {
    sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
  }

  public static void saveDownloadedUri(String uri) {
    sharedPreferences.edit().putString(URI, uri).commit();
  }

  public static void saveLocalUri(String uri) {
    sharedPreferences.edit().putString(LOCAL, uri).commit();
  }

  public static String getDownloadedUri() {
    return sharedPreferences.getString(URI, null);
  }

  public static boolean isDownloading() {
    return sharedPreferences.getBoolean(IS_DOWNLOADING, false);
  }

  public static void setDownloading(boolean downloading) {
    sharedPreferences.edit().putBoolean(IS_DOWNLOADING, downloading).commit();
  }

  public static String getLocalUri() {
    return sharedPreferences.getString(LOCAL, null);
  }

  public static long getDownloadId() {
    return sharedPreferences.getLong(DOWNLOAD_ID, 0);
  }

  public static boolean isUriAlreadyDownloaded(String uri) {
    return uri.equals(getDownloadedUri());
  }

  public static void saveDownloadId(long enqueue) {
    sharedPreferences.edit().putLong(DOWNLOAD_ID, enqueue).commit();
  }
}