package com.example.iwayplus.di

import android.app.Application
import com.example.iwayplus.model.sensors.Accelerometer
import com.example.iwayplus.model.sensors.MagneticField
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAccelerometerSensor(app : Application) : Accelerometer {
        return Accelerometer(app)
    }

    @Provides
    @Singleton
    fun provideMagneticFieldSensor(app : Application) : MagneticField {
        return MagneticField(app)
    }
}