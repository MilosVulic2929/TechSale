package com.metropolitan.techsale.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;

import com.metropolitan.techsale.MainActivity;
import com.metropolitan.techsale.R;
import com.metropolitan.techsale.utils.Utils;

import java.util.Objects;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SwitchPreferenceCompat switchThemePref;
    private SharedPreferences preferences;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.preferences, s);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        switchThemePref = (SwitchPreferenceCompat) findPreference("themeKey");

        switchThemePref.setOnPreferenceChangeListener((preference, o) -> {
            Utils.setStyleTheme(preferences, getActivity());
            Objects.requireNonNull(getActivity()).recreate();
            MainActivity.mainActivity.recreate();
            return true;
        });
    }
}
