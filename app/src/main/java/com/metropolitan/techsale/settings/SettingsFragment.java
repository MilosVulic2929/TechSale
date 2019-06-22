package com.metropolitan.techsale.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;

import com.metropolitan.techsale.MainActivity;
import com.metropolitan.techsale.R;
import com.metropolitan.techsale.auth.LoginActivity;
import com.metropolitan.techsale.auth.RegistrationActivity;
import com.metropolitan.techsale.items.ItemListActivity;
import com.metropolitan.techsale.order.OrderActivity;
import com.metropolitan.techsale.orderlist.OrderDetailActivity;
import com.metropolitan.techsale.orderlist.OrderListActivity;
import com.metropolitan.techsale.shoppingcart.ShoppingCartActivity;
import com.metropolitan.techsale.utils.Utils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SwitchPreferenceCompat switchThemePref;
    private ListPreference currenciesList;
    private SharedPreferences preferences;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.preferences, s);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        switchThemePref = (SwitchPreferenceCompat) findPreference("themeKey");
        currenciesList = (android.support.v7.preference.ListPreference) findPreference("currencyKey");

        switchThemePref.setOnPreferenceChangeListener((preference, o) -> {
            Utils.setStyleTheme(preferences, getActivity());
            Objects.requireNonNull(getActivity()).recreate();
            recreating();
            return true;
        });

        currenciesList.setOnPreferenceChangeListener((preference, o) -> {
            MainActivity.oldValue = currenciesList.getValue();
            return true;
        });
    }

    private void recreating() {
        List<Activity> list = Stream.of(MainActivity.mainActivity,
                ShoppingCartActivity.shoppingCartActivity,
                OrderListActivity.orderListActivity,
                OrderActivity.orderActivity,
                ItemListActivity.itemListActivity,
                RegistrationActivity.registrationActivity,
                LoginActivity.loginActivity,
                OrderDetailActivity.orderDetailActivity).collect(Collectors.toList());

        list.forEach(item -> {
            if (item != null) {
                item.recreate();
            }
        });
    }
}
