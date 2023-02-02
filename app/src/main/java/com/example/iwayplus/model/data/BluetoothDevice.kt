package com.example.iwayplus.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.iwayplus.utils.Constants.BLUETOOTH_DEVICE_TABLE
import java.time.LocalDateTime


@Entity(tableName = "BLUETOOTH_DEVICE_TABLE")
data class BluetoothDevice(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val deviceName : String,
    val macAddress: String,
    val time : String
)