package com.metropolitan.techsale.auth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.metropolitan.techsale.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    }

    public void onClickLogin(View view){
        //TODO on login go to ListItemActivity
        // Save user (token) to preferences
    }
}
