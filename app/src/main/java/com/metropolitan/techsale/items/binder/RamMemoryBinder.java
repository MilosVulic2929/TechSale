package com.metropolitan.techsale.items.binder;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.metropolitan.techsale.R;
import com.metropolitan.techsale.items.model.RamMemory;

import mva2.adapter.ItemBinder;
import mva2.adapter.ItemViewHolder;

/**
 * TODO info Binder za procesore u RecycleView
 */
public class RamMemoryBinder extends ItemBinder<RamMemory, RamMemoryBinder.RamViewHolder> {

    @Override
    public RamViewHolder createViewHolder(ViewGroup parent) {
        return new RamViewHolder(inflate(parent, R.layout.list_item_ram));
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof RamMemory;
    }

    @SuppressWarnings("all")
    @Override
    public void bindViewHolder(RamViewHolder holder, RamMemory item) {
        holder.textViewItemName.setText("Name: " + item.getName());
        holder.textViewItemMake.setText("Make: " + item.getMake());
        holder.textViewItemPrice.setText("Price: $" + item.getPrice());
        holder.textViewRamMemory.setText(String.format("Memory: %dGB", item.getMemory()));
        holder.textViewRamFrequency.setText(String.format("Frequency: %dMHz", item.getFrequency()));
        holder.textViewRamType.setText(String.format("Type: %s", item.getType()));
    }

    static class RamViewHolder extends ItemViewHolder<RamMemory> {

        TextView textViewItemName;
        TextView textViewItemMake;
        TextView textViewItemPrice;
        TextView textViewRamMemory;
        TextView textViewRamFrequency;
        TextView textViewRamType;

        public RamViewHolder(View itemView) {
            super(itemView);

            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            textViewItemMake = itemView.findViewById(R.id.textViewMake);
            textViewItemPrice = itemView.findViewById(R.id.textViewPrice);
            textViewRamMemory = itemView.findViewById(R.id.textViewRamMemory);
            textViewRamFrequency = itemView.findViewById(R.id.textViewRamFrequency);
            textViewRamType = itemView.findViewById(R.id.textViewRamType);
        }
    }
}