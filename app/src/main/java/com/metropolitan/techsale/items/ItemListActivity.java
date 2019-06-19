package com.metropolitan.techsale.items;

import android.content.Intent;
import android.content.SharedPreferences;

import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.metropolitan.techsale.R;
import com.metropolitan.techsale.currency.CurrencyConverterService;
import com.metropolitan.techsale.currency.CurrencyConverterServiceImpl;
import com.metropolitan.techsale.items.binder.GpuBinder;
import com.metropolitan.techsale.items.binder.ProcessorBinder;
import com.metropolitan.techsale.items.binder.RamMemoryBinder;
import com.metropolitan.techsale.items.binder.StorageBinder;
import com.metropolitan.techsale.items.model.Gpu;
import com.metropolitan.techsale.items.model.Item;
import com.metropolitan.techsale.items.model.Processor;
import com.metropolitan.techsale.items.model.RamMemory;
import com.metropolitan.techsale.items.model.Storage;
import com.metropolitan.techsale.items.service.ItemServiceImpl;
import com.metropolitan.techsale.items.service.ItemsService;
import com.metropolitan.techsale.payment.PaymentActivity;
import com.metropolitan.techsale.settings.SettingsFragment;
import com.metropolitan.techsale.shoppingcart.ShoppingCart;
import com.metropolitan.techsale.shoppingcart.ShoppingCartActivity;
import com.metropolitan.techsale.utils.ExtraKeys;
import com.metropolitan.techsale.utils.Utils;

