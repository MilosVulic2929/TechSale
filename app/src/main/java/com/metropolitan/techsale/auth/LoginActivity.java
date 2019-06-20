package com.metropolitan.techsale.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.metropolitan.techsale.R;
import com.metropolitan.techsale.items.ItemListActivity;
import com.metropolitan.techsale.utils.ExtraKeys;
import com.metropolitan.techsale.utils.PreferenceKeys;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameEditText = findViewById(R.id.editTextUsernameLogin);
        passwordEditText = findViewById(R.id.editTextPasswordLogin);

    }

    public void onClickLogin(View view) {
        //TODO on login go to ListItemActivity
        // Save user (token) to preferences
        if (validateInput()) {
            UserDetails userDetails = new UserDetails(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            AuthService authService = new AuthServiceImpl().getAuthService();
            authService.login(userDetails).enqueue(new Callback<LoginResult>() {
                @Override
                public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                    Log.d("random_tag", "Code:" + response.code());
                    if (response.isSuccessful()) {
                        Log.d("random_tag", "tokence je " + response.body().getToken());
                        Log.d("random_tag", "username je " + response.body().getUsername());
                        Toast.makeText(getApplicationContext(), "User je uspesno ulogovan", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
                        startActivity(intent);
                        Context context = getApplicationContext();
                        SharedPreferences sharedPref = context.getSharedPreferences(PreferenceKeys.PREFERENCES_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(PreferenceKeys.AUTH_TOKEN, response.body().getToken());
                        editor.apply();
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
            Toast.makeText(getApplicationContext(), "Nisu popunjena sva polja", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInput() {
        return !usernameEditText.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty();
    }
}
