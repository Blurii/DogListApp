package com.example.doglistapp

import android.app.Application
import com.example.doglistapp.data.AppContainer
import com.example.doglistapp.data.DefaultAppContainer

class DoggoApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(applicationContext)
    }
}