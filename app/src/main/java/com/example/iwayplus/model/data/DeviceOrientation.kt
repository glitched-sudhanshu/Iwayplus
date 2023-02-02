package com.example.iwayplus.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.iwayplus.utils.Constants.DEVICE_ORIENTATION_TABLE


@Entity(tableName = DEVICE_ORIENTATION_TABLE)
data class DeviceOrientation (

    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val azimuth : Float,
    val pitch : Float,
    val roll : Float,
    val time : String
)