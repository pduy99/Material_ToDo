package com.example.taskmananger.receivers

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.taskmananger.R
import com.example.taskmananger.models.ToDoItem
import com.example.taskmananger.utils.INTENT_EXTRA_DESTINATION_FRAGMENT
import com.example.taskmananger.utils.INTENT_EXTRA_REMINDER_IDENTIFIER
import com.example.taskmananger.utils.INTENT_EXTRA_ROW_ID
import com.example.taskmananger.utils.INTENT_EXTRA_TITLE
import com.example.taskmananger.views.MainActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationId = intent?.extras?.getInt(INTENT_EXTRA_ROW_ID)?:0
        val reminderIdentifier = intent?.extras?.getString(INTENT_EXTRA_REMINDER_IDENTIFIER)
        val title = intent?.extras?.getString(INTENT_EXTRA_TITLE)
        val fragment = intent?.extras?.getString(INTENT_EXTRA_DESTINATION_FRAGMENT)

        val notificationIntent = Intent(context,MainActivity::class.java).apply {
            putExtra("destinationFragment",fragment)
            putExtra(INTENT_EXTRA_REMINDER_IDENTIFIER,reminderIdentifier)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        //val pendingIntent = PendingIntent.getActivity(context,notificationId, notificationIntent,FLAG_UPDATE_CURRENT)
        val pendingIntent : PendingIntent? = TaskStackBuilder.create(context).run{
            addNextIntentWithParentStack(notificationIntent)
            getPendingIntent(2502,PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notificationBuilder = NotificationCompat.Builder(context!!,context.getString(R.string.reminder_notification_channel_id)).apply {
            setSmallIcon(R.drawable.ic_notification)
            setContentTitle(context.getString(R.string.reminder_title))
            setContentText(title)
            setContentIntent(pendingIntent)
            setAutoCancel(true)
            setOnlyAlertOnce(true)
            priority = NotificationCompat.PRIORITY_HIGH
        }

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
                putExtra(INTENT_EXTRA_DESTINATION_FRAGMENT,item.category)
            }

            return PendingIntent.getBroadcast(context,item.hashCode(),intent,PendingIntent.FLAG_CANCEL_CURRENT)
        }
    }
}