package com.example.iwayplus.views.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.iwayplus.databinding.ActivityStepCounterBinding
import com.example.iwayplus.model.utils.Constants.ACTIVITY_RECOGNITION_REQUEST_CODE
import com.example.iwayplus.model.utils.Constants.TOTAL_STEPS

class StepCounterActivity : AppCompatActivity(), SensorEventListener {
    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    private lateinit var mBinding : ActivityStepCounterBinding
    private var sensorManager: SensorManager? = null
    private var prog = 0

//    private val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityStepCounterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

    mBinding.txtTotalSteps.text = "/ $TOTAL_STEPS steps"

    if (isPermissionGranted()) {
        requestPermission()
    }
//    mBinding.progressBar.progress = prog
    loadData()
    resetSteps()
    sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
}

    override fun onResume() {

        super.onResume()
        running = true

        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (running) {
            totalSteps = event!!.values[0]

            var currentSteps = totalSteps.toInt()
            if(currentSteps > TOTAL_STEPS){
                currentSteps -= TOTAL_STEPS
            }
            mBinding.txtSteps.text = currentSteps.toString()
            prog = (currentSteps*100/ TOTAL_STEPS)
            mBinding.progressBar.progress = prog
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        println("onAccuracyChanged: Sensor: $sensor; accuracy: $accuracy")
    }

    private fun resetSteps() {
        mBinding.txtSteps.setOnClickListener {
            // This will give a toast message if the user want to reset the steps
            Toast.makeText(this, "Long tap to reset steps", Toast.LENGTH_SHORT).show()
        }

        mBinding.txtSteps.setOnLongClickListener {

            previousTotalSteps = totalSteps

            // When the user will click long tap on the screen,
            // the steps will be reset to 0
            mBinding.txtSteps.text = 0.toString()

            prog = 0
            mBinding.progressBar.progress = prog

            // This will save the data
            saveData()

            true
        }
    }

    private fun saveData() {

        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.putFloat("key1", previousTotalSteps)
        editor.putInt("prog", prog)
        editor.apply()
    }

    private fun loadData() {

        // In this function we will retrieve data
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences.getFloat("key1", 0f)
        val progSaved = sharedPreferences.getInt("prog", 0)

        // Log.d is used for debugging purposes
        Log.d("MainActivity", "$savedNumber")
        prog = progSaved
        mBinding.progressBar.progress = prog
        previousTotalSteps = savedNumber
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                ACTIVITY_RECOGNITION_REQUEST_CODE
            )
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACTIVITY_RECOGNITION
        ) != PackageManager.PERMISSION_GRANTED
    }

    //handle requested permission result(allow or deny)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ACTIVITY_RECOGNITION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //might have to delete it.
        running = false
        sensorManager?.unregisterListener(this)
    }
}