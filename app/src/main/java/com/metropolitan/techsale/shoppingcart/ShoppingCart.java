package com.metropolitan.techsale.shoppingcart;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.metropolitan.techsale.items.model.Item;
import com.metropolitan.techsale.utils.PreferenceKeys;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * TODO info - singleton klasa posto se koristi iz vise aktivitija
 */
public class ShoppingCart {

    private static final ShoppingCart instance = new ShoppingCart();

    private Consumer<List<Item>> listener;

    public static ShoppingCart getInstance(Context context) {
        instance.context = context;
        return instance;
    }

    private final List<Item> items = new ArrayList<>();

    private Context context;

    public void add(Item item) {
        items.add(item);
        save();
        if (listener != null)
            listener.accept(items);
    }

    public void remove(Item item) {
        items.remove(item);
        if (listener != null)
            listener.accept(items);
        save();
    }

    public void removeAll() {
        items.clear();
        if (listener != null)
            listener.accept(items);
        save();
    }

    public List<Item> getItems() {
        if (items.isEmpty()) {
            SharedPreferences preferences = context.getSharedPreferences(PreferenceKeys.PREFERENCES_NAME, Context.MODE_PRIVATE);
            String json = preferences.getString(PreferenceKeys.SHOPPING_CART, "");
            if (json == null || json.isEmpty())
                return items;
            Type collectionType = new TypeToken<List<Item>>() {}.getType();
            List<Item> list = new Gson().fromJson(json, collectionType);
            items.addAll(list);
            Toast.makeText(context, "Items " + items.size(), Toast.LENGTH_SHORT).show();
        }

        return items;
    }

    private void save() {
        String json = items.isEmpty() ? "" : new Gson().toJson(items);
        SharedPreferences preferences = context.getSharedPreferences(PreferenceKeys.PREFERENCES_NAME, Context.MODE_PRIVATE);
        preferences.edit().putString(PreferenceKeys.SHOPPING_CART, json).apply();
    }

    public void addListener(Consumer<List<Item>> listener) {
        this.listener = listener;
    }

    public void removeListener() {
        this.listener = null;
    }

    private ShoppingCart() {
    }
}
