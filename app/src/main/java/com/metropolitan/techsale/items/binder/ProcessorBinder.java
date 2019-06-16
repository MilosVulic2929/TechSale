package com.metropolitan.techsale.items.binder;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.metropolitan.techsale.R;
import com.metropolitan.techsale.items.model.Processor;
import com.metropolitan.techsale.items.model.RamMemory;

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
        holder.textViewItemName.setText("Name: " + item.getName());
        holder.textViewItemMake.setText("Make: " + item.getMake());
        holder.textViewItemPrice.setText("Price: $" + item.getPrice());
        holder.textViewProcessorCores.setText(String.format("Cores: %d", item.getCores()));
        holder.textViewProcessorSpeed.setText(String.format("Speed: %s", item.getSpeed()));
        holder.textViewProcessorSocket.setText(String.format("Socket: %s", item.getSocket()));
    }

    static class ProcessorViewHolder extends ItemViewHolder<Processor> {

        TextView textViewItemName;
        TextView textViewItemMake;
        TextView textViewItemPrice;
        TextView textViewProcessorCores;
        TextView textViewProcessorSpeed;
        TextView textViewProcessorSocket;

        public ProcessorViewHolder(View itemView) {
            super(itemView);

            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            textViewItemMake = itemView.findViewById(R.id.textViewMake);
            textViewItemPrice = itemView.findViewById(R.id.textViewPrice);
            textViewProcessorCores = itemView.findViewById(R.id.textViewProcessorCores);
            textViewProcessorSpeed = itemView.findViewById(R.id.textViewProcessorSpeed);
            textViewProcessorSocket = itemView.findViewById(R.id.textViewProcessorSocket);
        }
    }
}
