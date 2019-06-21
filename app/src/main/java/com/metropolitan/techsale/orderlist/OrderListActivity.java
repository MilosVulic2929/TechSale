package com.metropolitan.techsale.orderlist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.metropolitan.techsale.MainActivity;
import com.metropolitan.techsale.R;

import com.metropolitan.techsale.items.model.Item;
import com.metropolitan.techsale.order.Order;
import com.metropolitan.techsale.order.OrderServiceImpl;
import com.metropolitan.techsale.settings.SettingsActivity;
import com.metropolitan.techsale.shoppingcart.ShoppingCart;
import com.metropolitan.techsale.utils.PreferenceKeys;
import com.metropolitan.techsale.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An activity representing a list of Orders. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link OrderDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class OrderListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    public static final List<Order> ORDERS_CACHE = new ArrayList<>();

    private RecyclerView recyclerView;
    public static OrderListActivity orderListActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Utils.setStyleTheme(preferences, this);
        setContentView(R.layout.activity_order_list);
        orderListActivity = this;
        /*
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        Objects.requireNonNull(getSupportActionBar()).setTitle(getTitle());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
       // toolbar.setTitle(getTitle());

        if (findViewById(R.id.order_detail_container) != null) {
            mTwoPane = true;
        }
        String token = this.getSharedPreferences(PreferenceKeys.PREFERENCES_NAME, Context.MODE_PRIVATE)
                .getString(PreferenceKeys.AUTH_TOKEN, "");
        String username = this.getSharedPreferences(PreferenceKeys.PREFERENCES_NAME, Context.MODE_PRIVATE)
                .getString(PreferenceKeys.AUTH_USERNAME, "");
        Log.d("random_tag", "token: " + token + "username: " + username);

        recyclerView = findViewById(R.id.order_list);

        if (token != null && token.isEmpty() && username != null && username.isEmpty()) {

        } else {

            if (ORDERS_CACHE.isEmpty()) {
                new OrderServiceImpl().getOrderService().getAllOrders("Bearer " + token, username).enqueue(new Callback<List<Order>>() {
                    @Override
                    public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                        Log.d("random_tag", "Response code: " + response.code());
                        if (response.isSuccessful()) {
                            List<Order> orders = response.body();
                            ORDERS_CACHE.clear();
                            if (orders != null){
                                ORDERS_CACHE.addAll(orders);
                            }
                            recyclerView.getAdapter().notifyDataSetChanged();
                        } else {
                            Log.d("random_tag", "Response failed: " + response.message() + ", " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Order>> call, Throwable t) {
                        Log.d("random_tag", "Error: " + t.getMessage());
                    }
                });
            }

        }
        assert recyclerView != null;
        setupRecyclerView(recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, ORDERS_CACHE, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final OrderListActivity mParentActivity;
        private final List<Order> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (int) view.getTag();
                Log.d("random_tag", "Position " + position);
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putInt(OrderDetailFragment.ARG_ITEM_POSITION, position);
                    OrderDetailFragment fragment = new OrderDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.order_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    intent.putExtra(OrderDetailFragment.ARG_ITEM_POSITION, position);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(OrderListActivity parent,
                                      List<Order> orders,
                                      boolean twoPane) {
            mValues = orders;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.order_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Order order = mValues.get(position);
            holder.textViewOrderId.setText(String.format("ID: %d", order.getId()));
            holder.textViewOrderFullName.setText(String.format("Name: %s %s", order.getFirstName(), order.getLastName()));
            holder.textViewOrderPhone.setText(String.format("Phone: %s", order.getPhone()));
            double total = 0;
            for(Item i : order.getItems())
                total+=i.getPrice();

            holder.textViewOrderTotalPrice.setText(String.format("Price: %.2f", total));
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView textViewOrderId;
            final TextView textViewOrderFullName;
            final TextView textViewOrderTotalPrice;
            final TextView textViewOrderPhone;

            ViewHolder(View view) {
                super(view);
                textViewOrderId = view.findViewById(R.id.textViewOrderId);
                textViewOrderFullName = view.findViewById(R.id.textViewOrderFullName);
                textViewOrderTotalPrice = view.findViewById(R.id.textViewOrderTotalPrice);
                textViewOrderPhone = view.findViewById(R.id.textViewOrderPhone);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        if (!Utils.checkHidingLogoutItem(this)) {
            menu.findItem(R.id.action_logout).setVisible(false);
            menu.findItem(R.id.action_orders).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (item.getItemId() == R.id.action_logout) {
            SharedPreferences sharedPref = this.getSharedPreferences(PreferenceKeys.PREFERENCES_NAME, Context.MODE_PRIVATE);
            sharedPref.edit()
                    .putString(PreferenceKeys.AUTH_TOKEN, "")
                    .putString(PreferenceKeys.AUTH_USERNAME, "")
                    .apply();
            ShoppingCart.getInstance(this).removeAll();
            startActivity(new Intent(this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
