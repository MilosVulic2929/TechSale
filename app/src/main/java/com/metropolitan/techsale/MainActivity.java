package com.metropolitan.techsale;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.metropolitan.techsale.auth.RegistrationActivity;
import com.metropolitan.techsale.items.ItemListActivity;
import com.metropolitan.techsale.settings.SettingsActivity;
import com.metropolitan.techsale.utils.ExtraKeys;
import com.metropolitan.techsale.utils.Utils;

public class MainActivity extends AppCompatActivity {

    public static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Utils.setStyleTheme(preferences, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickRegister(View view) {
        //TODO start register activity
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void onClickLogin(View view) {
        //TODO start register activity
    }

    public void onClickAsGuest(View view) {
        Intent intent = new Intent(this, ItemListActivity.class).putExtra(ExtraKeys.EXTRA_KEY_GUEST, true);
        startActivity(intent);
    }
}
