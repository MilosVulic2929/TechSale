package com.metropolitan.techsale.order;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface OrderService {

    @GET
    Call<List<Order>> getAllOrders();

    @POST
    Call<Order> saveOrder(@Header("Authorization") String token, @Body OrderDTO order);

}
