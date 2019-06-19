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
import com.metropolitan.techsale.items.model.Processor;
import com.metropolitan.techsale.shoppingcart.ShoppingCart;
import com.metropolitan.techsale.utils.Utils;

import mva2.adapter.ItemBinder;
import mva2.adapter.ItemViewHolder;

/**
 * TODO info Binder za procesore u RecycleView
 */
public class ProcessorBinder  extends ItemBinder<Processor, ProcessorBinder.ProcessorViewHolder> {

    @Override
    public ProcessorBinder.ProcessorViewHolder createViewHolder(ViewGroup parent) {
        return new ProcessorBinder.ProcessorViewHolder(inflate(parent, R.layout.list_item_processor));
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof Processor;
    }

    @SuppressWarnings("all")
    @Override
    public void bindViewHolder(ProcessorBinder.ProcessorViewHolder holder, Processor item) {
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
        holder.textViewItemType.setText("Type: CPU");
        holder.textViewProcessorCores.setText(String.format("Cores: %d", item.getCores()));
        holder.textViewProcessorSpeed.setText(String.format("Speed: %s", item.getSpeed()));
        holder.textViewProcessorSocket.setText(String.format("Socket: %s", item.getSocket()));
    }

    static class ProcessorViewHolder extends ItemViewHolder<Processor> {

        ImageView imageView;
        TextView textViewItemName;
        TextView textViewItemMake;
        TextView textViewItemPrice;
        TextView textViewItemType;
        TextView textViewProcessorCores;
        TextView textViewProcessorSpeed;
        TextView textViewProcessorSocket;

        ProcessorViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewItem);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            textViewItemMake = itemView.findViewById(R.id.textViewItemMake);
            textViewItemPrice = itemView.findViewById(R.id.textViewItemPrice);
            textViewItemType = itemView.findViewById(R.id.textViewItemType);
            textViewProcessorCores = itemView.findViewById(R.id.textViewProcessorCores);
            textViewProcessorSpeed = itemView.findViewById(R.id.textViewProcessorSpeed);
            textViewProcessorSocket = itemView.findViewById(R.id.textViewProcessorSocket);
            Button button = itemView.findViewById(R.id.buttonAddToCart);
            button.setOnClickListener(v -> {
                Item item = getItem();
                ShoppingCart.getInstance(itemView.getContext()).add(item);
                Toast.makeText(itemView.getContext(), getItem().getName() + " added to cart", Toast.LENGTH_LONG).show();
            });
        }
    }
}
