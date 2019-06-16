package com.metropolitan.techsale.items;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.metropolitan.techsale.R;
import com.metropolitan.techsale.items.binder.ProcessorBinder;
import com.metropolitan.techsale.items.binder.RamMemoryBinder;
import com.metropolitan.techsale.items.model.Item;
import com.metropolitan.techsale.items.model.Processor;
import com.metropolitan.techsale.items.model.RamMemory;
import com.metropolitan.techsale.utils.ExtraKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import mva2.adapter.ListSection;
import mva2.adapter.MultiViewAdapter;


public class ItemListActivity extends AppCompatActivity {

    private boolean asGuest;

    private RecyclerView recyclerView;

    private List<Item> itemList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        asGuest = getIntent().getBooleanExtra(ExtraKeys.EXTRA_KEY_GUEST, false);

        // TODO info - test podaci
        itemList.add(new RamMemory(1, "Furry", "Kingstone", 70, 14, 8, 2400, "DDR4"));
        itemList.add(new RamMemory(2, "Furry", "Kingstone", 110, 14, 16, 2400, "DDR4"));
        itemList.add(new Processor(3, "i5 6600k", "Intel", 240, 10, 4, 4.0, "1151"));

        recyclerView = findViewById(R.id.recyclerViewItems);
        MultiViewAdapter adapter = new MultiViewAdapter();


        adapter.registerItemBinders(new RamMemoryBinder(), new ProcessorBinder());

        ListSection<Item> listSection = new ListSection<>();
        listSection.addAll(itemList);
        adapter.addSection(listSection);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Toast.makeText(this, "Items " + Objects.requireNonNull(recyclerView.getAdapter()).getItemCount(), Toast.LENGTH_LONG).show();
    }
}
