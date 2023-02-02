package com.example.iwayplus.views

import android.bluetooth.BluetoothClass
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.iwayplus.R
import com.example.iwayplus.databinding.CustomDeviceOrientationBinding
import com.example.iwayplus.model.data.DeviceOrientation

class DeviceOrientationAdapter(private val context: Context) : RecyclerView.Adapter<DeviceOrientationAdapter.ViewHolder>() {

    private var deviceList = emptyList<DeviceOrientation>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CustomDeviceOrientationBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentOrientation = deviceList[position]
        holder.txtAzimuth.text = "Azimuth: \n${currentOrientation.azimuth}"
        holder.txtPitch.text = "Pitch: \n${currentOrientation.pitch}"
        holder.txtRoll.text = "Roll: \n${currentOrientation.roll}"
        holder.txtTime.text = "Time:     ${currentOrientation.time}"
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    inner class ViewHolder(view: CustomDeviceOrientationBinding) : RecyclerView.ViewHolder(view.root){
        val txtAzimuth = view.txtAzimuth
        val txtPitch = view.txtPitch
        val txtRoll = view.txtRoll
        val txtTime = view.txtTime
    }

    fun setData(newList : List<DeviceOrientation>){
        this.deviceList = newList
        notifyDataSetChanged()
    }
}