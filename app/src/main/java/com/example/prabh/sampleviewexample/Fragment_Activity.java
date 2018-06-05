package com.example.prabh.sampleviewexample;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Activity extends Fragment {
    public static final String API_KEY = "d141c211a6099a6460b108c1c1b86335";
    public  CurrentLocationData data;
    public static String latitude = "";

    public static String longitude = "";
    public String cityName = "";
    //UI elements
    public TextView place, temperature, sunrise, sunset, date_time, pressure, humidity, description;
    public ImageView icon;
    public ImageButton refresh;
    public int check = 0;
    public RelativeLayout relativeLayout;
    public Fragment_Activity() {

    }

    public static String unixTimeToDateTime(int unixTimeStamp) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        date.setTime((long) unixTimeStamp * 1000);
        return dateFormat.format(date);
    }

    public CurrentLocationData getData() {
        return data;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        MainActivity activity = new MainActivity();
        place = (TextView) view.findViewById(R.id.place);
        relativeLayout=(RelativeLayout)view.findViewById(R.id.fragment_view);
        temperature = (TextView) view.findViewById(R.id.temperature);
        sunrise = (TextView) view.findViewById(R.id.sunrise);
        sunset = (TextView) view.findViewById(R.id.sunset);
        date_time = (TextView) view.findViewById(R.id.date_time);
        pressure = (TextView) view.findViewById(R.id.pressure);
        humidity = (TextView) view.findViewById(R.id.humidity);
        description = (TextView) view.findViewById(R.id.description);
        icon = (ImageView) view.findViewById(R.id.icon);
        refresh = (ImageButton) view.findViewById(R.id.refresh);
        if (bundle2string() != null)
            Log.d("Bundle", bundle2string());
        if (getArguments() != null && check == 0) {
            cityName = getArguments().getString("cityName" + (MainActivity.getPageNo() - 1));
            check = 1;
        }
        if (getArguments() != null)
            Log.d("Bundle", ("cityName" + cityName) + "PageNumber:" + (MainActivity.getPageNo() - 1));

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Refresh();
            }
        });

               location_permission();
        getLocation();
        fetchData();

        return view;
    }


    public String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void getLocation() {
        LocationManager locationManager = (LocationManager) Fragment_Activity.this.getContext().getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        if (ActivityCompat.checkSelfPermission(Fragment_Activity.this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Fragment_Activity.this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Fragment_Activity.this.getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);
        latitude = String.format("%.2f", location.getLatitude());
        longitude = String.format("%.2f", location.getLongitude());
    }



    //TO set the Ui values
    void set_UI(CurrentLocationData weather) {
        place.setText(String.format("%s", weather.getName()));
        date_time.setText(String.format("%s", getDateTime()));
        sunrise.setText(String.format("Sunrise:%s", unixTimeToDateTime(weather.getSys().getSunrise())));
        sunset.setText(String.format("Sunset:%s", unixTimeToDateTime(weather.getSys().getSunset())));
        date_time.setText(String.format("%s", getDateTime()));
        pressure.setText(String.format("Pressure:%.0f hPa", weather.getMain().getPressure()));
        humidity.setText(String.format("Humidity:%d%%", weather.getMain().getHumidity()));
        temperature.setText(String.format("%.0f°C", weather.getMain().getTemp() - 273.0));
        description.setText(String.format("%s", weather.getWeather().get(0).getDescription()));
        Picasso.get().load(String.format("http://openweathermap.org/img/w/%s.png", weather.getWeather().get(0).getIcon())).fit().centerCrop().into(icon);
    }


    //To get the location permission
    void location_permission() {
        LocationCoords loc = new LocationCoords();
        LocationManager locationManager = (LocationManager) Fragment_Activity.this.getContext().getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;

        if (ActivityCompat.checkSelfPermission(Fragment_Activity.this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Fragment_Activity.this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Fragment_Activity.this.getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, loc);
    }

    public String bundle2string() {
        if (getArguments() == null) {
            return null;
        }
        String string = "Bundle{";
        for (String key : getArguments().keySet()) {
            string += " " + key + " => " + getArguments().get(key) + ";";
        }
        string += " }Bundle";
        return string;
    }

    //TO fetch data from the server
    void fetchData() {
        Call<CurrentLocationData> call;
        if (cityName.equals("")) {
            call = weatherApi.getService().getData(latitude, longitude, API_KEY);
        } else {
            call = weatherApi.getService().getData(cityName, API_KEY);
        }
        call.enqueue(new Callback<CurrentLocationData>() {
            @Override
            public void onResponse(Call<CurrentLocationData> call, Response<CurrentLocationData> response) {
                data = response.body();
                set_UI(data);
            }

            @Override
            public void onFailure(Call<CurrentLocationData> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    public void Refresh() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchData();
                Snackbar snackbar=Snackbar.make(relativeLayout,"Refreshed",Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        },2500);
    }

}
