package com.example.kotlin_shake_example

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.Animation
import kotlinx.android.synthetic.main.activity_main.*
import render.animations.Attention
import render.animations.Render
import kotlin.math.sqrt

class MainActivity : AppCompatActivity(), SensorEventListener {


    private lateinit var sensorManager: SensorManager
    val TAG: String = "로그"

    private var accel: Float =0.0f
    private var accelCurrency: Float = 0.0f
    private var accelLast: Float =0.0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        accel =10f
        accelCurrency = SensorManager.GRAVITY_EARTH
        accelLast = SensorManager.GRAVITY_EARTH
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        Log.d(TAG, "MainActivity - onAccuracyChanged() called")
    }

    override fun onSensorChanged(event: SensorEvent?) {

      //  Log.d(TAG, "MainActivity - onSensorChanged() called")

        val x: Float = event?.values?.get(0) as Float
        val y: Float = event?.values?.get(0) as Float
        val z: Float = event?.values?.get(0) as Float



        x_text.text = "X: " + x.toInt().toString()
        y_text.text = "Y: " + y.toInt().toString()
        z_text.text = "Z: " + z.toInt().toString()

        accelLast = accelCurrency

        accelCurrency = sqrt((x * x + y * y + z * z).toDouble()).toFloat()

        val delta: Float = accelCurrency - accelLast

        accel = accel * 0.9f + delta

            if(accel >20){
                Log.d(TAG, "MainActivity - 흔들었음")
                Log.d(TAG, "MainActivity - accel : ${accel}")
                face_img.setImageResource(R.drawable.ic_smile)

                var render = Render(this)

                render.setAnimation(Attention().Wobble(face_img))
                render.start()



                Handler().postDelayed({
                    face_img.setImageResource(R.drawable.ic_emoji)
                },700L)
            }


    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "MainActivity - onResume() called")
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL)

    }


    //앱을 닫았을때!
    override fun onPause() {

        sensorManager.unregisterListener(this)

        super.onPause()
    }


}
