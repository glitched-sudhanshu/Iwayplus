package com.example.iwayplus.model.data


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.iwayplus.utils.Constants.SENSOR_DATABASE

@Database(entities = [BluetoothDevice::class, Step::class, DeviceOrientation::class], version = 1, exportSchema = false)
abstract class SensorsDatabase : RoomDatabase() {

    abstract fun bluetoothDeviceDao() : BluetoothDeviceDao
    abstract fun stepDao() : StepDao
    abstract fun deviceOrientationDao() : DeviceOrientationDao

    companion object{
        @Volatile
        private var INSTANCE : SensorsDatabase? = null

        fun getDatabase(context: Context): SensorsDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SensorsDatabase::class.java,
                    SENSOR_DATABASE
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}