package com.example.prabh.sampleviewexample;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import static com.example.prabh.sampleviewexample.Fragment_Activity.latitude;
import static com.example.prabh.sampleviewexample.Fragment_Activity.longitude;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    int currentItem=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(1);
        SwipeAdapter swipeAdapter=new SwipeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeAdapter);
        viewPager.setCurrentItem(currentItem);

    }

    //To get location permission

}
