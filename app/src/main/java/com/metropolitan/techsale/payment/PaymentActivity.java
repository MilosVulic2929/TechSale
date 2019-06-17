package com.metropolitan.techsale.payment;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.metropolitan.techsale.R;

import java.util.Objects;

public class PaymentActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        textView = findViewById(R.id.textViewRegistrationWizard);
        setTotalPrice(400, preferences);
    }

    private void setTotalPrice(int price, SharedPreferences preferences) {
        if (Objects.equals(preferences.getString("currencyKey", "euro"), "dollar")) {
            textView.setText("Total price " + price + "$");
        } else if (Objects.equals(preferences.getString("currencyKey", "euro"), "pound")) {
            textView.setText("Total price " + price + "£");
        } else if (Objects.equals(preferences.getString("currencyKey", "euro"), "ruble")) {
            textView.setText("Total price " + price + "\u20BD");
        } else {
            textView.setText("Total price " + price + "€");
        }
    }
}
