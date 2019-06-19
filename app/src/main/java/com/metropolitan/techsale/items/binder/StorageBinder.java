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
import com.metropolitan.techsale.items.model.Storage;
import com.metropolitan.techsale.shoppingcart.ShoppingCart;
import com.metropolitan.techsale.utils.Utils;

import mva2.adapter.ItemBinder;
import mva2.adapter.ItemViewHolder;

public class StorageBinder  extends ItemBinder<Storage, StorageBinder.StorageViewHolder> {

    @Override
    public StorageViewHolder createViewHolder(ViewGroup parent) {
        return new StorageViewHolder(inflate(parent, R.layout.list_item_storage));
    }

    @SuppressWarnings("all")
    @Override
    public void bindViewHolder(StorageViewHolder holder, Storage item) {
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
        holder.textViewItemType.setText("Type: Storage");
        holder.textViewStorageCapacity.setText(String.format("Capacity: %dGB", item.getCapacity()));
        holder.textViewStorageType.setText(String.format("Disk Type: %s", item.getDiskType().toString()));
        holder.textViewStorageSpeed.setText(String.format("Speed: %s", item.getSpeed()));
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof Storage;
    }

    static class StorageViewHolder extends ItemViewHolder<Storage> {

        ImageView imageView;
        TextView textViewItemName;
        TextView textViewItemMake;
        TextView textViewItemPrice;
        TextView textViewItemType;
        TextView textViewStorageCapacity;
        TextView textViewStorageType;
        TextView textViewStorageSpeed;

        public StorageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewItem);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            textViewItemMake = itemView.findViewById(R.id.textViewItemMake);
            textViewItemPrice = itemView.findViewById(R.id.textViewItemPrice);
            textViewItemType = itemView.findViewById(R.id.textViewItemType);
            textViewStorageCapacity = itemView.findViewById(R.id.textViewStorageCapacity);
            textViewStorageType = itemView.findViewById(R.id.textViewStorageType);
            textViewStorageSpeed = itemView.findViewById(R.id.textViewStorageSpeed);
            Button button = itemView.findViewById(R.id.buttonAddToCart);
            button.setOnClickListener(v -> {
                Item item = getItem();
                ShoppingCart.getInstance(itemView.getContext()).add(item);
                Toast.makeText(itemView.getContext(), getItem().getName() + " added to cart", Toast.LENGTH_LONG).show();
            });
        }
    }

}
