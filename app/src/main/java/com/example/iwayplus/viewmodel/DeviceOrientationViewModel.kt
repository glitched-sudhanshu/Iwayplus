package com.example.iwayplus.viewmodel

import android.app.Application
import android.graphics.drawable.GradientDrawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.iwayplus.model.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeviceOrientationViewModel(application: Application) : AndroidViewModel(application) {

//    private val _readAllDevice : MutableLiveData<List<DeviceOrientation>>

//    val readAllDevice : LiveData<List<DeviceOrientation>>

    val readAllDevice : LiveData<List<DeviceOrientation>>
    private val repository : DeviceOrientationRepository


    init {
        val deviceOrientationDao = SensorsDatabase.getDatabase(application).deviceOrientationDao()
        repository = DeviceOrientationRepository(deviceOrientationDao)
        readAllDevice = repository.readAllDeviceOrientation
    }

    fun addDeviceOrientation(orientation: DeviceOrientation){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addDeviceOrientation(orientation)
        }
    }
}

