package com.example.iwayplus.views.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.iwayplus.databinding.FragmentBluetoothBinding
import com.example.iwayplus.utils.Constants
import com.example.iwayplus.utils.receivers.BluetoothReceiver
import com.example.iwayplus.utils.receivers.DiscoverDeviceReceiver
import com.example.iwayplus.utils.receivers.DiscoverabilityReceiver
import com.example.iwayplus.viewmodel.BluetoothDeviceViewmodel
import com.example.iwayplus.model.data.BluetoothDevice as DiscoveredBluetoothDevice

class BluetoothFragment : Fragment(), View.OnClickListener {

    private var mBinding : FragmentBluetoothBinding? = null
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private var mReceiverEnableDisable: BluetoothReceiver? = null
    private var mReceiverDiscoverability: DiscoverabilityReceiver? = null
    private var mReceiverDiscoverDevices: DiscoverDeviceReceiver? = null

    private lateinit var mViewModel : BluetoothDeviceViewmodel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        mBinding = FragmentBluetoothBinding.inflate(inflater, container, false)
        mViewModel = ViewModelProvider(this)[(BluetoothDeviceViewmodel::class.java)]
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bluetoothManager = context?.getSystemService(BluetoothManager::class.java)!!
        bluetoothAdapter = bluetoothManager.adapter

        onClickListeners()
    }

    private fun onClickListeners() {
        mBinding!!.btnEnableBT.setOnClickListener(this)
        mBinding!!.btnDiscoverability.setOnClickListener(this)
        mBinding!!.btnPairedDevice.setOnClickListener(this)
        mBinding!!.btnDiscoverDevices.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            mBinding!!.btnEnableBT.id -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    getPermissionForBt(Manifest.permission.BLUETOOTH_CONNECT,
                        Constants.REQUEST_ENABLE_BT)
                } else enableBt()
            }

            mBinding!!.btnDiscoverability.id -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    getPermissionForBt(Manifest.permission.BLUETOOTH_ADVERTISE,
                        Constants.REQUEST_DISCOVERABILITY_BT)
                } else enableDiscoverability()
            }

            mBinding!!.btnPairedDevice.id -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    getPermissionForBt(Manifest.permission.BLUETOOTH_CONNECT,
                        Constants.REQUEST_PAIRED_BT)
                } else getPairedDevice()
            }

            mBinding!!.btnDiscoverDevices.id -> getDiscoverDevicesPermission()
        }
    }

    private fun getDiscoverDevicesPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            getPermissionForBt(Manifest.permission.ACCESS_COARSE_LOCATION,
                Constants.REQUEST_ACCESS_COARSE_LOCATION)
        }
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED
        ) {
            getPermissionForBt(Manifest.permission.BLUETOOTH_SCAN, Constants.REQUEST_BLUETOOTH_SCAN)
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
        context?.registerReceiver(mReceiverDiscoverDevices, filter)
    }

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
        context?.registerReceiver(mReceiverDiscoverability, intentFiler)
//        context?.unregisterReceiver(mReceiverDiscoverDevices)
//        } catch (e : SecurityException){
//                Log.i("Bluetooth Discoverability Permission", "Permission Revoked")
//        }
    }

    private fun getPermissionForBt(BtPermission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(requireContext(),
                BtPermission) == PackageManager.PERMISSION_GRANTED
        ) {
            when (requestCode) {
                Constants.REQUEST_ENABLE_BT -> enableBt()
                Constants.REQUEST_DISCOVERABILITY_BT -> enableDiscoverability()
                Constants.REQUEST_PAIRED_BT -> getPairedDevice()
                Constants.REQUEST_ACCESS_COARSE_LOCATION -> discoverDevices()
                Constants.REQUEST_BLUETOOTH_SCAN -> discoverDevices()
            }
        } else {
            if (shouldShowRequestPermissionRationale(BtPermission)) {
                Toast.makeText(requireContext(), "Bluetooth permission required", Toast.LENGTH_SHORT).show()
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
        if (requestCode == Constants.REQUEST_ENABLE_BT) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableBt()
                } else {
                    Log.d("Enable bluetooth: ", "Permission denied")
                    Toast.makeText(requireContext(), "Give permission", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (requestCode == Constants.REQUEST_DISCOVERABILITY_BT) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableDiscoverability()
                } else {
                    Log.d("Enable bluetooth discoverability ", "Permission denied")
                    Toast.makeText(requireContext(), "Give permission", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (requestCode == Constants.REQUEST_PAIRED_BT) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPairedDevice()
                } else {
                    Log.d("Enable bluetooth paired req ", "Permission denied")
                    Toast.makeText(requireContext(), "Give permission", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    discoverDevices()
                } else {
                    Log.d("Enable bluetooth discover device req ", "Permission denied")
                    Toast.makeText(requireContext(), "Give permission", Toast.LENGTH_SHORT).show()
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
                context?.registerReceiver(mReceiverEnableDisable, intentFilter)
            } catch (e: SecurityException) {
                Log.i("Bluetooth Permission", "Permission Revoked")
            }
        }
        if (bluetoothAdapter.isEnabled) {
            try {
                bluetoothAdapter.disable()
                val intentFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
                mReceiverEnableDisable = BluetoothReceiver()
                context?.registerReceiver(mReceiverEnableDisable, intentFilter)
            } catch (e: SecurityException) {
                Log.i("Bluetooth Permission", "Permission Revoked")
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        if (mReceiverDiscoverDevices != null) context?.unregisterReceiver(mReceiverDiscoverDevices)
        if (mReceiverDiscoverability != null) context?.unregisterReceiver(mReceiverDiscoverability)
        if (mReceiverEnableDisable != null) context?.unregisterReceiver(mReceiverEnableDisable)

        mBinding = null
    }
}