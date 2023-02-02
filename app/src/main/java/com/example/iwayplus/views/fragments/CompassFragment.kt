package com.example.iwayplus.views.fragments

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.iwayplus.databinding.FragmentCompassBinding


class CompassFragment : Fragment(), View.OnClickListener, SensorEventListener {

    private var mBinding : FragmentCompassBinding? = null
    private lateinit var sensorManager : SensorManager
    private lateinit var sensorAccelerometer : Sensor
    private lateinit var sensorMagneticField : Sensor

    private var floatGravity : FloatArray? = FloatArray(3)
    private var floatGeoMagnetic : FloatArray? = FloatArray(3)
    private var floatOrientation : FloatArray? = FloatArray(3)
    private var floatRotationMatrix : FloatArray? = FloatArray(9)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        mBinding = FragmentCompassBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sensorManager = context?.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)


        mBinding!!.btnReset.setOnClickListener(this)
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
            mBinding!!.compass.rotation = finalAngle
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) = Unit

    override fun onClick(view: View?) {
        when(view?.id){
            mBinding!!.btnReset.id -> {
                resetCompass()
            }
        }
    }

    private fun resetCompass() {
        mBinding!!.compass.rotation = 0f
    }

    override fun onPause() {
        super.onPause()
        //might have to delete it.
        sensorManager.unregisterListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}