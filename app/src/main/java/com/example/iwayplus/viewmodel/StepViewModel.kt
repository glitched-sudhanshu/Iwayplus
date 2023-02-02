package com.example.iwayplus.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.iwayplus.model.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StepViewModel(application: Application) : AndroidViewModel(application) {

    val readAllStep : LiveData<List<Step>>
    private val repository : StepRepository

    init {
        val stepDao = SensorsDatabase.getDatabase(application).stepDao()
        repository = StepRepository(stepDao)
        readAllStep = repository.readAllSteps
    }

    fun addSteps(step: Step){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addSteps(step)
        }
    }
}