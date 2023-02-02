package com.example.iwayplus.views.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.example.iwayplus.databinding.FragmentLocationTrackerBinding
import com.example.iwayplus.model.gpsUtils.LocationService

class LocationTrackerFragment : Fragment() {

    private var mBinding: FragmentLocationTrackerBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = FragmentLocationTrackerBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION),
            0
        )

        mBinding!!.btnStart.setOnClickListener {
            Intent(requireActivity().applicationContext, LocationService::class.java).apply {
                action = LocationService.ACTION_START
                activity?.startService(this)
                context?.registerReceiver(locationBroadcastReceiver, IntentFilter("LOCATION_UPDATES"))
            }
        }

        mBinding!!.btnStop.setOnClickListener {
            Intent(requireActivity().applicationContext, LocationService::class.java).apply {
                action = LocationService.ACTION_STOP
                context?.unregisterReceiver(locationBroadcastReceiver)
                activity?.startService(this)
                mBinding!!.txtCurrLocation.text = ""
            }

        }
    }

    private val locationBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val lat = intent.getStringExtra("latitude")
            val long = intent.getStringExtra("longitude")
            mBinding!!.txtCurrLocation.text = "Longitude : $long \nLatitude : $lat"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }


}