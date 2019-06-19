package com.metropolitan.techsale.utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public abstract class AbstractRetrofitService {

    public static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(2, TimeUnit.MINUTES)
            .build();

    protected Retrofit retrofit;

    protected AbstractRetrofitService(String baseUrl){
        retrofit = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create())
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build();
    }
}
