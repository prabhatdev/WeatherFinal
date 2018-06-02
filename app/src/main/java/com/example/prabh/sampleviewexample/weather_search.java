package com.example.prabh.sampleviewexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class weather_search extends Fragment {

    public weather_search() {

    }
    public EditText cityName;
    public Button submit;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view=inflater.inflate(R.layout.activity_weather_search,container,false);
        submit=(Button)view.findViewById(R.id.submit);
        cityName = (EditText)view.findViewById(R.id.cityName);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(weather_search.this.getContext(), weatherActivity.class);
                String intentMessage = cityName.getText().toString();
                i.putExtra("cityName", intentMessage);
                startActivity(i);
            }
        });
        return view;
    }
}
