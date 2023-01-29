package com.example.iwayplus.bluetooth


import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.iwayplus.bluetooth.receivers.BluetoothReceiver
import com.example.iwayplus.bluetooth.receivers.DiscoverabilityReceiver
import com.example.iwayplus.databinding.ActivityBluetoothBinding


class BluetoothActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mBinding: ActivityBluetoothBinding
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private val REQUEST_ENABLE_BT = 101
    private val REQUEST_PERMISSION_BT = 2
    private lateinit var mReceiverEnableDisable: BluetoothReceiver
    private lateinit var mReceiverDiscoverability : DiscoverabilityReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityBluetoothBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        bluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter

        onClickListeners()
        mReceiverEnableDisable = BluetoothReceiver()
        mReceiverDiscoverability = DiscoverabilityReceiver()

    }

    private fun onClickListeners() {
        mBinding.btnEnableBT.setOnClickListener(this)
        mBinding.btnDiscoverability.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            mBinding.btnEnableBT.id -> {
                enableBt()
            }

            mBinding.btnDiscoverability.id -> {
                enableDiscoverability()
            }
        }
    }

    private fun enableDiscoverability() {
        when {
            ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.BLUETOOTH_ADVERTISE) == PackageManager.PERMISSION_GRANTED -> {

            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.BLUETOOTH_ADVERTISE) -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.BLUETOOTH_ADVERTISE),
                        REQUEST_ENABLE_BT)
                }
            }
        }
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 20)
        startActivity(intent)
        val intentFiler = IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
        registerReceiver(mReceiverDiscoverability, intentFiler)
    }

    private fun enableBt() {
        when {
            ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED -> {

            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.BLUETOOTH_CONNECT) -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.BLUETOOTH_CONNECT),
                        REQUEST_ENABLE_BT)
                }
            }
        }

        if (!bluetoothAdapter.isEnabled) {
            bluetoothAdapter.enable()
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivity(intent)
            val intentFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
            registerReceiver(mReceiverEnableDisable, intentFilter)
        }

        if (bluetoothAdapter.isEnabled) {
            bluetoothAdapter.disable()
            val intentFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
            registerReceiver(mReceiverEnableDisable, intentFilter)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mReceiverEnableDisable)
        unregisterReceiver(mReceiverDiscoverability)
    }
}