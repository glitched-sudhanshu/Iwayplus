package com.example.iwayplus

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.iwayplus.databinding.ActivityTempBtBinding
import java.util.jar.Pack200

class TempBtActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityTempBtBinding
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private val REQ_ENABLE_BT = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityTempBtBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        bluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter
        getPermissionForBt()
    }

    private fun getPermissionForBt() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Permission for bluetooth already granted", Toast.LENGTH_SHORT)
                .show()
            enableBt()
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH_CONNECT)) {
                Toast.makeText(this, "Bluetooth permission required", Toast.LENGTH_SHORT).show()
            }
            requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_CONNECT), REQ_ENABLE_BT)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_ENABLE_BT) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableBt()
                } else {
                    Log.d("onRequestPermissionsResult: ", "Permission denied")
                    Toast.makeText(this, "Give permission", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun enableBt() {
        if (!bluetoothAdapter.isEnabled) {
            Log.d("onRequestPermissionsResult: ", "Permission granted")
            bluetoothAdapter.enable()
        }
        if(bluetoothAdapter.isEnabled)
        {
            bluetoothAdapter.disable()
        }
    }
}