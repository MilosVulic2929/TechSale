package com.metropolitan.techsale;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.metropolitan.techsale.auth.LoginActivity;
import com.metropolitan.techsale.auth.RegistrationActivity;
import com.metropolitan.techsale.items.ItemListActivity;
import com.metropolitan.techsale.settings.SettingsActivity;
import com.metropolitan.techsale.shoppingcart.ShoppingCart;
import com.metropolitan.techsale.utils.ExtraKeys;
import com.metropolitan.techsale.utils.PreferenceKeys;
import com.metropolitan.techsale.utils.Utils;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static MainActivity mainActivity;

    public static String oldValue = "euro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Utils.setStyleTheme(preferences, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        oldValue = Utils.getPreferenceCurrency(this);
        Log.d("tagic", "Old value: " + oldValue);
        mainActivity = this;
        // TODO info za ficu, nemoj pozivas ovu metodicu ako oces da ti se prikazuje mainActivity jer kad se jednom ulogujes posle ce samo cepa na itemList jer sam gha save u preference,
        // TODO to znaci da ce mora ubacimo logOut negde;
        checkTokenExsistance();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ShoppingCart.getInstance(this).save();
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
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void onClickLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void onClickAsGuest(View view) {
        Intent intent = new Intent(this, ItemListActivity.class).putExtra(ExtraKeys.EXTRA_KEY_GUEST, true);
        startActivity(intent);
    }

    private void checkTokenExsistance(){
        SharedPreferences sharedPref = this.getSharedPreferences(PreferenceKeys.PREFERENCES_NAME, Context.MODE_PRIVATE);
        String tokence = sharedPref.getString(PreferenceKeys.AUTH_TOKEN,"");
        if(!Objects.requireNonNull(tokence).isEmpty()){
            // TODO info za ficu , ne znam da li treba da se metne ovde ispod put ekstra
            Intent intent = new Intent(this, ItemListActivity.class).putExtra(ExtraKeys.EXTRA_KEY_GUEST, true);
            startActivity(intent);
        }
    }
}
