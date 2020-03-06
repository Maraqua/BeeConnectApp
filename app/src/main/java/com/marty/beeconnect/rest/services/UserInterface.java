package com.marty.beeconnect.rest.services;

import com.marty.beeconnect.LoginActivity;
import com.marty.beeconnect.model.User;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface UserInterface {
    //standard retrofit call back
    @POST("login")
    Call<Integer> signin ( @Body LoginActivity.UserInfo userInfo );

    @GET("myprofile")
    Call<User> loadownProfile ( @QueryMap Map<String, String> params );

    @POST("poststatus")
    Call<Integer> uploadStatus ( @Body MultipartBody requestBody );
}