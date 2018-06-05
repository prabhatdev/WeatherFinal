package com.example.prabh.sampleviewexample;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int uniqueId = 1;
    public static int itemNumber = 1;
    public static int check = 0;
    public static int pageNo = 0;
    public String cityName = "";
    public SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
    RelativeLayout relativeLayout;
    ViewPager viewPager;
    Menu menu;
    FloatingActionButton addCity;
    Bundle bundle = new Bundle(100);
    Notification.Builder notification;

    public static int getPageNo() {
        return pageNo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        relativeLayout = findViewById(R.id.main_view);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(swipeAdapter);
        viewPager.setCurrentItem(0);
        notification = new Notification.Builder(this);
        notification.setAutoCancel(true);
        addCity = (FloatingActionButton) findViewById(R.id.addCity);
        addCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Enter the City Name");
                final EditText input = new EditText(MainActivity.this);
                alertDialog.setView(input);
                alertDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cityName = input.getText().toString();
                        Log.d("cityName", cityName);
                        itemNumber++;
                        bundle.putString(("cityName" + pageNo), cityName);
                        Fragment_Activity fragobj = new Fragment_Activity();
                        pageNo++;
                        fragobj.setArguments(bundle);
                        swipeAdapter.addFragment(fragobj);
                        viewPager.setAdapter(swipeAdapter);
                        viewPager.setCurrentItem(itemNumber);
                        Snackbar snackbar = Snackbar.make(relativeLayout, "City Added", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                swipeAdapter.removeFragment(pageNo--);
                                itemNumber--;
                                viewPager.setAdapter(swipeAdapter);
                                viewPager.setCurrentItem(itemNumber);
                                Snackbar snackbar1 = Snackbar.make(relativeLayout, "City Deleted", Snackbar.LENGTH_LONG);
                                snackbar1.show();
                            }
                        });
                        snackbar.show();
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notification:

                //if(item.isChecked())
            {
                item.setChecked(false);
                Log.d("Check", "working");
                //do the notification shit over here
                notification.setSmallIcon(R.drawable.weathericon);
                notification.setTicker("Current Location Weather");
                Fragment_Activity f = (Fragment_Activity) swipeAdapter.fragment_list.get(itemNumber - 1);
                notification.setContentTitle(String.format("%s %.2f C", f.getData().getName(), f.getData().getMain().getTemp() - 273.0));
                //notification.setContentTitle(String.format("%s %.2f°C", data.getName(), data.getMain().getTemp() - 273.0));
                notification.setContentText(f.getData().getWeather().get(0).getDescription());
                notification.setWhen(System.currentTimeMillis());
                //The activity which should be started after its clicked
                Intent intent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                notification.setContentIntent(pendingIntent);
                Toast.makeText(this, "Notification created", Toast.LENGTH_LONG).show();
                ;
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(uniqueId, notification.build());
            }
            return true;
            case R.id.delete:
                swipeAdapter.removeFragment(itemNumber - 1);
                itemNumber--;
                pageNo--;
                viewPager.setAdapter(swipeAdapter);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class SwipeAdapter extends FragmentStatePagerAdapter {
        public List<android.support.v4.app.Fragment> fragment_list = new ArrayList<>();

        public SwipeAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }


        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            if (position == 0 && check == 0) {
                fragment_list.add(new Fragment_Activity());
                check = 1;
            }
            return fragment_list.get(position);
        }

        @Override
        public int getCount() {
            return itemNumber;
        }

        public void addFragment(Fragment_Activity f) {
            if (f.getData() == null)

                fragment_list.add(f);
        }

        public void removeFragment(int pos) {
            fragment_list.set(pos, null);
            fragment_list.remove(pos);
            System.gc();
        }
    }

}
