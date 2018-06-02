package com.example.prabh.sampleviewexample;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SwipeAdapter extends FragmentPagerAdapter {

    public SwipeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        List<Fragment> fragment_list=new ArrayList<>();
        fragment_list.add(new Fragment_Activity());

        fragment_list.add(new weather_search());
        return fragment_list.get(position);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
