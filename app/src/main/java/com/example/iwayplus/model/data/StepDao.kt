package com.example.iwayplus.model.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.iwayplus.utils.Constants.STEP_COUNTER_TABLE

@Dao
interface StepDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addStep(step: Step)

    @Query("SELECT * FROM $STEP_COUNTER_TABLE ORDER BY id ASC")
    fun readAllSteps() : LiveData<List<Step>>
}