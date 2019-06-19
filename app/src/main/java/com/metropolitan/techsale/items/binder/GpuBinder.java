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
import com.metropolitan.techsale.items.model.Gpu;
import com.metropolitan.techsale.items.model.Item;
import com.metropolitan.techsale.shoppingcart.ShoppingCart;
import com.metropolitan.techsale.utils.Utils;

import java.util.function.Consumer;

import mva2.adapter.ItemBinder;
import mva2.adapter.ItemViewHolder;

public class GpuBinder extends ItemBinder<Gpu, GpuBinder.GpuViewHolder> {

    @Override
    public GpuViewHolder createViewHolder(ViewGroup parent) {
        return new GpuViewHolder(inflate(parent, R.layout.list_item_gpu));
    }

    @Override
    public void bindViewHolder(GpuViewHolder holder, Gpu item) {
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
        holder.textViewItemPrice.setText(String.format("Price: " + Utils.setCurrencyTag(preferences) + "%.2f", item.getPrice()));
        holder.textViewItemType.setText("Type: GPU");
        holder.textViewGpuMemory.setText(String.format("Memory: %dGB", item.getMemory()));
        holder.textViewGpuCores.setText(String.format("Cores: %d", item.getCores()));
        holder.textViewGpuMemorySpeed.setText(String.format("Speed: %dGbps", item.getMemorySpeed()));
        holder.textViewGpuBoostClock.setText(String.format("Boost: %dMHz", item.getBoostClock()));
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof Gpu;
    }

    static class GpuViewHolder extends ItemViewHolder<Gpu> {

        ImageView imageView;
        TextView textViewItemName;
        TextView textViewItemMake;
        TextView textViewItemPrice;
        TextView textViewItemType;
        TextView textViewGpuMemory;
        TextView textViewGpuCores;
        TextView textViewGpuMemorySpeed;
        TextView textViewGpuBoostClock;

        public GpuViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewItem);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            textViewItemMake = itemView.findViewById(R.id.textViewItemMake);
            textViewItemPrice = itemView.findViewById(R.id.textViewItemPrice);
            textViewItemType = itemView.findViewById(R.id.textViewItemType);
            textViewGpuMemory = itemView.findViewById(R.id.textViewGpuMemory);
            textViewGpuCores = itemView.findViewById(R.id.textViewGpuCores);
            textViewGpuMemorySpeed = itemView.findViewById(R.id.textViewGpuMemorySpeed);
            textViewGpuBoostClock = itemView.findViewById(R.id.textViewGpuBoostClock);
            Button button = itemView.findViewById(R.id.buttonAddToCart);
            button.setOnClickListener(v -> {
                Item item = getItem();
                ShoppingCart.getInstance(itemView.getContext()).add(item);
                Toast.makeText(itemView.getContext(), getItem().getName() + " added to cart", Toast.LENGTH_LONG).show();
            });
        }


    }

}
