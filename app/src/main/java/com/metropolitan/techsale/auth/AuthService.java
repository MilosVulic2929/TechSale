package com.metropolitan.techsale.auth;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST("auth/login")
    Call<LoginResult> login(@Body UserDetails userDetails);

    @POST("user/register")
    Call<User> register(@Body User user);
}
