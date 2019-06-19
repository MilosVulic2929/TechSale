package com.metropolitan.techsale.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.metropolitan.techsale.R;

import java.util.Objects;

public class Utils {

    public static String json = "";

    public static String getPreferenceCurrency(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString("currencyKey", "euro");
    }

    public static void setStyleTheme(SharedPreferences preferences, Context context) {

        if (preferences.getBoolean("themeKey", false)) {
            context.setTheme(R.style.DarkTheme);
        } else {
            context.setTheme(R.style.LightTheme);
        }
    }

    public static String setCurrencyTag(SharedPreferences preferences) {
        if (Objects.equals(preferences.getString("currencyKey", "euro"), "dollar")) {
            return "$";
        } else if (Objects.equals(preferences.getString("currencyKey", "euro"), "pound")) {
            return "£";
        } else if (Objects.equals(preferences.getString("currencyKey", "euro"), "ruble")) {
            return "\u20BD";
        } else {
            return "€";
        }
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }
}
