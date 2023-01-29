package com.example.iwayplus.bluetooth.receivers

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlin.math.log

object DiscoverDeviceReceiver : BroadcastReceiver() {

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d("onBTDiscoverDevice", "here")
        val action = intent?.action.toString()
        when(action)
        {
            BluetoothAdapter.ACTION_STATE_CHANGED -> {
                Log.d("onBTDiscoverDevice", "ACTION_STATE_CHANGED")
            }
            BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                Log.d("onBTDiscoverDevice", "ACTION_DISCOVERY_STARTED")
            }
            BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                Log.d("onBTDiscoverDevice", "ACTION_DISCOVERY_FINISHED")
            }
            BluetoothDevice.ACTION_FOUND -> {
                val device = intent?.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                device?.let {
                    Log.d("onBTDiscoverDevice", "${device.name} ${device
                        .address}")
                }

            }
        }
    }
}