package com.example.rosa.kickstart.activity

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.example.rosa.kickstart.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.util.*

//import kotlin.concurrent.thread

class MagnetometerDataActivity : AppCompatActivity(), SensorEventListener{
    private lateinit var sensorManager: SensorManager

    private lateinit var xValueTV : TextView
    private lateinit var yValueTV : TextView
    private lateinit var zValueTV : TextView

    private lateinit var lineChart : LineChart
    private lateinit var lineData : LineData

    private lateinit var thread : Thread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_magnetometer_data)

        supportActionBar!!.title = "Magnetometer Data"

        xValueTV = findViewById(R.id.xValue)
        yValueTV = findViewById(R.id.yValue)
        zValueTV = findViewById(R.id.zValue)

        lineChart = findViewById(R.id.graph)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if (sensorManager != null){
            val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) as Sensor
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        lineChart.description.isEnabled = true
        lineChart.setTouchEnabled(true)
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(true)
        lineChart.setDrawGridBackground(true)
        lineChart.setPinchZoom(true)

        lineData.setValueTextColor(Color.BLACK)

        lineChart.setBackgroundColor(Color.BLACK)
        lineChart.data = lineData

    }

    fun addEntry(sensorEvent: SensorEvent){
        val data : LineData = lineChart.data

        if (data != null){
           var setX : LineDataSet = data.getDataSetByIndex(0) as LineDataSet
           var setY : LineDataSet = data.getDataSetByIndex(1) as LineDataSet
           var setZ : LineDataSet = data.getDataSetByIndex(2) as LineDataSet

            if(setX == null || setY == null || setZ == null){
                setX = createSet("X")
                data.addDataSet(setX)

                setY = createSet("Y")
                data.addDataSet(setY)

                setZ = createSet("Y")
                data.addDataSet(setZ)
            }

            data.addEntry(Entry(setX.entryCount.toFloat(), sensorEvent.values[0]), 0)
            data.addEntry(Entry(setY.entryCount.toFloat(), sensorEvent.values[0]), 1)
            data.addEntry(Entry(setZ.entryCount.toFloat(), sensorEvent.values[0]), 2)

            data.notifyDataChanged()

            lineChart.notifyDataSetChanged()
            lineChart.setVisibleXRangeMaximum(150F)
            lineChart.moveViewToX(data.entryCount.toFloat())
        }
    }

    private fun createSet(axis: String) : LineDataSet{
        val set = LineDataSet(null, "$axis-Axis")
        set.axisDependency = YAxis.AxisDependency.LEFT
        set.lineWidth = 2f

        val random = Random()
        val color: Int = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
        set.color = color

        set.isHighlightEnabled = false
        set.setDrawValues(false)
        set.setDrawCircles(false)
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.cubicIntensity = 0.2f

        return set
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        Log.i("test: ", "test")
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if (p0 != null) {
            xValueTV.text = p0.values[0].toString()
            yValueTV.text = p0.values[1].toString()
            zValueTV.text = p0.values[2].toString()
        }
    }

    //TODO: thread start

    override fun onPause() {
        super.onPause()
        if (thread != null){
            thread.interrupt()
        }
        sensorManager.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (thread != null){
            thread.interrupt()
        }
        sensorManager.unregisterListener(this)
    }
}
