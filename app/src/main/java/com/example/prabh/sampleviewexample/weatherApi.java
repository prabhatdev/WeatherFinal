package com.example.prabh.sampleviewexample;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class weatherApi {
    public static API api;
    public static API getService()
    {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api=retrofit.create(API.class);
        return api;
    }
}
