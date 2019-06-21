package com.metropolitan.techsale.auth;

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
import android.widget.EditText;
import android.widget.Toast;

import com.metropolitan.techsale.MainActivity;
import com.metropolitan.techsale.R;
import com.metropolitan.techsale.items.ItemListActivity;
import com.metropolitan.techsale.orderlist.OrderListActivity;
import com.metropolitan.techsale.settings.SettingsActivity;
import com.metropolitan.techsale.shoppingcart.ShoppingCart;
import com.metropolitan.techsale.utils.PreferenceKeys;
import com.metropolitan.techsale.utils.Utils;


import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    public static LoginActivity loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Utils.setStyleTheme(preferences, this);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        usernameEditText = findViewById(R.id.editTextUsernameLogin);
        passwordEditText = findViewById(R.id.editTextPasswordLogin);
        loginActivity = this;
    }

    public void onClickLogin(View view) {
        if (validateInput()) {
            UserDetails userDetails = new UserDetails(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            AuthService authService = new AuthServiceImpl().getAuthService();
            authService.login(userDetails).enqueue(new Callback<LoginResult>() {
                @Override
                public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                    Log.d("random_tag", "Code:" + response.code());
                    if (response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "User je uspesno ulogovan", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
                        startActivity(intent);
                        Context context = getApplicationContext();
                        SharedPreferences sharedPref = context.getSharedPreferences(PreferenceKeys.PREFERENCES_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(PreferenceKeys.AUTH_TOKEN, response.body().getToken());
                        editor.putString(PreferenceKeys.AUTH_USERNAME, response.body().getUsername());
                        editor.apply();
                        invalidateOptionsMenu();
                    } else {
                        Toast.makeText(getApplicationContext(), "User nije uspesno ulogovan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResult> call, Throwable t) {
                    Log.d("random_tag", "Error:" + t.toString());
                    t.printStackTrace();
                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "Login failed, please try again", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickRegister(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    private boolean validateInput() {
        return !usernameEditText.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
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
}
