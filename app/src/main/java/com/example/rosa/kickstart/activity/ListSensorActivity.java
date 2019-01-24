package com.example.rosa.kickstart.activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.rosa.kickstart.R;
import com.example.rosa.kickstart.adapter.RVSensorAdapter;

import java.util.List;

public class ListSensorActivity extends AppCompatActivity implements SensorEventListener {
    public static final String TAG = "ListSensorActivity";

    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sensor);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Sensor List");
        }
        RecyclerView recyclerView = findViewById(R.id.sensorRV);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = null;
        if (sensorManager != null) {
            Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ALL);
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

            sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        }
        Log.i(TAG, "onCreate: Sensors: " + sensors);

        RVSensorAdapter adapter = new RVSensorAdapter(sensors);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
