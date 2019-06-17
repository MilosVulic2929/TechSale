package com.metropolitan.techsale.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.metropolitan.techsale.R;

public class Utils {
    public static void setStyleTheme(SharedPreferences preferences, Context context) {
        if (preferences.getBoolean("themeKey", false)) {
            context.setTheme(R.style.DarkTheme);
        } else {
            context.setTheme(R.style.LightTheme);
        }
    }
}
