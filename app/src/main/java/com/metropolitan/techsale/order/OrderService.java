package com.metropolitan.techsale.order;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrderService {

    @GET("order/{username}")
    Call<List<Order>> getAllOrders(@Header("Authorization") String token, @Path("username") String username);

    @POST("order/")
    Call<String> saveOrder(@Header("Authorization") String token, @Body OrderDTO order);

}
