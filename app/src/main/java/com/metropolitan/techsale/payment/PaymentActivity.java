package com.metropolitan.techsale.payment;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.metropolitan.techsale.R;
import com.metropolitan.techsale.items.model.Item;
import com.metropolitan.techsale.shoppingcart.ShoppingCart;
import com.metropolitan.techsale.utils.Utils;

import java.util.Objects;


public class PaymentActivity extends AppCompatActivity {

    private TextView textView;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Utils.setStyleTheme(preferences, this);
        setContentView(R.layout.activity_payment);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        textView = findViewById(R.id.textViewTotalPrice);

        double total = 0;
        for (Item i : ShoppingCart.getInstance(this).getItems())
            total += i.getPrice();
        setTotalPrice(total, preferences);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void setTotalPrice(double price, SharedPreferences preferences) {
        if (Objects.equals(preferences.getString("currencyKey", "euro"), "dollar")) {
            Log.d("tagic", "probala " + price);
            textView.setText(String.format("Total price: %.2f $", price));
        } else if (Objects.equals(preferences.getString("currencyKey", "euro"), "pound")) {
            Log.d("tagic", "probala " + price);
            textView.setText(String.format("Total price: %.2f £", price));
        } else if (Objects.equals(preferences.getString("currencyKey", "euro"), "ruble")) {
            Log.d("tagic", "probala " + price);
            textView.setText(String.format("Total price: %.2f \u20BD", price));
        } else {
            Log.d("tagic", "probala " + price);
            textView.setText(String.format("Total price: %.2f €", price));
        }
    }
}
