package com.metropolitan.techsale.order;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.metropolitan.techsale.R;
import com.metropolitan.techsale.items.model.Item;
import com.metropolitan.techsale.shoppingcart.ShoppingCart;

import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.util.stream.Collectors.*;

public class OrderActivity extends AppCompatActivity {

    private TextView textViewAddress;
    private TextView textViewPhone;
    private TextView textViewFirstName;
    private TextView textViewLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        textViewAddress = findViewById(R.id.editTextAddress);
        textViewPhone = findViewById(R.id.editTextPhone);
        textViewFirstName = findViewById(R.id.editTextFirstName);
        textViewLastName = findViewById(R.id.editTextLastName);

    }


    public void onClickCreateOrder(View view) {
        if (isInputValid()) {
            OrderDTO order = new OrderDTO(ShoppingCart.getInstance(this).getItems().stream().map(Item::getId).collect(toList()),
                    textViewAddress.getText().toString(), textViewPhone.getText().toString(),
                    textViewFirstName.getText().toString(), textViewLastName.getText().toString());
            // TODO get token from prefs and send order to backend
            String token = "";
            new OrderServiceImpl().getOrderService().saveOrder(token, order).enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Call<Order> call, Response<Order> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(OrderActivity.this, "Order is saved",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(OrderActivity.this, "Failed to create order, please try again",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Order> call, Throwable t) {
                    Toast.makeText(OrderActivity.this, "Failed to create order, please try again",
                            Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(this, "You must fill in all fields",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isInputValid() {
        return !textViewAddress.getText().toString().isEmpty() &&
                !textViewPhone.getText().toString().isEmpty() &&
                !textViewFirstName.getText().toString().isEmpty() &&
                !textViewLastName.getText().toString().isEmpty() &&
                ShoppingCart.getInstance(this).getItems().size() > 0;
    }
}
