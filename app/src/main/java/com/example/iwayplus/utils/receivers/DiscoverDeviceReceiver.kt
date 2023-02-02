package com.example.iwayplus.utils.receivers

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*
import com.example.iwayplus.model.data.BluetoothDevice as DiscoveredBluetoothDevice

class DiscoverDeviceReceiver : BroadcastReceiver() {

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
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
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if (device != null) {
                    Log.d("onBTDiscoverDevice", "${device.name} ${
                        device
                            .address
                    }")
                    Toast.makeText(context, "${device.name}", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
}
