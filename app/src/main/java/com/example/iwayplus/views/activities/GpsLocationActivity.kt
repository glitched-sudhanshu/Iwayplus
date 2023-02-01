package com.example.iwayplus.views.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.iwayplus.databinding.ActivityGpsLocationBinding
import com.example.iwayplus.model.gpsUtils.LocationService

class GpsLocationActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityGpsLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityGpsLocationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION),
            0
        )

        mBinding.btnStart.setOnClickListener{
            Intent(applicationContext, LocationService::class.java).apply {
                action = LocationService.ACTION_START
                startService(this)
                registerReceiver(locationBroadcastReceiver, IntentFilter("LOCATION_UPDATES"))
            }
        }

        mBinding.btnStop.setOnClickListener{
            Intent(applicationContext, LocationService::class.java).apply {
                action = LocationService.ACTION_STOP
                unregisterReceiver(locationBroadcastReceiver)
                startService(this)
                mBinding.txtCurrLocation.text = ""
            }

        }
    }

    private val locationBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val lat = intent.getStringExtra("latitude")
            val long = intent.getStringExtra("longitude")

            mBinding.txtCurrLocation.text = "Longitude : $long \nLatitude : $lat"
        }
    }

}