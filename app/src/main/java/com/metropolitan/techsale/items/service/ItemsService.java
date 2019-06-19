package com.metropolitan.techsale.items.service;

import com.metropolitan.techsale.items.model.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface ItemsService {


    @GET("item/")
    Call<List<Item>> getItems();

}
