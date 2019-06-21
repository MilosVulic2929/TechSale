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
import com.metropolitan.techsale.orderlist.OrderListActivity;
import com.metropolitan.techsale.settings.SettingsActivity;
import com.metropolitan.techsale.shoppingcart.ShoppingCart;
import com.metropolitan.techsale.utils.PreferenceKeys;
import com.metropolitan.techsale.utils.Utils;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static MainActivity mainActivity;

    public static String oldValue = "euro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Utils.setStyleTheme(preferences, this);
        setContentView(R.layout.activity_main);
        oldValue = Utils.getPreferenceCurrency(this);
        Log.d("tagic", "Old value: " + oldValue);
        mainActivity = this;
        // TODO info vulic - ubacio sam u options menu logout
        checkTokenExsistance();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ShoppingCart.getInstance(this).save();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("tagic", "evo u on resume sam konacno");
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        if (!Utils.checkHidingLogoutItem(this)) {
            menu.findItem(R.id.action_logout).setVisible(false);
            menu.findItem(R.id.action_orders).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (item.getItemId() == R.id.action_logout) {
            SharedPreferences sharedPref = this.getSharedPreferences(PreferenceKeys.PREFERENCES_NAME, Context.MODE_PRIVATE);
            sharedPref.edit()
                    .putString(PreferenceKeys.AUTH_TOKEN, "")
                    .putString(PreferenceKeys.AUTH_USERNAME, "")
                    .apply();
            ShoppingCart.getInstance(this).removeAll();
            startActivity(new Intent(this, MainActivity.class));
        }   else if(item.getItemId() == R.id.action_orders){
            startActivity(new Intent(this, OrderListActivity.class));
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
        Intent intent = new Intent(this, ItemListActivity.class);
        startActivity(intent);
    }

    private void checkTokenExsistance(){
        SharedPreferences sharedPref = this.getSharedPreferences(PreferenceKeys.PREFERENCES_NAME, Context.MODE_PRIVATE);
        String token = sharedPref.getString(PreferenceKeys.AUTH_TOKEN,"");
        if(!Objects.requireNonNull(token).isEmpty()){
            Intent intent = new Intent(this, ItemListActivity.class);
            startActivity(intent);
        }
    }
}
