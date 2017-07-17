package com.humaniq.apilib;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private static final String URI = "URI";
    private static final String LOCAL = "LOCAL_URI";
    private static final String APP_PREFERENCES = "humaniq_java_api_prefs";
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


    public static String getLocalUri() {
        return sharedPreferences.getString(LOCAL, null);
    }

    public static boolean isUriAlreadyDownloaded(String uri) {
        return uri.equals(getDownloadedUri());
    }



}