package com.example.iwayplus.model.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class BluetoothDeviceRepository(private val bluetoothDeviceDao: BluetoothDeviceDao) {
    val readAllBluetoothDevice : LiveData<List<BluetoothDevice>> = bluetoothDeviceDao.readAllBluetoothDevice()

    suspend fun addBluetoothDevice(device: BluetoothDevice){
        bluetoothDeviceDao.addBluetoothDevice(device)
    }
}