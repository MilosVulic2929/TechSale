package com.metropolitan.techsale.auth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.metropolitan.techsale.R;

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


    private Pattern emailRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
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
        if (password.getText().length() > 6 && firstName.getText().length() > 4 && lastName.getText().length() > 4 && emailRegexValidation()) {
            if (!password.getText().toString().equals(repeatPassword.getText().toString())) {
                Toast.makeText(this, "Password fields are not identical", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
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
                    Toast.makeText(getApplicationContext(), "User je registrovan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("random_tag", "Error:" + t.toString());
                t.printStackTrace();
            }
        });
    }
}
