package com.example.iwayplus.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.iwayplus.utils.Constants.STEP_COUNTER_TABLE

@Entity(tableName = STEP_COUNTER_TABLE)
data class Step(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val steps : Int,
    val time : String
)