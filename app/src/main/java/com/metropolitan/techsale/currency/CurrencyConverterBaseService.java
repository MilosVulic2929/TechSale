package com.metropolitan.techsale.currency;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public abstract class CurrencyConverterBaseService {

    protected Retrofit retrofit;

    public final static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(2, TimeUnit.MINUTES)
            .build();

    public CurrencyConverterBaseService() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://data.fixer.io/api/")
                .client(okHttpClient)
                .build();
    }
}