package com.example.prabh.sampleviewexample;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface API {
    String BASE_URL="http://api.openweathermap.org/";
    @GET("/data/2.5/weather")
    Call<CurrentLocationData> getData(@Query("q") String city,@Query("appid") String id);
    @GET("/data/2.5/weather")
    Call<CurrentLocationData> getData(@Query("lat") String lat,@Query("lon") String lon,@Query("APPID") String id);

}
