package com.example.prabh.sampleviewexample;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface API {
    String BASE_URL="http://api.openweathermap.org/data/2.5/";
    @GET
    Call<CurrentLocationData> getData(@Url String url);

}
