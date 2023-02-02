package com.example.iwayplus.utils.sensors

abstract class MeasurableSensor(
    protected val sensorType : Int
) {

    protected var onSensorValuesChanged : ((FloatArray) -> Unit)? = null

    abstract val doesSensorExist : Boolean

    abstract fun startListening()
    abstract fun stopListening()

    fun setOnSensorValuesChangedListener(listener : (FloatArray) -> Unit){
        onSensorValuesChanged = listener
    }
}