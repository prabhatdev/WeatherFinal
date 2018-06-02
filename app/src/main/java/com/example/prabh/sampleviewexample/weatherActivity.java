package com.example.prabh.sampleviewexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class weatherActivity extends AppCompatActivity {

    String cityName="";
    String path= API.BASE_URL+"weather?q="+cityName+"&appid=d141c211a6099a6460b108c1c1b86335";

    //UI elements
    public TextView place,temperature,sunrise,sunset,date_time,pressure,humidity,description;
    public ImageView icon;

    CurrentLocationData weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Bundle cityData=getIntent().getExtras();
        if(cityData==null) {
            Toast.makeText(this, "City name Invalid", Toast.LENGTH_LONG);
            return;
        }
        cityName=cityData.getString("cityName");
        gen_url();
        place=(TextView)findViewById(R.id.place);
        temperature=(TextView)findViewById(R.id.temperature);
        sunrise=(TextView)findViewById(R.id.sunrise);
        sunset=(TextView)findViewById(R.id.sunset);
        date_time=(TextView)findViewById(R.id.date_time);
        pressure=(TextView)findViewById(R.id.pressure);
        humidity=(TextView)findViewById(R.id.humidity);
        description=(TextView)findViewById(R.id.description);
        icon=(ImageView)findViewById(R.id.icon);
        fetchData();
    }

    public void onGoback(View view)
    {
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);

    }
    //To generate the url
    String gen_url()
    {
        path=API.BASE_URL+"weather?q="+cityName+"&appid=d141c211a6099a6460b108c1c1b86335";
        return path;
    }

    public String getDateTime()
    {
        DateFormat dateFormat=new SimpleDateFormat("dd MMMM yyyy HH:mm");
        Date date=new Date();
        return dateFormat.format(date);
    }

    public static String unixTimeToDateTime(int unixTimeStamp)
    {
        DateFormat dateFormat=new SimpleDateFormat("HH:mm");
        Date date=new Date();
        date.setTime((long)unixTimeStamp*1000);
        return dateFormat.format(date);
    }

    //TO set the Ui values
    void set_UI(CurrentLocationData weather)
    {
        place.setText(String.format("%s",weather.getName()));
        date_time.setText(String.format("%s",getDateTime()));
        sunrise.setText(String.format("Sunrise:%s",unixTimeToDateTime(weather.getSys().getSunrise())));
        sunset.setText(String.format("Sunset:%s",unixTimeToDateTime(weather.getSys().getSunset())));
        date_time.setText(String.format("%s",getDateTime()));
        pressure.setText(String.format("Pressure:%.0f hPa",weather.getMain().getPressure()));
        humidity.setText(String.format("Humidity:%d%%",weather.getMain().getHumidity()));
        temperature.setText(String.format("%.0f°C",weather.getMain().getTemp()-273.0));
        description.setText(String.format("%s",weather.getWeather().get(0).getDescription()));
        Picasso.get().load(String.format("http://openweathermap.org/img/w/%s.png",weather.getWeather().get(0).getIcon())).fit().centerCrop().into(icon);
    }

    void fetchData()
    {
        Call<CurrentLocationData> call= weatherApi.getService().getData(gen_url());
        call.enqueue(new Callback<CurrentLocationData>() {
            @Override
            public void onResponse(Call<CurrentLocationData> call, Response<CurrentLocationData> response) {
                CurrentLocationData data=response.body();
                if(data==null)
                {
                    Toast.makeText(weatherActivity.this,"City not found",Toast.LENGTH_LONG).show();
                    Intent i=new Intent(weatherActivity.this,MainActivity.class);
                    startActivity(i);
                    return;
                }
                else {
                    Toast.makeText(weatherActivity.this,"Success",Toast.LENGTH_LONG).show();
                    Log.d("Country", "Sucess");
                    Log.d("Country", data.getSys().getCountry());
                    set_UI(data);
                }
            }

            @Override
            public void onFailure(Call<CurrentLocationData> call, Throwable t) {
                Log.d("Country","Failure");
                Log.d("Country",t.toString());
                Toast.makeText(weatherActivity.this,t.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }


    public void onRefresh(View view)
    {
        fetchData();

    }
    //Initialize the object
    void setData(CurrentLocationData data)
    {
        this.weather=data;
    }

}
