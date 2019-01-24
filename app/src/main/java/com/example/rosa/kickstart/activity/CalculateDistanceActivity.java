package com.example.rosa.kickstart.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rosa.kickstart.R;
import com.example.rosa.kickstart.StepDetector;
import com.example.rosa.kickstart.interfaces.StepListener;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CalculateDistanceActivity extends AppCompatActivity {
    public static final String TAG = "CalculateDistance";

    public static final Double EARTH_RADIUS = 6371.00;
    private double initialLat, initialLong, altitude, speed = 0.0;
    private double currentLat, currentLong;

    private long startTime, endTime, diffTime;

    private int counter = 0;
    private int numSteps;

    private StepDetector stepDetector;
    private SensorManager sensorManager;
    private Sensor sensor;

    TextView initialLatTV, initialLongTV, currentLatTV, currentLongTV, distanceTV, speedTV, altitudeTV;
    Button startCalculate, stopCalculate;
    ImageButton recalculate;
    RelativeLayout currentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_distance);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Calculate Distance Traveled");
        }

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

//        stepDetector = new StepDetector();
//        stepDetector.registerListener(this);

        initialLatTV = findViewById(R.id.initialLatitude);
        initialLongTV = findViewById(R.id.initialLongitude);
        altitudeTV = findViewById(R.id.initialAltitude);

        currentLatTV = findViewById(R.id.currentLatitude);
        currentLongTV = findViewById(R.id.currentLongitude);

        distanceTV = findViewById(R.id.distance);
        speedTV = findViewById(R.id.speed);

        startCalculate = findViewById(R.id.startCalculate);
        startCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numSteps = 0;
                startTime = System.currentTimeMillis();
                Toast.makeText(getApplicationContext(), "Let's Move!!!", Toast.LENGTH_SHORT).show();
            }
        });

        stopCalculate = findViewById(R.id.stopCalculate);
        stopCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endTime = System.currentTimeMillis();
                Toast.makeText(getApplicationContext(), R.string.wait_message, Toast.LENGTH_LONG).show();

                counter = 1;
            }
        });

        currentLayout = findViewById(R.id.currentLayout);

        recalculate = findViewById(R.id.recalculate);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    if (counter == 0) {
                        initialLat = location.getLatitude();
                        initialLatTV.setText(String.format(Locale.UK, "%.6f", initialLat));

                        initialLong = location.getLongitude();
                        initialLongTV.setText(String.format(Locale.UK, "%.6f", initialLong));

                        altitude = location.getAltitude();
                        altitudeTV.setText(String.format(Locale.UK, "%.2f", altitude).concat(" mdpl"));

                        counter = 2;
                    } else if (counter == 1) {
                        currentLat = location.getLatitude();
                        currentLatTV.setText(String.format(Locale.UK, "%.6f", currentLat));

                        currentLong = location.getLongitude();
                        currentLongTV.setText(String.format(Locale.UK, "%.6f", currentLong));

                        diffTime = endTime - startTime;
                        diffTime = TimeUnit.MILLISECONDS.toSeconds(diffTime);

                        double distance = calculatingDistance(Double.valueOf(initialLatTV.getText().toString()),
                                Double.valueOf(initialLongTV.getText().toString()), currentLat, currentLong);
                        distanceTV.setText(String.format(Locale.UK, "%.4f", distance*1000).concat(" m"));

                        speed = (distance*1000) / diffTime;
                        speedTV.setText(String.format(Locale.UK, "%.4f", speed).concat(" m/s"));

                        currentLayout.setVisibility(View.VISIBLE);

                        recalculate.setVisibility(View.VISIBLE);
                        recalculate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                currentLayout.setVisibility(View.GONE);
                                recalculate.setVisibility(View.GONE);

                                initialLatTV.setText("");
                                initialLongTV.setText("");
                                altitudeTV.setText("");

                                counter = 0;
                            }
                        });

                        counter = 2;
                    }
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
    }

//    @Override
//    public void onSensorChanged(SensorEvent sensorEvent) {
//        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
//            stepDetector.updateAccel(sensorEvent.timestamp, sensorEvent.values[0],
//                    sensorEvent.values[1], sensorEvent.values[2]);
//
//            Log.i(TAG, "onSensorChanged: " + sensorEvent.values[0] + " " + sensorEvent.values[1]);
//        }
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int i) {
//
//    }
//
//    @Override
//    public void step(long timeNs) {
//        numSteps++;
//
//        currentLayout.setVisibility(View.VISIBLE);
//        distanceTV.setText(String.valueOf(numSteps*80).concat(" cm"));
//        speedTV.setText(String.valueOf((numSteps*0.8)/endTime-startTime).concat(" m/s"));
//
//        Log.i(TAG, "step: " + numSteps);
//    }

        public double calculatingDistance(double currentLat, double currentLong, double nextLat, double nextLong){
        double radius = EARTH_RADIUS;
        double radLat = Math.toRadians(nextLat - currentLat);
        double radLong = Math.toRadians(nextLong - currentLong);

        double a = Math.sin(radLat/2)*Math.sin(radLat/2) +
                Math.sin(radLong/2)*Math.sin(radLong/2) * Math.cos(Math.toRadians(currentLat))*Math.cos(Math.toRadians(nextLat));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return radius*c;
    }
}
