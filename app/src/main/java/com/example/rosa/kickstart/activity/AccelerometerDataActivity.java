package com.example.rosa.kickstart.activity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.rosa.kickstart.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.Random;

public class AccelerometerDataActivity extends AppCompatActivity implements SensorEventListener {
    public static final String TAG = "AccDataActivity";

    private SensorManager sensorManager;

    private TextView xValue, yValue, zValue;
    private LineChart chart;
    private Thread thread;
    private boolean plotData =true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer_data);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Accelerometer Data");
        }

        xValue = findViewById(R.id.xValue);
        yValue = findViewById(R.id.yValue);
        zValue = findViewById(R.id.zValue);

        chart = findViewById(R.id.graph);

        getSupportActionBar().setIcon(R.drawable.ic_back);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        chart.getDescription().setEnabled(true);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setDrawGridBackground(true);
        chart.setPinchZoom(false);

        LineData data = new LineData();
        data.setValueTextColor(Color.BLACK);

        chart.setBackgroundColor(Color.BLACK);
        chart.setData(data);

        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextColor(Color.WHITE);


        XAxis xAxis = chart.getXAxis();
        xAxis.setTextColor(Color.RED);
        xAxis.setDrawGridLines(false);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setEnabled(true);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(Color.GREEN);
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMaximum(15f);
        leftAxis.setAxisMinimum(-15f);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        chart.getAxisLeft().setDrawGridLines(true);
        chart.getXAxis().setDrawGridLines(false);
        chart.setDrawBorders(false);

        feedMultiple();
    }

    private void feedMultiple(){
        if (thread != null){
            thread.interrupt();
        }

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    plotData = true;
                    try {
                        Thread.sleep(10);
                    }catch (InterruptedException error){
                        error.printStackTrace();
                        Log.e(TAG, "run: " + error.getMessage());
                    }
                }
            }
        });

        thread.start();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            xValue.setText("X-Axis: " + sensorEvent.values[0]);
            yValue.setText("Y-Axis: " + sensorEvent.values[1]);
            zValue.setText("Z-Axis: " + sensorEvent.values[2]);
        }

        if (plotData){
            addEntry(sensorEvent);
            plotData = false;
        }
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
        if (thread != null){
            thread.interrupt();
        }
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (thread != null){
            thread.interrupt();
        }
        sensorManager.unregisterListener(this);
    }

    private void addEntry(SensorEvent event){
        LineData data = chart.getData();

        if (data != null){
            LineDataSet setX = (LineDataSet) data.getDataSetByIndex(0);
            LineDataSet setY = (LineDataSet) data.getDataSetByIndex(1);
            LineDataSet setZ = (LineDataSet) data.getDataSetByIndex(2);

            if (setX == null || setY == null || setZ == null){
                setX = createSet("X");
                data.addDataSet(setX);

                setY = createSet("Y");
                data.addDataSet(setY);

                setZ = createSet("Z");
                data.addDataSet(setZ);
            }
            
            data.addEntry(new Entry(setX.getEntryCount(), event.values[0]), 0);
            data.addEntry(new Entry(setY.getEntryCount(), event.values[1]), 1);
            data.addEntry(new Entry(setZ.getEntryCount(), event.values[2]), 2);

            data.notifyDataChanged();
            
            chart.notifyDataSetChanged();
            chart.setVisibleXRangeMaximum(150);
            chart.moveViewToX(data.getEntryCount());
        }
    }

    private LineDataSet createSet(String axis) {
        LineDataSet set = new LineDataSet(null, axis+"-Axis");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(2f);

        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        set.setColor(color);

        set.setHighlightEnabled(false);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        return set;
    }
}
