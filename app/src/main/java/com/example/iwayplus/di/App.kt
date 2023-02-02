package com.example.iwayplus.di

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.iwayplus.utils.Constants.LOCATION_ID

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelTrackLocationNotification =
                NotificationChannel(LOCATION_ID, "Location", NotificationManager.IMPORTANCE_LOW)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channelTrackLocationNotification)
        }
    }
}