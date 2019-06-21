package com.metropolitan.techsale.orderlist;

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
import com.metropolitan.techsale.items.model.Processor;
import com.metropolitan.techsale.items.model.RamMemory;
import com.metropolitan.techsale.items.model.Storage;
import com.metropolitan.techsale.shoppingcart.CartItemBinder;
import com.metropolitan.techsale.shoppingcart.ShoppingCart;
import com.metropolitan.techsale.utils.Utils;

import mva2.adapter.ItemBinder;
import mva2.adapter.ItemViewHolder;

public class OrderItemBinder extends ItemBinder<Item, OrderItemBinder.OrderItemViewHolder> {
    @Override
    public OrderItemViewHolder createViewHolder(ViewGroup parent) {
        return new OrderItemViewHolder(inflate(parent, R.layout.list_order_item));
    }

    @Override
    public void bindViewHolder(OrderItemViewHolder holder, Item item) {
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
        holder.textViewItemType.setText(String.format("Type: %s", findType(item)));
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof Item;
    }

    private String findType(Item item) {
        if (item instanceof Gpu)
            return "GPU";
        else if (item instanceof Processor)
            return "CPU";
        else if (item instanceof RamMemory)
            return "RAM";
        else if (item instanceof Storage)
            return "Storage";
        return "";
    }

    static class OrderItemViewHolder extends ItemViewHolder<Item> {

        ImageView imageView;
        TextView textViewItemName;
        TextView textViewItemMake;
        TextView textViewItemPrice;
        TextView textViewItemType;


        public OrderItemViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewItem);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            textViewItemMake = itemView.findViewById(R.id.textViewItemMake);
            textViewItemPrice = itemView.findViewById(R.id.textViewItemPrice);
            textViewItemType = itemView.findViewById(R.id.textViewItemType);
        }
    }
}
