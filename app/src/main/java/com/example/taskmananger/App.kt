package com.example.taskmananger

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.content.ContextCompat
import com.example.taskmananger.sharedpreference.SharedPrefs

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPrefs.init(this)
        App.appContext = applicationContext

        createNotificationChanel(getString(R.string.reminder_notification_channel_id),
                                getString(R.string.reminder_notification_channel_name),
                                getString(R.string.reminder_notification_channel_description))
    }

    companion object {
        lateinit var appContext: Context
    }

    private fun createNotificationChanel(id : String, name: String, description: String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(id,name,NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.apply {
                enableLights(true)
                lightColor = Color.GREEN
                enableVibration(true)
            }
            notificationChannel.description = description
            val notificationManager = ContextCompat.getSystemService(this.applicationContext, NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}