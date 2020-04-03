package com.marty.beeconnect.rest.services;

import com.marty.beeconnect.LoginActivity;
import com.marty.beeconnect.adapter.IdeasAdapter;
import com.marty.beeconnect.model.IdeasModel;
import com.marty.beeconnect.model.User;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface  UserInterface {
    //standard retrofit call back
    @POST("login")
    Call<Integer> signin ( @Body LoginActivity.UserInfo userInfo );

    @GET("myprofile")
    Call<User> loadownProfile ( @QueryMap Map<String, String> params );

    @POST("postidea")
    Call<Integer> uploadIdea ( @Body MultipartBody requestBody );

    @GET("search")
    Call <List<User>> searchbees ( @QueryMap Map<String, String> params );

    @GET("myprofiletimeline")
    Call<List<IdeasModel>> getMyProfileTimeline(@QueryMap Map<String, String> params);

    @GET("getallposts")
    Call<List<IdeasModel>> getHotPosts( @QueryMap Map<String, String> params);

    @GET("getallposts")
    Call<List<IdeasModel>> getAllPosts( @QueryMap Map<String, String> params);

    @POST("votedownvote")
    Call<Integer> voteDownvote ( @Body IdeasAdapter.AddVote requestBody );
}