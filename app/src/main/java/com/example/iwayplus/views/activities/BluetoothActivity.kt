package com.example.iwayplus.views.activities


import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.iwayplus.receivers.BluetoothReceiver
import com.example.iwayplus.receivers.DiscoverDeviceReceiver
import com.example.iwayplus.receivers.DiscoverabilityReceiver
import com.example.iwayplus.databinding.ActivityBluetoothBinding
import com.example.iwayplus.model.utils.Constants.REQUEST_ACCESS_COARSE_LOCATION
import com.example.iwayplus.model.utils.Constants.REQUEST_BLUETOOTH_SCAN
import com.example.iwayplus.model.utils.Constants.REQUEST_DISCOVERABILITY_BT
import com.example.iwayplus.model.utils.Constants.REQUEST_ENABLE_BT
import com.example.iwayplus.model.utils.Constants.REQUEST_PAIRED_BT


class BluetoothActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mBinding: ActivityBluetoothBinding
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private var mReceiverEnableDisable: BluetoothReceiver? = null
    private var mReceiverDiscoverability: DiscoverabilityReceiver? = null
    private var mReceiverDiscoverDevices: DiscoverDeviceReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityBluetoothBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        bluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter

        onClickListeners()




    }

    private fun onClickListeners() {
        mBinding.btnEnableBT.setOnClickListener(this)
        mBinding.btnDiscoverability.setOnClickListener(this)
        mBinding.btnPairedDevice.setOnClickListener(this)
        mBinding.btnDiscoverDevices.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            mBinding.btnEnableBT.id -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    getPermissionForBt(Manifest.permission.BLUETOOTH_CONNECT,
                        REQUEST_ENABLE_BT)
                } else enableBt()
            }

            mBinding.btnDiscoverability.id -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    getPermissionForBt(Manifest.permission.BLUETOOTH_ADVERTISE,
                        REQUEST_DISCOVERABILITY_BT)
                } else enableDiscoverability()
            }

            mBinding.btnPairedDevice.id -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    getPermissionForBt(Manifest.permission.BLUETOOTH_CONNECT,
                        REQUEST_PAIRED_BT)
                } else getPairedDevice()
            }

            mBinding.btnDiscoverDevices.id -> getDiscoverDevicesPermission()
        }
    }

    private fun getDiscoverDevicesPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            getPermissionForBt(Manifest.permission.ACCESS_COARSE_LOCATION,
                REQUEST_ACCESS_COARSE_LOCATION)
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED
        ) {
            getPermissionForBt(Manifest.permission.BLUETOOTH_SCAN, REQUEST_BLUETOOTH_SCAN)
        }
        discoverDevices()
    }

    @SuppressLint("MissingPermission")
    private fun discoverDevices() {
        bluetoothAdapter.startDiscovery()
        Log.d("discoverDevices", "Permission Granted")
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        mReceiverDiscoverDevices = DiscoverDeviceReceiver()
        registerReceiver(mReceiverDiscoverDevices, filter)
//        bluetoothAdapter.cancelDiscovery()
    }

//    private var mReceiverDiscoverDevices = object : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//            when (intent?.action) {
//                BluetoothAdapter.ACTION_STATE_CHANGED -> {
//                    Log.d("onBTDiscoverDevice", "ACTION_STATE_CHANGED")
//                }
//                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
//                    Log.d("onBTDiscoverDevice", "ACTION_DISCOVERY_STARTED")
//                }
//                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
//                    Log.d("onBTDiscoverDevice", "ACTION_DISCOVERY_FINISHED")
//                }
//                BluetoothDevice.ACTION_FOUND -> {
//                    val device =
//                        intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
//                    if (device != null) {
//                        Log.d("onBTDiscoverDevice", "${device.name} ${
//                            device
//                                .address
//                        }")
//                    }
//
//                }
//            }
//        }
//    }

    private fun getPairedDevice() {
        try {
            Log.d("paired", "getPairedDevice: pressed")
            val arrPairedDevices = bluetoothAdapter.bondedDevices
            Log.d("bonded devices", "${arrPairedDevices.size}")
            for (device in arrPairedDevices) {
                Log.d("Paired device:", "Device: ${device.name} ${device.address}")
            }
        } catch (e: SecurityException) {
            Log.i("Bluetooth Pairing Permission", "Permission Revoked")
        }

    }

    @SuppressLint("MissingPermission")
    private fun enableDiscoverability() {
//        try {
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 10)
        startActivity(intent)
        val intentFiler = IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
        mReceiverDiscoverability = DiscoverabilityReceiver()
        registerReceiver(mReceiverDiscoverability, intentFiler)
        unregisterReceiver(mReceiverDiscoverDevices)
//        } catch (e : SecurityException){
//                Log.i("Bluetooth Discoverability Permission", "Permission Revoked")
//        }
    }

    private fun getPermissionForBt(BtPermission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this,
                BtPermission) == PackageManager.PERMISSION_GRANTED
        ) {
            when (requestCode) {
                REQUEST_ENABLE_BT -> enableBt()
                REQUEST_DISCOVERABILITY_BT -> enableDiscoverability()
                REQUEST_PAIRED_BT -> getPairedDevice()
                REQUEST_ACCESS_COARSE_LOCATION -> discoverDevices()
                REQUEST_BLUETOOTH_SCAN -> discoverDevices()
            }
        } else {
            if (shouldShowRequestPermissionRationale(BtPermission)) {
                Toast.makeText(this, "Bluetooth permission required", Toast.LENGTH_SHORT).show()
                Log.d("Enable bluetooth", "Bluetooth permission required")
            }
            requestPermissions(arrayOf(BtPermission), requestCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_ENABLE_BT) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableBt()
                } else {
                    Log.d("Enable bluetooth: ", "Permission denied")
                    Toast.makeText(this, "Give permission", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (requestCode == REQUEST_DISCOVERABILITY_BT) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableDiscoverability()
                } else {
                    Log.d("Enable bluetooth discoverability ", "Permission denied")
                    Toast.makeText(this, "Give permission", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (requestCode == REQUEST_PAIRED_BT) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPairedDevice()
                } else {
                    Log.d("Enable bluetooth paired req ", "Permission denied")
                    Toast.makeText(this, "Give permission", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    discoverDevices()
                } else {
                    Log.d("Enable bluetooth discover device req ", "Permission denied")
                    Toast.makeText(this, "Give permission", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun enableBt() {
        if (!bluetoothAdapter.isEnabled) {
            try {
                bluetoothAdapter.enable()
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivity(intent)
                val intentFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
                mReceiverEnableDisable = BluetoothReceiver()
                registerReceiver(mReceiverEnableDisable, intentFilter)
            } catch (e: SecurityException) {
                Log.i("Bluetooth Permission", "Permission Revoked")
            }
        }
        if (bluetoothAdapter.isEnabled) {
            try {
                bluetoothAdapter.disable()
                val intentFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
                mReceiverEnableDisable = BluetoothReceiver()
                registerReceiver(mReceiverEnableDisable, intentFilter)
            } catch (e: SecurityException) {
                Log.i("Bluetooth Permission", "Permission Revoked")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        if (mReceiverDiscoverDevices != null) unregisterReceiver(mReceiverDiscoverDevices)
        if (mReceiverDiscoverability != null) unregisterReceiver(mReceiverDiscoverability)
        if (mReceiverEnableDisable != null) unregisterReceiver(mReceiverEnableDisable)

    }
}