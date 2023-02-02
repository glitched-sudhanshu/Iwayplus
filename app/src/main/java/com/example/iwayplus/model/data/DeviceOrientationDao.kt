package com.example.iwayplus.model.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.iwayplus.utils.Constants.DEVICE_ORIENTATION_TABLE

@Dao
interface DeviceOrientationDao {

    //this means if we have a duplicate tuple, it will ignore it
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addDeviceOrientation(deviceOrientation: DeviceOrientation)

    @Query("SELECT * FROM $DEVICE_ORIENTATION_TABLE ORDER BY id ASC")
    fun readAllDeviceOrientations() : LiveData<List<DeviceOrientation>>

}