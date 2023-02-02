package com.example.iwayplus.model.data

import androidx.lifecycle.LiveData

class DeviceOrientationRepository(private val deviceOrientationDao: DeviceOrientationDao) {
    val readAllDeviceOrientation : LiveData<List<DeviceOrientation>> = deviceOrientationDao.readAllDeviceOrientations()

    suspend fun addDeviceOrientation(deviceOrientation: DeviceOrientation){
        deviceOrientationDao.addDeviceOrientation(deviceOrientation)
    }
}