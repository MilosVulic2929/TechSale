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
import com.metropolitan.techsale.orderlist.OrderListActivity;
import com.metropolitan.techsale.settings.SettingsActivity;
import com.metropolitan.techsale.shoppingcart.ShoppingCart;
import com.metropolitan.techsale.utils.PreferenceKeys;
import com.metropolitan.techsale.utils.Utils;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText username;
    private EditText repeatPassword;
    private EditText firstName;
    private EditText lastName;
    public static RegistrationActivity registrationActivity;

    private Pattern emailRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Utils.setStyleTheme(preferences, this);
        setContentView(R.layout.activity_registration);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        registrationActivity = this;

        password = findViewById(R.id.editTextPassword);
        repeatPassword = findViewById(R.id.editTextPaswordConfirmation);
        email = findViewById(R.id.editTextEmail);
        firstName = findViewById(R.id.editTextFirstName);
        lastName = findViewById(R.id.editTextLastName);
        username = findViewById(R.id.editTextUsername);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void register(View view) {
        if (credentialsValidation()) {
            register();
        }
    }

    private boolean credentialsValidation() {
        if(true)
            return true;
        /*
        if (password.getText().length() > 6 && firstName.getText().length() > 4 && lastName.getText().length() > 4 && emailRegexValidation()) {
            if (!password.getText().toString().equals(repeatPassword.getText().toString())) {
                Toast.makeText(this, "Password fields are not identical", Toast.LENGTH_SHORT).show();
            }
            return true;
        }*/
        Toast.makeText(this, "Something doesn't fits registration standards", Toast.LENGTH_SHORT).show();
        return false;
    }

    private boolean emailRegexValidation() {
        Matcher matcher = emailRegex.matcher(email.getText().toString());
        return matcher.find();
    }

    private void register() {
        User user = new User("0", email.getText().toString(), username.getText().toString(), password.getText().toString(), firstName.getText().toString(), lastName.getText().toString());
        AuthService authService = new AuthServiceImpl().getAuthService();
        authService.register(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d("random_tag", "Code:" + response.code());
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Registration completed", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Registration failed, please try again", Toast.LENGTH_SHORT).show();
            }
        });
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
