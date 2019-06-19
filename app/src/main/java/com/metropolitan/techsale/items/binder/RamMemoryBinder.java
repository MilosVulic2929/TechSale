package com.metropolitan.techsale.items.binder;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.CircularProgressDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.metropolitan.techsale.R;
import com.metropolitan.techsale.items.ItemListActivity;
import com.metropolitan.techsale.items.model.Item;
import com.metropolitan.techsale.items.model.RamMemory;
import com.metropolitan.techsale.shoppingcart.ShoppingCart;
import com.metropolitan.techsale.utils.Utils;

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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ItemListActivity.itemListActivity);
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(holder.itemView.getContext());
        circularProgressDrawable.setStrokeWidth(5);
        circularProgressDrawable.setCenterRadius(32);
        circularProgressDrawable.start();
        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .placeholder(circularProgressDrawable)
                .error(R.drawable.ic_item_placeholder)
                .into(holder.imageView);
        holder.textViewItemName.setText(String.format("Name: %s", item.getName()));
        holder.textViewItemMake.setText(String.format("Make: %s", item.getMake()));
        holder.textViewItemPrice.setText(String.format("Price: " + Utils.setCurrencyTag(preferences) + "%s", item.getPrice()));
        holder.textViewItemType.setText("Type: RAM");
        holder.textViewRamMemory.setText(String.format("Memory: %dGB", item.getMemory()));
        holder.textViewRamFrequency.setText(String.format("Frequency: %dMHz", item.getFrequency()));
        holder.textViewRamType.setText(String.format("Type: %s", item.getType()));
    }

    static class RamViewHolder extends ItemViewHolder<RamMemory> {

        ImageView imageView;
        TextView textViewItemName;
        TextView textViewItemMake;
        TextView textViewItemPrice;
        TextView textViewItemType;
        TextView textViewRamMemory;
        TextView textViewRamFrequency;
        TextView textViewRamType;

        public RamViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewItem);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            textViewItemMake = itemView.findViewById(R.id.textViewItemMake);
            textViewItemPrice = itemView.findViewById(R.id.textViewItemPrice);
            textViewItemType = itemView.findViewById(R.id.textViewItemType);
            textViewRamMemory = itemView.findViewById(R.id.textViewRamMemory);
            textViewRamFrequency = itemView.findViewById(R.id.textViewRamFrequency);
            textViewRamType = itemView.findViewById(R.id.textViewRamType);
            Button button = itemView.findViewById(R.id.buttonAddToCart);
            button.setOnClickListener(v -> {
                Item item = getItem();
                ShoppingCart.getInstance(itemView.getContext()).add(item);
                Toast.makeText(itemView.getContext(), getItem().getName() + " added to cart", Toast.LENGTH_LONG).show();
            });
        }
    }
}