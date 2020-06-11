package com.example.taskmananger.utils

import android.app.AlarmManager
import android.content.Context
import android.os.SystemClock
import androidx.core.app.AlarmManagerCompat
import com.example.taskmananger.models.ToDoItem
import com.example.taskmananger.receivers.AlarmReceiver
import java.text.SimpleDateFormat
import java.util.*

class AlarmUtil {
    companion object{
        private const val TAG = "ALARM_UTIL"

        private fun getTriggerTime(dueDate : Date) : Long{
            val calendar = Calendar.getInstance()
            calendar.time = dueDate
            val diffTime = calendar.timeInMillis - Calendar.getInstance().timeInMillis
            return SystemClock.elapsedRealtime() + diffTime
        }

        fun createAlarm(context: Context, item : ToDoItem, alarmManager: AlarmManager){
            val alarmPendingIntent = AlarmReceiver.getPendingIntent(context,item)

            AlarmManagerCompat.setExactAndAllowWhileIdle(alarmManager,AlarmManager.ELAPSED_REALTIME_WAKEUP,
                getTriggerTime(item.dueDate),alarmPendingIntent)

        }

        fun cancelAlarm(context: Context, item : ToDoItem, alarmManager: AlarmManager){
            val pendingIntent = AlarmReceiver.getPendingIntent(context,item)
            alarmManager.cancel(pendingIntent)
        }

    }
}