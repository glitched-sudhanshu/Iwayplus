package com.example.iwayplus.viewmodel

import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import com.example.iwayplus.model.sensors.Accelerometer
import com.example.iwayplus.model.sensors.MagneticField
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CompassViewModel @Inject constructor(
    private val sensorAccelerometer : Accelerometer,
    private val sensorMagnetometer : MagneticField
) : ViewModel(){
    private val _floatGravity = MutableStateFlow(FloatArray(3))
    val floatGravity: StateFlow<FloatArray>? = _floatGravity
    private val _floatGeoMagnetic = MutableStateFlow(FloatArray(3))
    val floatGeoMagnetic: StateFlow<FloatArray>? = _floatGeoMagnetic
    private val _floatOrientation = MutableStateFlow(FloatArray(3))
    val floatOrientation: StateFlow<FloatArray>? = _floatOrientation
    private val _floatRotationMatrix = MutableStateFlow(FloatArray(9))
    val floatRotationMatrix: StateFlow<FloatArray>? = _floatRotationMatrix

    private val _finalAngle = MutableStateFlow(0f)
    val finalAngle : StateFlow<Float> = _finalAngle

    init {
        sensorAccelerometer.startListening()
        sensorMagnetometer.startListening()
        sensorMagnetometer.setOnSensorValuesChangedListener { values ->
            _floatGeoMagnetic.value = values
        }
        sensorAccelerometer.setOnSensorValuesChangedListener { values ->
            _floatGravity.value = values
        }
        SensorManager.getRotationMatrix(_floatRotationMatrix.value,
            null,
            _floatGravity.value,
            _floatGeoMagnetic.value)
        SensorManager.getOrientation(_floatRotationMatrix.value, _floatOrientation.value)
        val angle = _floatOrientation.value[0]
        _finalAngle.value = angle.times(-57.29582790879777f) ?: 270f

    }

}