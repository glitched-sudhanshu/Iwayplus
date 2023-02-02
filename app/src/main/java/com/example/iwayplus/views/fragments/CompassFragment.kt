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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.iwayplus.databinding.FragmentCompassBinding
import com.example.iwayplus.model.data.DeviceOrientation
import com.example.iwayplus.utils.getCurrentTime
import com.example.iwayplus.viewmodel.DeviceOrientationViewModel
import com.example.iwayplus.views.DeviceOrientationAdapter


class CompassFragment : Fragment(), View.OnClickListener, SensorEventListener {

    private var mBinding: FragmentCompassBinding? = null
    private lateinit var sensorManager: SensorManager
    private lateinit var sensorAccelerometer: Sensor
    private lateinit var sensorMagneticField: Sensor

    private var floatGravity: FloatArray? = FloatArray(3)
    private var floatGeoMagnetic: FloatArray? = FloatArray(3)
    private var floatOrientation: FloatArray? = FloatArray(3)
    private var floatRotationMatrix: FloatArray? = FloatArray(9)

    private lateinit var mViewModel: DeviceOrientationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        mBinding = FragmentCompassBinding.inflate(inflater, container, false)

        mViewModel = ViewModelProvider(this)[DeviceOrientationViewModel::class.java]

        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClinkListeners()

        sensorManager = context?.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    }

    private fun setOnClinkListeners() {
        mBinding!!.btnSaveCompassData.setOnClickListener(this)
        mBinding!!.btnLoadCompassData.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            mBinding!!.btnSaveCompassData.id -> {
                saveDataInDatabase()
            }
            mBinding!!.btnLoadCompassData.id -> {
                loadDataFromDatabase()
            }
        }
    }

    private fun loadDataFromDatabase() {
        mBinding!!.txtCompassSavedData.visibility = View.VISIBLE
        val recyclerView = mBinding!!.rvCompassData
        recyclerView.visibility = View.VISIBLE
        val adapter = DeviceOrientationAdapter(requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mViewModel.readAllDevice.observe(viewLifecycleOwner, Observer {
                orientation ->
            adapter.setData(orientation)
        })

    }

    private fun saveDataInDatabase() {
        val deviceOrientation = DeviceOrientation(id = 0,
            azimuth = floatOrientation?.get(0)!!,
            pitch = floatOrientation?.get(1)!!,
            roll = floatOrientation?.get(2)!!,
            time = requireContext().getCurrentTime())

        mViewModel.addDeviceOrientation(deviceOrientation)
        Toast.makeText(requireContext(), "Successfully added!!", Toast.LENGTH_SHORT).show()
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
            SensorManager.getRotationMatrix(floatRotationMatrix,
                null,
                floatGravity,
                floatGeoMagnetic)
            SensorManager.getOrientation(floatRotationMatrix, floatOrientation)
            val angle = floatOrientation?.get(0)
            val finalAngle = angle?.times(-57.29582790879777f) ?: 270f
            mBinding!!.compass.rotation = finalAngle

            mBinding!!.btnSaveCompassData.isEnabled = true
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) = Unit


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