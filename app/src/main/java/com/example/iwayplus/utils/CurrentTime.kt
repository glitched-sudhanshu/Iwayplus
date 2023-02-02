package com.example.iwayplus.utils

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

fun Context.getCurrentTime(): String {
    val date = Date()
    val dateFormat = SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
    return dateFormat.format(date)
}