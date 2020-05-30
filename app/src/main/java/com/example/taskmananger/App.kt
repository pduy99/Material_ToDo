package com.example.taskmananger

import android.app.Application
import android.content.Context
import com.example.taskmananger.sharedpreference.SharedPrefs

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPrefs.init(this)
        App.appContext = applicationContext
    }

    companion object {
        lateinit var appContext: Context
    }
}