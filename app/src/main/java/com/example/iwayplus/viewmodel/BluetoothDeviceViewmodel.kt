package com.example.iwayplus.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.iwayplus.model.data.BluetoothDevice
import com.example.iwayplus.model.data.BluetoothDeviceRepository
import com.example.iwayplus.model.data.SensorsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BluetoothDeviceViewmodel(application: Application) : AndroidViewModel(application) {

    private val readAllDevice : LiveData<List<BluetoothDevice>>
    private val repository : BluetoothDeviceRepository

    init {
        val bluetoothDeviceDao = SensorsDatabase.getDatabase(application).bluetoothDeviceDao()
        repository = BluetoothDeviceRepository(bluetoothDeviceDao)
        readAllDevice = repository.readAllBluetoothDevice
    }

    fun addBluetoothDevice(device: BluetoothDevice){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addBluetoothDevice(device)
        }
    }
}