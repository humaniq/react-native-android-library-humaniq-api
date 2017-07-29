package com.humaniq.apilib.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class Prefs {
  private static final String URI = "URI";
  private static final String LOCAL = "LOCAL_URI";
  private static final String APP_PREFERENCES = "humaniq_java_api_prefs";
  private static final String IS_DOWNLOADING = "IS_DOWNLOADING";
  private static final String DOWNLOAD_ID = "DOWNLOAD_ID";
  private static final String JWT_TOKEN = "JWT_TOKEN";
  private static final String FCM_TOKEN = "FCM_TOKEN";
  private static final String ACCOUNT_ID = "ACCOUNT_ID";
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

  public static void saveJwtToken(String token) {
    sharedPreferences.edit().putString(JWT_TOKEN, token).commit();
  }

  public static String getJwtToken() {
    return sharedPreferences.getString(JWT_TOKEN, null);
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

  public static void saveAccountId(String accountId) {
    sharedPreferences.edit().putString(ACCOUNT_ID, accountId).commit();
  }

  public static boolean hasToken() {
    return !TextUtils.isEmpty(sharedPreferences.getString(JWT_TOKEN, null));
  }

  public static void clearJwtToken() {
    sharedPreferences.edit().putString(JWT_TOKEN, null).commit();
  }

  public static void saveFCMToken(String fcm_token) {
    sharedPreferences.edit().putString(FCM_TOKEN, fcm_token).commit();
  }

  public static String getFCMToken() {
    return sharedPreferences.getString(FCM_TOKEN, null);
  }

  public static String getAccountId() {
    return sharedPreferences.getString(ACCOUNT_ID, null);
  }
}