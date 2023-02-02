package com.example.iwayplus.utils.sensors

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor.TYPE_ACCELEROMETER
import android.hardware.Sensor.TYPE_MAGNETIC_FIELD

class Accelerometer (
    context: Context
) : AndroidSensor(
    context = context,
    sensorFeature = PackageManager.FEATURE_SENSOR_ACCELEROMETER,
    sensorType = TYPE_ACCELEROMETER
)

class MagneticField (
    context: Context
) : AndroidSensor(
    context = context,
    sensorFeature = PackageManager.FEATURE_SENSOR_COMPASS,
    sensorType = TYPE_MAGNETIC_FIELD
)