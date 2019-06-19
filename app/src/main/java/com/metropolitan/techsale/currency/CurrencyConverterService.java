package com.metropolitan.techsale.currency;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CurrencyConverterService {

    @GET("latest")
    Call<ResponseBody> getCurrency(@Query("access_key") String access_key, @Query("base") String base);
}