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
    private TextView textViewTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Utils.setStyleTheme(preferences, this);
        setContentView(R.layout.activity_payment);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        textView = findViewById(R.id.textViewRegistrationWizard);
        setTotalPrice(this.getIntent().getDoubleExtra("Total", 0.0), preferences);

        //TODO info - dodat total price
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);
        double total = 0;
        for (Item i : ShoppingCart.getInstance(this).getItems())
            total += i.getPrice();

        textViewTotalPrice.setText(String.format("Total Price: %s", total));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void setTotalPrice(double price, SharedPreferences preferences) {
        if (Objects.equals(preferences.getString("currencyKey", "euro"), "dollar")) {
            Log.d("tagic", "probala " + price);
            textView.setText("Total price " + price + " $");
        } else if (Objects.equals(preferences.getString("currencyKey", "euro"), "pound")) {
            Log.d("tagic", "probala " + price);
            textView.setText("Total price " + price + " £");
        } else if (Objects.equals(preferences.getString("currencyKey", "euro"), "ruble")) {
            Log.d("tagic", "probala " + price);
            textView.setText("Total price " + price + " \u20BD");
        } else {
            Log.d("tagic", "probala " + price);
            textView.setText("Total price " + price + " €");
        }
    }
}
