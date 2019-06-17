package com.metropolitan.techsale.shoppingcart;

import android.support.v4.widget.CircularProgressDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.metropolitan.techsale.R;
import com.metropolitan.techsale.items.model.Gpu;
import com.metropolitan.techsale.items.model.Item;
import com.metropolitan.techsale.items.model.Processor;
import com.metropolitan.techsale.items.model.RamMemory;
import com.metropolitan.techsale.items.model.Storage;

import mva2.adapter.ItemBinder;
import mva2.adapter.ItemViewHolder;

public class CartItemBinder extends ItemBinder<Item, CartItemBinder.CartItemViewHolder> {

    @Override
    public CartItemViewHolder createViewHolder(ViewGroup parent) {
        return new CartItemViewHolder(inflate(parent, R.layout.list_cart_item));
    }

    @Override
    public void bindViewHolder(CartItemViewHolder holder, Item item) {
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
        holder.textViewItemPrice.setText(String.format("Price: $%s", item.getPrice()));
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

    static class CartItemViewHolder extends ItemViewHolder<Item> {

        ImageView imageView;
        TextView textViewItemName;
        TextView textViewItemMake;
        TextView textViewItemPrice;
        TextView textViewItemType;


        public CartItemViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewItem);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            textViewItemMake = itemView.findViewById(R.id.textViewItemMake);
            textViewItemPrice = itemView.findViewById(R.id.textViewItemPrice);
            textViewItemType = itemView.findViewById(R.id.textViewItemType);
            Button button = itemView.findViewById(R.id.buttonRemove);
            button.setOnClickListener(v -> {
                Item item = getItem();
                ShoppingCart.getInstance(itemView.getContext()).remove(item);
                Toast.makeText(itemView.getContext(), getItem().getName() + " removed from cart", Toast.LENGTH_LONG).show();
            });
        }


    }

}
