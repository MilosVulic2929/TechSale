package com.metropolitan.techsale.settings;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.metropolitan.techsale.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.preferences, s);
    }
}
