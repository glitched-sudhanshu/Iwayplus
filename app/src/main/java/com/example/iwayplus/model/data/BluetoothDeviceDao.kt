package com.example.iwayplus.model.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BluetoothDeviceDao {

    //this means if we have a duplicate tuple, it will ignore it
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBluetoothDevice(bluetoothDevice: BluetoothDevice)

    @Query("SELECT * FROM BLUETOOTH_DEVICE_TABLE ORDER BY id ASC")
    fun readAllBluetoothDevice() : LiveData<List<BluetoothDevice>>
}