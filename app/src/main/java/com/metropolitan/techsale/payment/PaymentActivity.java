package com.metropolitan.techsale.payment;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.metropolitan.techsale.R;
import com.metropolitan.techsale.items.model.Item;
import com.metropolitan.techsale.shoppingcart.ShoppingCart;
import com.metropolitan.techsale.utils.Utils;

import java.util.Objects;
public class PaymentActivity extends AppCompatActivity {

    private TextView textViewTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Utils.setStyleTheme(preferences, this);
        setContentView(R.layout.activity_payment);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

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
}
