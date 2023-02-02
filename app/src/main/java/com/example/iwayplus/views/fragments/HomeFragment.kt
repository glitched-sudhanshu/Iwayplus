package com.example.iwayplus.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.iwayplus.R
import com.example.iwayplus.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), View.OnClickListener {

    private var mBinding : FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListeners()
    }

    private fun onClickListeners() {
        mBinding!!.imgBluetooth.setOnClickListener(this)
        mBinding!!.imgStepCounter.setOnClickListener(this)
        mBinding!!.imgCompass.setOnClickListener(this)
        mBinding!!.imgLocation.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            mBinding!!.imgBluetooth.id -> {
                findNavController().navigate(R.id.action_homeFragment_to_bluetoothFragment)
            }

            mBinding!!.imgStepCounter.id -> {
                findNavController().navigate(R.id.action_homeFragment_to_stepCounterFragment)
            }

            mBinding!!.imgCompass.id -> {
                findNavController().navigate(R.id.action_homeFragment_to_compassFragment)
            }
            mBinding!!.imgLocation.id -> {
                findNavController().navigate(R.id.action_homeFragment_to_locationTrackerFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

}