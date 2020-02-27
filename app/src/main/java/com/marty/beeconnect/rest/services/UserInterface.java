package com.marty.beeconnect.rest.services;

import com.marty.beeconnect.LoginActivity;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserInterface {
    //standard retrofit call back
    @POST("login")
    Call<Integer>signin(@Body LoginActivity.UserInfo userInfo);
}
