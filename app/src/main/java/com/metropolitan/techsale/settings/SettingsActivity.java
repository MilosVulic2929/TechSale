package com.metropolitan.techsale.settings;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.metropolitan.techsale.R;
import com.metropolitan.techsale.utils.Utils;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Utils.setStyleTheme(preferences, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        showSettings();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void showSettings() {
        getSupportFragmentManager().beginTransaction().replace(R.id.settingsActivity, new SettingsFragment()).commit();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.settings));
    }
}
