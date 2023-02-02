package com.example.iwayplus.views.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.iwayplus.databinding.FragmentStepCounterBinding
import com.example.iwayplus.model.data.Step
import com.example.iwayplus.utils.Constants
import com.example.iwayplus.utils.getCurrentTime
import com.example.iwayplus.viewmodel.StepViewModel
import com.example.iwayplus.views.DeviceOrientationAdapter
import com.example.iwayplus.views.StepCounterAdapter

class StepCounterFragment : Fragment(), SensorEventListener, View.OnClickListener {
    private var mBinding : FragmentStepCounterBinding? = null

    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    private var sensorManager: SensorManager? = null
    private var prog = 0

    private lateinit var mViewModel : StepViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        mBinding = FragmentStepCounterBinding.inflate(inflater, container, false)
        mViewModel = ViewModelProvider(this)[StepViewModel::class.java]
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding!!.txtTotalSteps.text = "/ ${Constants.TOTAL_STEPS} steps"

        if (isPermissionGranted()) {
            requestPermission()
        }
        setOnClickListeners()
        loadData()
        sensorManager = context?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private fun setOnClickListeners() {
        mBinding!!.btnLoadSteps.setOnClickListener(this)
        mBinding!!.btnSaveSteps.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            mBinding!!.btnSaveSteps.id -> {
                saveStepsInDatabase()
            }

            mBinding!!.btnLoadSteps.id -> {
                loadStepsFromDatabase()
            }
        }
    }

    private fun loadStepsFromDatabase() {
        mBinding!!.txtSavedSteps.visibility = View.VISIBLE
        val recyclerView = mBinding!!.rvStep
        recyclerView.visibility = View.VISIBLE
        val adapter = StepCounterAdapter(requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mViewModel.readAllStep.observe(viewLifecycleOwner) { step ->
            adapter.setData(step)
        }
    }

    private fun saveStepsInDatabase() {
        val step = Step(0, totalSteps.toInt(), requireContext().getCurrentTime())
        mViewModel.addSteps(step)
        Toast.makeText(requireContext(), "Successfully added!!", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        running = true
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null) {
            Toast.makeText(context, "No sensor detected on this device", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (running) {
            totalSteps = event!!.values[0]

            var currentSteps = totalSteps.toInt()
            if(currentSteps > Constants.TOTAL_STEPS){
                currentSteps -= Constants.TOTAL_STEPS
            }
            mBinding!!.txtSteps.text = currentSteps.toString()
            prog = (currentSteps*100/ Constants.TOTAL_STEPS)
            mBinding!!.progressBar.progress = prog
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    private fun saveData() {

        val sharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.putFloat("key1", previousTotalSteps)
        editor.putInt("prog", prog)
        editor.apply()
    }

    private fun loadData() {
        // In this function we will retrieve data
        val sharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences.getFloat("key1", 0f)
        val progSaved = sharedPreferences.getInt("prog", 0)

        // Log.d is used for debugging purposes
        Log.d("MainActivity", "$savedNumber")
        prog = progSaved
        mBinding!!.progressBar.progress = prog
        previousTotalSteps = savedNumber
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                Constants.ACTIVITY_RECOGNITION_REQUEST_CODE
            )
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACTIVITY_RECOGNITION
        ) != PackageManager.PERMISSION_GRANTED
    }

    //handle requested permission result(allow or deny)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constants.ACTIVITY_RECOGNITION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        //might have to delete it.
        saveData()
        running = false
        sensorManager?.unregisterListener(this)
        mBinding = null
    }

}