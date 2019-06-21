package com.metropolitan.techsale.shoppingcart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.metropolitan.techsale.MainActivity;
import com.metropolitan.techsale.R;
import com.metropolitan.techsale.items.model.Item;
import com.metropolitan.techsale.order.OrderActivity;

import com.metropolitan.techsale.orderlist.OrderListActivity;
import com.metropolitan.techsale.settings.SettingsActivity;
import com.metropolitan.techsale.utils.PreferenceKeys;
import com.metropolitan.techsale.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import mva2.adapter.ListSection;
import mva2.adapter.MultiViewAdapter;

public class ShoppingCartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView textTotal;
    private List<Item> itemList = new ArrayList<>();
    private ListSection<Item> itemSection;
    public static ShoppingCartActivity shoppingCartActivity;

    private Consumer<List<Item>> cartListener = (items -> {
        double total = 0;
        for (Item i : items) {
            total += i.getPrice();
        }
        itemSection.clear();
        itemSection.addAll(items);
        if (textTotal != null)
            textTotal.setText(String.format("Total Price: %.2f", total));

        if (recyclerView != null && recyclerView.getAdapter() != null)
            recyclerView.getAdapter().notifyDataSetChanged();
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Utils.setStyleTheme(preferences, this);
        setContentView(R.layout.activity_shopping_cart);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        shoppingCartActivity = this;

        recyclerView = findViewById(R.id.recyclerViewShoppingCart);
        textTotal = findViewById(R.id.textViewTotal);
        MultiViewAdapter adapter = new MultiViewAdapter();
        adapter.registerItemBinders(new CartItemBinder());
        itemSection = new ListSection<>();

        List<Item> items = ShoppingCart.getInstance(this).getItems();
        Toast.makeText(this, "Items in cart: " + items.size(), Toast.LENGTH_SHORT).show();

        itemList.clear();
        itemList.addAll(items);
        itemSection.addAll(itemList);
        adapter.addSection(itemSection);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        cartListener.accept(ShoppingCart.getInstance(this).getItems());
        ShoppingCart.getInstance(this).addListener(cartListener);

        double total = 0;
        for (Item i : items) {
            total += i.getPrice();
        }
        textTotal.setText(String.format("Total Price: %.2f", total));
        invalidateOptionsMenu();
    }

    @Override
    protected void onStart() {
        super.onStart();
        cartListener.accept(ShoppingCart.getInstance(this).getItems());
        ShoppingCart.getInstance(this).addListener(cartListener);
    }


    public void onClickEmpty(View view) {
        ShoppingCart.getInstance(this).removeAll();
        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
        Toast.makeText(this, "Shopping Cart is cleared", Toast.LENGTH_SHORT).show();
    }

    public void onClickGoToOrder(View view){
        if(ShoppingCart.getInstance(this).getItems().size() > 0){
            Intent intent = new Intent(this, OrderActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Shopping Cart is empty", Toast.LENGTH_SHORT).show();
        }
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
