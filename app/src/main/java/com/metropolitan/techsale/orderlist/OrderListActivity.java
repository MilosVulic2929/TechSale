package com.metropolitan.techsale.orderlist;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.metropolitan.techsale.R;
import com.metropolitan.techsale.utils.Utils;

public class OrderListActivity extends AppCompatActivity {

    public static OrderListActivity orderListActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Utils.setStyleTheme(preferences, this);
        orderListActivity = this;
    }
}
