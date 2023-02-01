package com.example.iwayplus.views.activities

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.iwayplus.databinding.ActivityCompassBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompassActivity : AppCompatActivity(), View.OnClickListener, SensorEventListener {
    private lateinit var mBinding : ActivityCompassBinding
    private lateinit var sensorManager : SensorManager
    private lateinit var sensorAccelerometer : Sensor
    private lateinit var sensorMagneticField : Sensor

    private var floatGravity : FloatArray? = FloatArray(3)
    private var floatGeoMagnetic : FloatArray? = FloatArray(3)
    private var floatOrientation : FloatArray? = FloatArray(3)
    private var floatRotationMatrix : FloatArray? = FloatArray(9)

//    private val viewModel by viewModels<CompassViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCompassBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)


        mBinding.btnReset.setOnClickListener(this)


//        mBinding.compass.rotation = viewModel.finalAngle.value


    }




    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, sensorMagneticField, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        when (event?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> floatGravity = event.values
            Sensor.TYPE_MAGNETIC_FIELD -> floatGeoMagnetic = event.values
        }
        if (floatGravity != null && floatGeoMagnetic != null) {
            SensorManager.getRotationMatrix(floatRotationMatrix, null, floatGravity, floatGeoMagnetic)
            SensorManager.getOrientation(floatRotationMatrix, floatOrientation)
            val angle = floatOrientation?.get(0)
            val finalAngle = angle?.times(-57.29582790879777f) ?: 270f
            mBinding.compass.rotation = finalAngle
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onClick(view: View?) {
        when(view?.id){
            mBinding.btnReset.id -> {
                resetCompass()
            }
        }
    }

    private fun resetCompass() {
        mBinding.compass.rotation = 0f
    }

    override fun onPause() {
        super.onPause()
        //might have to delete it.
        sensorManager.unregisterListener(this)
    }
}
