package com.example.iwayplus.views

import com.example.iwayplus.databinding.CustomStepBinding
import com.example.iwayplus.model.data.Step
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class StepCounterAdapter(private val context: Context) : RecyclerView.Adapter<StepCounterAdapter.ViewHolder>() {

    private var stepList = emptyList<Step>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CustomStepBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentStep = stepList[position]
        holder.txtSteps.text = "Step: ${currentStep.steps}"
        holder.txtTime.text = "Time: ${currentStep.time}"
    }

    override fun getItemCount(): Int {
        return stepList.size
    }

    inner class ViewHolder(view: CustomStepBinding) : RecyclerView.ViewHolder(view.root){
        val txtTime = view.txtTime
        val txtSteps = view.txtSteps
    }

    fun setData(newList : List<Step>){
        this.stepList = newList
        notifyDataSetChanged()

    }
}