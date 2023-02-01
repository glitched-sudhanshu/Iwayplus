package com.example.iwayplus.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.iwayplus.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mBinding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        onClickListeners()
    }

    private fun onClickListeners() {
        mBinding.imgBluetooth.setOnClickListener(this)
        mBinding.imgStepCounter.setOnClickListener(this)
        mBinding.imgCompass.setOnClickListener(this)
        mBinding.imgLocation.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when(view?.id){
            mBinding.imgBluetooth.id -> {
                val intent = Intent(this@MainActivity, BluetoothActivity::class.java)
                startActivity(intent)
            }

            mBinding.imgStepCounter.id -> {
                val intent = Intent(this@MainActivity, StepCounterActivity::class.java)
                startActivity(intent)
            }

            mBinding.imgCompass.id -> {
                val intent = Intent(this@MainActivity, CompassActivity::class.java)
                startActivity(intent)
            }
            mBinding.imgLocation.id -> {
                val intent = Intent(this@MainActivity, GpsLocationActivity::class.java)
                startActivity(intent)
            }
        }
    }
}