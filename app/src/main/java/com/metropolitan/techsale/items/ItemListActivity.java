package com.metropolitan.techsale.items;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.metropolitan.techsale.MainActivity;
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
import com.metropolitan.techsale.order.OrderActivity;
import com.metropolitan.techsale.orderlist.OrderListActivity;
import com.metropolitan.techsale.settings.SettingsActivity;
import com.metropolitan.techsale.shoppingcart.ShoppingCart;
import com.metropolitan.techsale.shoppingcart.ShoppingCartActivity;
import com.metropolitan.techsale.utils.PreferenceKeys;
import com.metropolitan.techsale.utils.Utils;

import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import mva2.adapter.ListSection;
import mva2.adapter.MultiViewAdapter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.metropolitan.techsale.utils.Utils.isConnected;


@SuppressWarnings("all")
public class ItemListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final List<Item> ITEMS_CACHE = new ArrayList<>();

    private RecyclerView recyclerView;
    private Button buttonCart;
    private Button buttonPayment;
    private List<Item> filteredItemsList = new ArrayList<>();
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
            buttonCart.setText(String.format("Cart(%d)", items.size()));
        if (buttonPayment != null)
            buttonPayment.setText(String.format("Order(%.2f)", total));

        Log.d("random_tag", "Called " + items.size());
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Utils.setStyleTheme(preferences, this);
        setContentView(R.layout.activity_item_list);
        buttonCart = findViewById(R.id.buttonCart);
        buttonPayment = findViewById(R.id.buttonPayment);
        recyclerView = findViewById(R.id.recyclerViewItems);
        Spinner spinner = findViewById(R.id.spinnerItemFilter);
        spinner.setOnItemSelectedListener(this);

        currencyConverterService = new CurrencyConverterServiceImpl().getCurrencyConverterService();
        settings(preferences);
        if (json.length() < 1 && Utils.json.length() > 20) {
            json = Utils.json;
        }
        //loadTestData();
        setupRecyclerView();

        // converting prices with regular currency to preferences chosen currency

        cartListener.accept(ShoppingCart.getInstance(this).getItems());
        ShoppingCart.getInstance(this).addListener(cartListener);
        Toast.makeText(this, "Items " + recyclerView.getAdapter().getItemCount(), Toast.LENGTH_LONG).show();
        convert(to, preferences);
        itemListActivity = this;
        invalidateOptionsMenu();

        if (ITEMS_CACHE.isEmpty()) {
            if (USE_TEST_DATA) {
                loadTestData();
            } else {
                ItemsService itemsService = new ItemServiceImpl().getItemService();
                itemsService.getItems().enqueue(new Callback<List<Item>>() {
                    @Override
                    public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                        Log.d("random_tag", "Code:" + response.code());
                        List<Item> items = response.body();
                        Log.d("random_tag", "Items " + Arrays.toString(items.toArray()));
                        ITEMS_CACHE.clear();
                        ITEMS_CACHE.addAll(items);
                        /*
                        filteredItemsList.clear();
                        filteredItemsList.addAll(items);*/
                        if (recyclerView != null && recyclerView.getAdapter() != null) {
                            listSection.clear();
                            listSection.addAll(ITEMS_CACHE);
                            Log.d("random_tag", "into recycleview " + listSection.size());
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Item>> call, Throwable t) {
                        Log.d("random_tag", "Error:" + t.toString());
                        Toast.makeText(ItemListActivity.this, "Failed to load items, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {

            if (recyclerView != null && recyclerView.getAdapter() != null) {
                listSection.clear();
                listSection.addAll(ITEMS_CACHE);
                Log.d("random_tag", "into recycleview " + listSection.size());
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        cartListener.accept(ShoppingCart.getInstance(this).getItems());
        ShoppingCart.getInstance(this).addListener(cartListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ShoppingCart.getInstance(this).save();
    }

    public void onClickGoToOrder(View view) {

        //TODO go to OrderActivity
        if (ShoppingCart.getInstance(this).getItems().size() > 0) {
            startActivity(new Intent(this, OrderActivity.class));
        } else {
            Toast.makeText(this, "Shopping Cart is empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickGoToCart(View view) {
        if (ShoppingCart.getInstance(this).getItems().size() > 0) {
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
        listSection.addAll(ITEMS_CACHE);
        adapter.addSection(listSection);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadTestData() {
        // TODO info - test podaci
        ITEMS_CACHE.add(new RamMemory(1, "Furry", "Kingstone", 70, 14,
                8, 2400, "DDR4", ""));
        ITEMS_CACHE.add(new RamMemory(2, "Furry", "Kingstone", 110, 14,
                16, 2400, "DDR4", ""));
        ITEMS_CACHE.add(new Processor(3, "i5 6600k", "Intel", 240, 10,
                4, 4.0, "1151",
                "https://images-na.ssl-images-amazon.com/images/I/81SY-P8siHL._SX425_.jpg"));
        ITEMS_CACHE.add(new Gpu(4, "GTX 1060", "Asus", 330, 34,
                6, 1280, 8, 1708, "https://c1.neweggimages.com/ProductImage/14-126-133-07.jpg"));
        ITEMS_CACHE.add(new Storage(5, "WD Blue", "Western Digital", 45, 13,
                1024, Storage.DiskType.HDD, "7200RPM", ""));
    }

    private void settings(SharedPreferences preferences) {
        switch (MainActivity.oldValue) {
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
        Log.d("tagic", "Base " + base + ", To " + to + ", oldValue" + MainActivity.oldValue);
    }


    private void convert(String to, SharedPreferences preferences) {
        if (!isConnected(this)) {
            Toast.makeText(this, "No Internet Connection or Host Unreachable", Toast.LENGTH_SHORT).show();
        } else {

            currencyConverterService.getCurrency("d9a2389ab121802b3085c71038dec034", base).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d("random_tag", "Code " + response.code());
                    try {
                        if (json.length() < 1) {
                            json = Objects.requireNonNull(response.body()).string();
                            Utils.json = json;
                            Log.d("tagic", "readam json iz static" + Utils.json);
                        }
                        String a = "{\"success\":true,\"timestamp\":1560975849,\"base\":\"EUR\",\"date\":\"2019-06-19\",\"rates\":{\"AED\":4.124488,\"AFN\":90.987188,\"ALL\":121.494996,\"AMD\":536.815763,\"ANG\":2.105817,\"AOA\":382.109356,\"ARS\":48.668616,\"AUD\":1.631482,\"AWG\":2.024122,\"AZN\":1.914661,\"BAM\":1.959945,\"BBD\":2.236697,\"BDT\":94.893961,\"BGN\":1.955619,\"BHD\":0.423219,\"BIF\":2071.847388,\"BMD\":1.122953,\"BND\":1.516491,\"BOB\":7.760444,\"BRL\":4.31461,\"BSD\":1.123009,\"BTC\":0.000123,\"BTN\":78.162047,\"BWP\":12.145892,\"BYN\":2.317717,\"BYR\":22009.869274,\"BZD\":2.263758,\"CAD\":1.492011,\"CDF\":1865.224268,\"CHF\":1.115911,\"CLF\":0.028125,\"CLP\":776.078105,\"CNY\":7.752417,\"COP\":3649.764113,\"CRC\":656.753173,\"CUC\":1.122953,\"CUP\":29.758242,\"CVE\":110.811268,\"CZK\":25.626002,\"DJF\":199.570569,\"DKK\":7.466961,\"DOP\":57.27622,\"DZD\":133.956866,\"EGP\":18.797662,\"ERN\":16.844551,\"ETB\":32.621367,\"EUR\":1,\"FJD\":2.428383,\"FKP\":0.892072,\"GBP\":0.887952,\"GEL\":3.127459,\"GGP\":0.887797,\"GHS\":6.06462,\"GIP\":0.892077,\"GMD\":55.647945,\"GNF\":10373.276083,\"GTQ\":8.642075,\"GYD\":234.545446,\"HKD\":8.786025,\"HNL\":27.854845,\"HRK\":7.402836,\"HTG\":104.089276,\"HUF\":323.578347,\"IDR\":15985.790513,\"ILS\":4.0378,\"IMP\":0.887797,\"INR\":78.180401,\"IQD\":1336.313492,\"IRR\":47281.915521,\"ISK\":141.536384,\"JEP\":0.887797,\"JMD\":144.995541,\"JOD\":0.796166,\"JPY\":121.370391,\"KES\":114.418025,\"KGS\":78.303586,\"KHR\":4578.837784,\"KMF\":493.032498,\"KPW\":1010.730393,\"KRW\":1316.02208,\"KWD\":0.34131,\"KYD\":0.935873,\"KZT\":431.124275,\"LAK\":9724.192859,\"LBP\":1696.500381,\"LKR\":198.414333,\"LRD\":218.975921,\"LSL\":16.221027,\"LTL\":3.315787,\"LVL\":0.679263,\"LYD\":1.570896,\"MAD\":10.744973,\"MDL\":20.440003,\"MGA\":4042.62876,\"MKD\":61.644508,\"MMK\":1716.055764,\"MNT\":2987.30648,\"MOP\":9.051727,\"MRO\":400.894096,\"MUR\":40.541952,\"MVR\":17.416693,\"MWK\":847.82926,\"MXN\":21.375407,\"MYR\":4.686756,\"MZN\":69.583761,\"NAD\":16.114694,\"NGN\":404.262551,\"NIO\":37.500726,\"NOK\":9.770923,\"NPR\":125.304692,\"NZD\":1.716871,\"OMR\":0.432342,\"PAB\":1.123009,\"PEN\":3.739992,\"PGK\":3.792775,\"PHP\":58.157796,\"PKR\":175.978279,\"PLN\":4.263289,\"PYG\":6947.485169,\"QAR\":4.08866,\"RON\":4.731337,\"RSD\":117.887863,\"RUB\":71.649985,\"RWF\":1019.640883,\"SAR\":4.211353,\"SBD\":9.23926,\"SCR\":15.337851,\"SDG\":50.664811,\"SEK\":10.651314,\"SGD\":1.530719,\"SHP\":1.483308,\"SLL\":10072.883567,\"SOS\":658.049753,\"SRD\":8.374992,\"STD\":23638.823753,\"SVC\":9.826677,\"SYP\":578.32078,\"SZL\":16.221073,\"THB\":35.103908,\"TJS\":10.590285,\"TMT\":3.930334,\"TND\":3.331822,\"TOP\":2.579196,\"TRY\":6.510651,\"TTD\":7.60615,\"TWD\":34.967055,\"TZS\":2580.544852,\"UAH\":29.638063,\"UGX\":4172.27375,\"USD\":1.122953,\"UYU\":39.673754,\"UZS\":9587.213673,\"VEF\":11.215487,\"VND\":26204.096914,\"VUV\":130.216763,\"WST\":2.983494,\"XAF\":657.34295,\"XAG\":0.074078,\"XAU\":0.000825,\"XCD\":3.034836,\"XDR\":0.811887,\"XOF\":658.61124,\"XPF\":119.454041,\"YER\":281.130923,\"ZAR\":16.086464,\"ZMK\":10107.914091,\"ZMW\":14.376022,\"ZWL\":361.98937}}";
                        conversionResult = (JSONObject) new JSONObject(a).get("rates");
                        String s = conversionResult.toString().substring(conversionResult.toString().indexOf(to));
                        String s1 = conversionResult.toString().substring(conversionResult.toString().indexOf(base));

                        for (int i = 0; i < ITEMS_CACHE.size(); i++) {
                            ITEMS_CACHE.get(i).setPrice(ITEMS_CACHE.get(i).getPrice() * Double.valueOf(s.substring(s.indexOf(to) + 5, s.indexOf(","))));
                        }
                        if (!base.equals(to)) {

                            for (int i = 0; i < ShoppingCart.getInstance(ItemListActivity.this).getItems().size(); i++) {
                                double broj = ShoppingCart.getInstance(ItemListActivity.this).getItems().get(i).getPrice() / Double.valueOf(s1.substring(s1.indexOf(base) + 5, s1.indexOf(",")));
                                ShoppingCart.getInstance(ItemListActivity.this).getItems().get(i)
                                        .setPrice(broj * Double.valueOf(s.substring(s.indexOf(to) + 5, s.indexOf(","))));
                            }
                            switch (to) {
                                case "USD":
                                    MainActivity.oldValue = "dollar";
                                    break;
                                case "EUR":
                                    MainActivity.oldValue = "euro";
                                    break;
                                case "GBP":
                                    MainActivity.oldValue = "pound";
                                    break;
                                case "RUB":
                                    MainActivity.oldValue = "ruble";
                                    break;
                            }
                        }
                        setupRecyclerView();
                        cartListener.accept(ShoppingCart.getInstance(ItemListActivity.this).getItems());

                        ShoppingCart.getInstance(ItemListActivity.this).addListener(cartListener);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Connection error handling
                    Log.d("random_tag", "Error " + t.getMessage());
                    Toast.makeText(ItemListActivity.this,
                            "Error occurred while updating currency", Toast.LENGTH_SHORT).show();
                }
            });
        }
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selected = (String) parent.getItemAtPosition(position);
        Log.d("random_tag", "Selected: " + selected);
        if (selected.equals("ALL")) {
            listSection.clear();
            listSection.addAll(ITEMS_CACHE);
            TextView textView = findViewById(R.id.textViewFilterMessage);
            textView.setVisibility(View.INVISIBLE);
        } else {
            List<Item> filtered = ITEMS_CACHE.stream().filter(i ->
                    i.getType().equals(selected.toLowerCase()))
                    .collect(Collectors.toList());
            Log.d("random_tag", "Filtered: " + filtered.size());
            listSection.clear();
            listSection.addAll(filtered);
            if(filtered.size() < 1){
                TextView textView = findViewById(R.id.textViewFilterMessage);
                textView.setVisibility(View.VISIBLE);
            } else {
                TextView textView = findViewById(R.id.textViewFilterMessage);
                textView.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        parent.setSelection(0);
    }
}
