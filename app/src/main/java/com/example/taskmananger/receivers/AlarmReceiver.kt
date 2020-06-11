package com.example.taskmananger.receivers

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.taskmananger.R
import com.example.taskmananger.models.ToDoItem
import com.example.taskmananger.utils.INTENT_EXTRA_REMINDER_IDENTIFIER
import com.example.taskmananger.utils.INTENT_EXTRA_ROW_ID
import com.example.taskmananger.utils.INTENT_EXTRA_TITLE
import com.example.taskmananger.views.MainActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationId = intent?.extras?.getInt(INTENT_EXTRA_ROW_ID)?:0
        val reminderIdentifier = intent?.extras?.getString(INTENT_EXTRA_REMINDER_IDENTIFIER)
        val title = intent?.extras?.getString(INTENT_EXTRA_TITLE)

        val notificationIntent = Intent(context,MainActivity::class.java)
        notificationIntent.putExtra(INTENT_EXTRA_REMINDER_IDENTIFIER,reminderIdentifier)
        val pendingIntent = PendingIntent.getActivity(context,notificationId, notificationIntent,FLAG_UPDATE_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(context!!,context.getString(R.string.reminder_notification_channel_id))
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(context.getString(R.string.reminder_title))
            .setContentText(title)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java) as NotificationManager
        notificationManager.notify(notificationId,notificationBuilder.build())
    }

    companion object{
        private const val ALARM_TAG = "ALARM_RECEIVER"

        fun getPendingIntent(context : Context, item : ToDoItem) : PendingIntent{
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.apply {
                putExtra(INTENT_EXTRA_REMINDER_IDENTIFIER, item.hashCode())
                putExtra(INTENT_EXTRA_ROW_ID, item.id)
                putExtra(INTENT_EXTRA_TITLE, item.title)
            }

            return PendingIntent.getBroadcast(context,item.hashCode(),intent,PendingIntent.FLAG_CANCEL_CURRENT)
        }
    }
}