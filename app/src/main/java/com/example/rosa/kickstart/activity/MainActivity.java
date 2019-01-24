package com.example.rosa.kickstart.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.rosa.kickstart.R;
import com.example.rosa.kickstart.activity.AccelerometerDataActivity;
import com.example.rosa.kickstart.activity.CalculateDistanceActivity;
import com.example.rosa.kickstart.activity.GPS_DataActivity;
import com.example.rosa.kickstart.activity.GyroscopeDataActivity;
import com.example.rosa.kickstart.activity.ListSensorActivity;

public class MainActivity extends AppCompatActivity{
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout listLayout = findViewById(R.id.listLayout);
        listLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toList = new Intent(view.getContext(), ListSensorActivity.class);
                startActivity(toList);
            }
        });

        RelativeLayout accelerometerLayout = findViewById(R.id.accelerometerLayout);
        accelerometerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAccelerometer = new Intent(view.getContext(), AccelerometerDataActivity.class);
                startActivity(toAccelerometer);
            }
        });

        RelativeLayout gyroscopeLayout = findViewById(R.id.gyroscopeLayout);
        gyroscopeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toGyroscope = new Intent(getApplicationContext(), GyroscopeDataActivity.class);
                startActivity(toGyroscope);
            }
        });

        RelativeLayout magnetometerLayout = findViewById(R.id.magnetometerLayout);
        magnetometerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toMagnetometer = new Intent(getApplicationContext(), MagnetometerDataActivity.class);
                startActivity(toMagnetometer);
            }
        });

        RelativeLayout gpsLayout = findViewById(R.id.gpsLayout);
        gpsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gpsPage = new Intent(view.getContext(), GPS_DataActivity.class);
                startActivity(gpsPage);
            }
        });

        RelativeLayout calculateLayout = findViewById(R.id.calculateLayout);
        calculateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent calculatePage = new Intent(view.getContext(), CalculateDistanceActivity.class);
                startActivity(calculatePage);
            }
        });

    }

}
