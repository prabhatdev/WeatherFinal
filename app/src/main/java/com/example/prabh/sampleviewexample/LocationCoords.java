package com.example.prabh.sampleviewexample;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import static com.example.prabh.sampleviewexample.Fragment_Activity.latitude;
import static com.example.prabh.sampleviewexample.Fragment_Activity.longitude;

public class LocationCoords implements LocationListener{

    @Override
    public void onLocationChanged(Location location) {

        Log.d("Latitude:Longitude",location.getLongitude()+" "+location.getLatitude());
        String lat=String.format("%.2f",location.getLatitude());
        String lon=String.format("%.2f",location.getLongitude());
        setCoordinates(lat,lon);
    }

    public void setCoordinates(String lat,String lon)
    {
        latitude=lat;
        longitude=lon;
    }
    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}
