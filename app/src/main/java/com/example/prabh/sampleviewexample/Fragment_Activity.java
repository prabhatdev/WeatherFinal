package com.example.prabh.sampleviewexample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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


public class Fragment_Activity extends Fragment{

    public Fragment_Activity()
    {
    }
    public Context mContext;
    @SuppressLint("ValidFragment")
    public Fragment_Activity(Context mContext) {
        this.mContext = mContext;
    }

    public static String latitude = "";
    public static String longitude = "";
    String path=API.BASE_URL+"weather?lat="+latitude+"&lon="+longitude+"&appid=d141c211a6099a6460b108c1c1b86335";

    //UI elements
    public TextView place,temperature,sunrise,sunset,date_time,pressure,humidity,description;
    public ImageView icon;
    public Button refresh;

    CurrentLocationData weather;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_layout,container,false);

        place=(TextView)view.findViewById(R.id.place);
        temperature=(TextView)view.findViewById(R.id.temperature);
        sunrise=(TextView)view.findViewById(R.id.sunrise);
        sunset=(TextView)view.findViewById(R.id.sunset);
        date_time=(TextView)view.findViewById(R.id.date_time);
        pressure=(TextView)view.findViewById(R.id.pressure);
        humidity=(TextView)view.findViewById(R.id.humidity);
        description=(TextView)view.findViewById(R.id.description);
        icon=(ImageView)view.findViewById(R.id.icon);
        refresh=(Button) view.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
        location_permission();
        getLocation();
        fetchData();

        return view;
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

    //To generate url

    String gen_url()
    {
        path=API.BASE_URL+"weather?lat="+latitude+"&lon="+longitude+"&appid=d141c211a6099a6460b108c1c1b86335";
        return path;
    }

    public  void getLocation() {
        LocationManager locationManager = (LocationManager)Fragment_Activity.this.getContext().getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        if (ActivityCompat.checkSelfPermission(Fragment_Activity.this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Fragment_Activity.this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Fragment_Activity.this.getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);
        latitude=String.format("%.2f",location.getLatitude());
        longitude=String.format("%.2f",location.getLongitude());
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


    //To get the location permission
    void location_permission() {
        LocationCoords loc = new LocationCoords();
        LocationManager locationManager = (LocationManager)Fragment_Activity.this.getContext().getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;

        if (ActivityCompat.checkSelfPermission(Fragment_Activity.this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Fragment_Activity.this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Fragment_Activity.this.getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, loc);
    }


    //TO fetch data from the server
    void fetchData()
    {
        Call<CurrentLocationData> call=weatherApi.getService().getData(gen_url());
        call.enqueue(new Callback<CurrentLocationData>() {
            @Override
            public void onResponse(Call<CurrentLocationData> call, Response<CurrentLocationData> response) {
                CurrentLocationData data=response.body();
                //Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_LONG).show();
                Log.d("Country","Sucess");
                Log.d("Country",data.getSys().getCountry());
                set_UI(data);
            }

            @Override
            public void onFailure(Call<CurrentLocationData> call, Throwable t) {
                Log.d("Country","Failure");
                Log.d("Country",t.toString());
                //Toast.makeText(FragmentActivity.this,t.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }


    public void onRefresh()
    {
        fetchData();

    }
    //Initialize the object
    void setData(CurrentLocationData data)
    {
        this.weather=data;
    }

}
