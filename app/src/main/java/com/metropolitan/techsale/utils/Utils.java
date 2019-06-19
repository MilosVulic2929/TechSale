package com.metropolitan.techsale.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.metropolitan.techsale.R;

import java.util.Objects;

public class Utils {

    public static String json = "";

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
}
