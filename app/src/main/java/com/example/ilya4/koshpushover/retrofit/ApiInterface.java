package com.example.ilya4.koshpushover.retrofit;

import com.example.ilya4.koshpushover.pojo.MessageToSend;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("/1/messages.json")
    Call<MessageToSend> sendMessage(@Query("token") String token, @Query("user") String userKey, @Query("message") String message);
}
