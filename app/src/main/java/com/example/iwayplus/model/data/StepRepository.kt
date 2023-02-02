package com.example.iwayplus.model.data

import androidx.lifecycle.LiveData

class StepRepository(private val stepDao: StepDao) {
    val readAllSteps : LiveData<List<Step>> = stepDao.readAllSteps()

    suspend fun addSteps(step: Step){
        stepDao.addStep(step = step)
    }
}