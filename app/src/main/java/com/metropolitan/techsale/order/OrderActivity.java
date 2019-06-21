package com.metropolitan.techsale.order;

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
import android.widget.TextView;
import android.widget.Toast;

import com.metropolitan.techsale.MainActivity;
import com.metropolitan.techsale.R;
import com.metropolitan.techsale.auth.LoginActivity;
import com.metropolitan.techsale.items.model.Item;
import com.metropolitan.techsale.settings.SettingsActivity;
import com.metropolitan.techsale.shoppingcart.ShoppingCart;
import com.metropolitan.techsale.utils.PreferenceKeys;
import com.metropolitan.techsale.utils.Utils;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.util.stream.Collectors.*;

public class OrderActivity extends AppCompatActivity {

    private TextView textViewAddress;
    private TextView textViewPhone;
    private TextView textViewFirstName;
    private TextView textViewLastName;
    private TextView textViewTotalPrice;
    public static OrderActivity orderActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Utils.setStyleTheme(preferences, this);
        setContentView(R.layout.activity_order);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        orderActivity = this;
        invalidateOptionsMenu();

        textViewAddress = findViewById(R.id.editTextAddress);
        textViewPhone = findViewById(R.id.editTextPhone);
        textViewFirstName = findViewById(R.id.editTextFirstName);
        textViewLastName = findViewById(R.id.editTextLastName);
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);

        String defaultAddress = preferences.getString("addressKey", "");
        textViewAddress.setText(defaultAddress);

        double total = 0;
        for (Item i : ShoppingCart.getInstance(this).getItems())
            total += i.getPrice();
        setTotalPrice(total, preferences);

    }


    public void onClickCreateOrder(View view) {
        if (isInputValid()) {
            SharedPreferences sharedPreferences = this.getSharedPreferences(PreferenceKeys.PREFERENCES_NAME, MODE_PRIVATE);
            String username = sharedPreferences.getString(PreferenceKeys.AUTH_USERNAME, "");
            String token = sharedPreferences.getString(PreferenceKeys.AUTH_TOKEN, "");

            if (!Objects.requireNonNull(token).isEmpty() && !Objects.requireNonNull(username).isEmpty()) {
                OrderDTO order = new OrderDTO(ShoppingCart.getInstance(this).getItems().stream().map(Item::getId).collect(toList()),
                        username, textViewAddress.getText().toString(), textViewPhone.getText().toString(),
                        textViewFirstName.getText().toString(), textViewLastName.getText().toString());
                new OrderServiceImpl().getOrderService().saveOrder("Bearer " + token, order).enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        Log.d("random_tag", "Order code: " + response.code() + ", success: " + response.isSuccessful());
                        if (response.isSuccessful()) {
                            Toast.makeText(OrderActivity.this, "Order is saved",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(OrderActivity.this, "Wait why mate",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {
                        Log.d("random_tag", t.getMessage());
                        Toast.makeText(OrderActivity.this, "Failed to create order, please try again",
                                Toast.LENGTH_SHORT).show();

                    }
                });
            } else {
                Toast.makeText(this, "You must be logged in",
                        Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "You must fill in all fields",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isInputValid() {
        return !textViewAddress.getText().toString().isEmpty() &&
                !textViewPhone.getText().toString().isEmpty() &&
                !textViewFirstName.getText().toString().isEmpty() &&
                !textViewLastName.getText().toString().isEmpty() &&
                ShoppingCart.getInstance(this).getItems().size() > 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        // TODO info za ficu - ovo metni na taj activity orderlist kad ga pravis i gore u onCreate jos jedan poziv imas - ovo pozovi invalidateOptionsMenu() u on create tamo
        if(!Utils.checkHidingLogoutItem(this)){
            menu.findItem(R.id.action_logout).setVisible(false);
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
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void setTotalPrice(double price, SharedPreferences preferences) {
        if (Objects.equals(preferences.getString("currencyKey", "euro"), "dollar")) {
            Log.d("tagic", "probala " + price);
            textViewTotalPrice.setText(String.format("Total price: %.2f $", price));
        } else if (Objects.equals(preferences.getString("currencyKey", "euro"), "pound")) {
            Log.d("tagic", "probala " + price);
            textViewTotalPrice.setText(String.format("Total price: %.2f £", price));
        } else if (Objects.equals(preferences.getString("currencyKey", "euro"), "ruble")) {
            Log.d("tagic", "probala " + price);
            textViewTotalPrice.setText(String.format("Total price: %.2f \u20BD", price));
        } else {
            Log.d("tagic", "probala " + price);
            textViewTotalPrice.setText(String.format("Total price: %.2f €", price));
        }
    }

}