import org.json.JSONObject;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import mva2.adapter.ListSection;
import mva2.adapter.MultiViewAdapter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ItemListActivity extends AppCompatActivity {

    private boolean asGuest;
    private RecyclerView recyclerView;
    private Button buttonCart;
    private Button buttonPayment;
    private List<Item> itemList = new ArrayList<>();

    private ListSection<Item> listSection;

    private CurrencyConverterService currencyConverterService;
    private String base;
    private String to;
    private String json = "";
    private JSONObject conversionResult = null;
    private SharedPreferences preferences;



    private static final boolean USE_TEST_DATA = false; //TODO da li da koristi test podatke ili rest

    public static ItemListActivity itemListActivity;

    private Consumer<List<Item>> cartListener = (items -> {
        double total = 0;
        for (Item i : items) {
            total += i.getPrice();
        }
        if (buttonCart != null)
            buttonCart.setText("Cart(" + items.size() + ")");
        if (buttonPayment != null)
            buttonPayment.setText(String.format("Pay(%.2f)", total));

        Log.d("random_tag", "Called " + items.size());
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Utils.setStyleTheme(preferences, this);
        setContentView(R.layout.activity_item_list);
        asGuest = getIntent().getBooleanExtra(ExtraKeys.EXTRA_KEY_GUEST, false);
        buttonCart = findViewById(R.id.buttonCart);
        buttonPayment = findViewById(R.id.buttonPayment);
        recyclerView = findViewById(R.id.recyclerViewItems);

        currencyConverterService = new CurrencyConverterServiceImpl().getCurrencyConverterService();
        settings(preferences);
        if (json.length() < 1 && Utils.json.length() > 20) {
            json = Utils.json;
        }
        loadTestData();
        setupRecyclerView();

        // converting prices with regular currency to preferences chosen currency

        cartListener.accept(ShoppingCart.getInstance(this).getItems());
        ShoppingCart.getInstance(this).addListener(cartListener);
        Toast.makeText(this, "Items " + recyclerView.getAdapter().getItemCount(), Toast.LENGTH_LONG).show();
        convert(to, preferences);
        itemListActivity = this;

        ItemsService itemsService= new ItemServiceImpl().getItemService();
        itemsService.getItems().enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {

                Log.d("random_tag", "Code:"+response.code());
                List<Item> items = response.body();
                Log.d("random_tag", "Items " + Arrays.toString( items.toArray()));
                if(!USE_TEST_DATA){
                    itemList.clear();
                    itemList.addAll(items);
                    if (recyclerView != null && recyclerView.getAdapter() != null){
                        Log.d("random_tag", "into recycleview " + listSection.size());
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Log.d("random_tag", "Error:"  + t.toString());
                //TODO error message
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        cartListener.accept(ShoppingCart.getInstance(this).getItems());
        ShoppingCart.getInstance(this).addListener(cartListener);
    }


    public void onClickGoToPayment(View view) {
       if(ShoppingCart.getInstance(this).getItems().size() > 0){
             Intent intent = new Intent(this, PaymentActivity.class);
             String vrednost = buttonPayment.getText().toString().substring(buttonPayment.getText().toString().indexOf("(") + 1, buttonPayment.getText().toString().lastIndexOf(")"));
             intent.putExtra("Total", Double.valueOf(vrednost));
             startActivity(intent);
        } else {
            Toast.makeText(this, "Shopping Cart is empty", Toast.LENGTH_SHORT).show();
        }
    }
    public void onClickGoToCart(View view){
        if(ShoppingCart.getInstance(this).getItems().size() > 0){
            Intent intent = new Intent(this, ShoppingCartActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Shopping Cart is empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickEmptyCart(View view) {
        ShoppingCart.getInstance(this).removeAll();
        buttonCart.setText(R.string.cart);
        Toast.makeText(this, "Shopping Cart is cleared", Toast.LENGTH_SHORT).show();
    }

    private void setupRecyclerView() {
        MultiViewAdapter adapter = new MultiViewAdapter();
        adapter.registerItemBinders(
                new RamMemoryBinder(),
                new ProcessorBinder(),
                new GpuBinder(),
                new StorageBinder());
        listSection = new ListSection<>();
        listSection.addAll(itemList);
        adapter.addSection(listSection);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadTestData() {
        // TODO info - test podaci
        itemList.add(new RamMemory(1, "Furry", "Kingstone", 70, 14,
                8, 2400, "DDR4", ""));
        itemList.add(new RamMemory(2, "Furry", "Kingstone", 110, 14,
                16, 2400, "DDR4", ""));
        itemList.add(new Processor(3, "i5 6600k", "Intel", 240, 10,
                4, 4.0, "1151",
                "https://images-na.ssl-images-amazon.com/images/I/81SY-P8siHL._SX425_.jpg"));
        itemList.add(new Gpu(4, "GTX 1060", "Asus", 330, 34,
                6, 1280, 8, 1708, "https://c1.neweggimages.com/ProductImage/14-126-133-07.jpg"));
        itemList.add(new Storage(5, "WD Blue", "Western Digital", 45, 13,
                1024, Storage.DiskType.HDD, "7200RPM", ""));
    }

    private void settings(SharedPreferences preferences) {
        switch (SettingsFragment.oldValue) {
            case "dollar":
                base = "USD";
                break;
            case "euro":
                base = "EUR";
                break;
            case "pound":
                base = "GBP";
                break;
            case "ruble":
                base = "RUB";
                break;
        }
        switch (Objects.requireNonNull(preferences.getString("currencyKey", "euro"))) {
            case "dollar":
                to = "USD";
                break;
            case "euro":
                to = "EUR";
                break;
            case "pound":
                to = "GBP";
                break;
            case "ruble":
                to = "RUB";
                break;
        }
    }

    public boolean isConnected() {
        ConnectivityManager connec = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    private void convert(String to, SharedPreferences preferences) {
        if (!isConnected()) {
            Toast.makeText(this, "No Internet Connection or Host Unreachable", Toast.LENGTH_SHORT).show();
        } else {
            currencyConverterService.getCurrency("ccd1fde24c995a42d6396f72561cf555", base).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (json.length() < 1) {
                            json = Objects.requireNonNull(response.body()).string();
                            Utils.json = json;
                            Log.d("tagic", "readam json iz static" + Utils.json);
                        }
                        conversionResult = (JSONObject) new JSONObject(json).get("rates");

                        String s = conversionResult.toString().substring(conversionResult.toString().indexOf(to));
                        String s1 = conversionResult.toString().substring(conversionResult.toString().indexOf(base));

                        for (int i = 0; i < itemList.size(); i++) {
                            itemList.get(i).setPrice(itemList.get(i).getPrice() * Double.valueOf(s.substring(s.indexOf(to) + 5, s.indexOf(","))));
                        }
                        setupRecyclerView();

                        if (!base.equals(to)) {
                            for (int i = 0; i < ShoppingCart.getInstance(ItemListActivity.this).getItems().size(); i++) {
                                double broj = ShoppingCart.getInstance(ItemListActivity.this).getItems().get(i).getPrice() / Double.valueOf(s1.substring(s1.indexOf(base) + 5, s1.indexOf(",")));
                                ShoppingCart.getInstance(ItemListActivity.this).getItems().get(i)
                                        .setPrice(broj * Double.valueOf(s.substring(s.indexOf(to) + 5, s.indexOf(","))));
                            }
                            switch (to) {
                                case "USD":
                                    SettingsFragment.oldValue = "dollar";
                                    break;
                                case "EUR":
                                    SettingsFragment.oldValue = "euro";
                                    break;
                                case "GBP":
                                    SettingsFragment.oldValue = "pound";
                                    break;
                                case "RUB":
                                    SettingsFragment.oldValue = "ruble";
                                    break;
                            }
                        }

                        cartListener.accept(ShoppingCart.getInstance(ItemListActivity.this).getItems());
                        ShoppingCart.getInstance(ItemListActivity.this).addListener(cartListener);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Connection error handling
                   // Toast.makeText(ItemListActivity.this, "Error occurred while updating currency", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
