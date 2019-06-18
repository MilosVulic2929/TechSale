package com.metropolitan.techsale.items;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.metropolitan.techsale.items.binder.GpuBinder;
import com.metropolitan.techsale.items.binder.ProcessorBinder;
import com.metropolitan.techsale.items.binder.RamMemoryBinder;
import com.metropolitan.techsale.items.binder.StorageBinder;
import com.metropolitan.techsale.items.model.Gpu;
import com.metropolitan.techsale.items.model.Item;
import com.metropolitan.techsale.items.model.Processor;
import com.metropolitan.techsale.items.model.RamMemory;
import com.metropolitan.techsale.items.model.Storage;
import com.metropolitan.techsale.payment.PaymentActivity;
import com.metropolitan.techsale.shoppingcart.ShoppingCart;
import com.metropolitan.techsale.shoppingcart.ShoppingCartActivity;
import com.metropolitan.techsale.utils.ExtraKeys;
import com.metropolitan.techsale.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import mva2.adapter.ListSection;
import mva2.adapter.MultiViewAdapter;


public class ItemListActivity extends AppCompatActivity {

    private boolean asGuest;
    private RecyclerView recyclerView;
    private Button buttonCart;
    private Button buttonPayment;
    private List<Item> itemList = new ArrayList<>();

    private Consumer<List<Item>> cartListener = (items -> {
        double total = 0;
        for(Item i : items){
            total += i.getPrice();
        }
        if(buttonCart != null)
        buttonCart.setText("Cart(" + items.size()+ ")");
        if(buttonPayment != null)
        buttonPayment.setText("Pay(" + total+ ")");

        Log.d("random_tag", "Called " + items.size());
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Utils.setStyleTheme(preferences, this);
        setContentView(R.layout.activity_item_list);
        asGuest = getIntent().getBooleanExtra(ExtraKeys.EXTRA_KEY_GUEST, false);
        buttonCart = findViewById(R.id.buttonCart);
        buttonPayment = findViewById(R.id.buttonPayment);
        recyclerView = findViewById(R.id.recyclerViewItems);
        loadTestData();
        setupRecyclerView();
        cartListener.accept(ShoppingCart.getInstance(this).getItems());
        ShoppingCart.getInstance(this).addListener(cartListener);

        Toast.makeText(this, "Items " + recyclerView.getAdapter().getItemCount(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        cartListener.accept(ShoppingCart.getInstance(this).getItems());
        ShoppingCart.getInstance(this).addListener(cartListener);
    }

    public void onClickGoToCart(View view){
        if(ShoppingCart.getInstance(this).getItems().size() > 0){
            Intent intent = new Intent(this, ShoppingCartActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Shopping Cart is empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickGoToPayment(View view){
        if(ShoppingCart.getInstance(this).getItems().size() > 0){
            Intent intent = new Intent(this, PaymentActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Shopping Cart is empty", Toast.LENGTH_SHORT).show();
        }
    }


    public void onClickEmptyCart(View view){
        ShoppingCart.getInstance(this).removeAll();
        buttonCart.setText(R.string.cart);
        Toast.makeText(this, "Shopping Cart is cleared", Toast.LENGTH_SHORT).show();
    }

    private void setupRecyclerView(){
        MultiViewAdapter adapter = new MultiViewAdapter();
        adapter.registerItemBinders(
                new RamMemoryBinder(),
                new ProcessorBinder(),
                new GpuBinder(),
                new StorageBinder());
        ListSection<Item> listSection = new ListSection<>();
        listSection.addAll(itemList);
        adapter.addSection(listSection);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadTestData(){
        // TODO info - test podaci
        itemList.add(new RamMemory(1, "Furry", "Kingstone", 70, 14,
                8, 2400, "DDR4", ""));
        itemList.add(new RamMemory(2, "Furry", "Kingstone", 110, 14,
                16, 2400, "DDR4", ""));
        itemList.add(new Processor(3, "i5 6600k", "Intel", 240, 10,
                4, 4.0, "1151",
                "https://images-na.ssl-images-amazon.com/images/I/81SY-P8siHL._SX425_.jpg"));
        itemList.add(new Gpu(4, "GTX 1060", "Asus", 330, 34,
                6, 	1280, 8, 1708, "https://c1.neweggimages.com/ProductImage/14-126-133-07.jpg"));
        itemList.add(new Storage(5, "WD Blue", "Western Digital", 45, 13,
                1024, Storage.DiskType.HDD, "7200RPM", ""));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
