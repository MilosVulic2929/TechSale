package com.metropolitan.techsale.orderlist;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.metropolitan.techsale.R;
import com.metropolitan.techsale.items.binder.GpuBinder;
import com.metropolitan.techsale.items.binder.ProcessorBinder;
import com.metropolitan.techsale.items.binder.RamMemoryBinder;
import com.metropolitan.techsale.items.binder.StorageBinder;
import com.metropolitan.techsale.items.model.Item;
import com.metropolitan.techsale.order.Order;
import com.metropolitan.techsale.shoppingcart.CartItemBinder;
import com.metropolitan.techsale.utils.Utils;

import java.util.Objects;

import mva2.adapter.ListSection;
import mva2.adapter.MultiViewAdapter;

/**
 * A fragment representing a single Order detail screen.
 * This fragment is either contained in a {@link OrderListActivity}
 * in two-pane mode (on tablets) or a {@link OrderDetailActivity}
 * on handsets.
 */
public class OrderDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_POSITION = "item_position";

    /**
     * The dummy content this fragment is presenting.
     */
    private Order mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OrderDetailFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        Utils.setStyleTheme(preferences, this.getActivity());*/
        if (Objects.requireNonNull(getArguments()).containsKey(ARG_ITEM_POSITION)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = OrderListActivity.ORDERS_CACHE.get(getArguments().getInt(ARG_ITEM_POSITION));
            Activity activity = this.getActivity();
            Objects.requireNonNull(activity).setTitle("Order " + mItem.getId());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.order_detail, container, false);
        if (mItem != null) {
            TextView textViewFullName = rootView.findViewById(R.id.textViewOrderDetailName);
            TextView textViewPhone = rootView.findViewById(R.id.textViewOrderDetailPhone);
            TextView textViewAddress = rootView.findViewById(R.id.textViewOrderDetailAddress);
            TextView textViewPrice = rootView.findViewById(R.id.textViewOrderDetailPrice);
            RecyclerView recyclerViewOrderItems = rootView.findViewById(R.id.recyclerViewOrderItems);

            MultiViewAdapter adapter = new MultiViewAdapter();
            adapter.registerItemBinders(new OrderItemBinder());
            ListSection<Item> listSection = new ListSection<>();
            listSection.addAll(mItem.getItems());
            adapter.addSection(listSection);
            recyclerViewOrderItems.setLayoutManager(new LinearLayoutManager(this.getActivity()));
            recyclerViewOrderItems.setAdapter(adapter);

            textViewFullName.setText(String.format("Full Name: %s %s", mItem.getFirstName(), mItem.getLastName()));
            textViewAddress.setText(String.format("Address: %s", mItem.getAddress()));
            textViewPhone.setText(String.format("Phone: %s", mItem.getPhone()));
            double total = 0;
            for(Item i : mItem.getItems())
                total+=i.getPrice();
            textViewPrice.setText(String.format("Price: %.2f", total));
        }

        return rootView;
    }
}
