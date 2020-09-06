package com.ziesapp.githubuserapp.provider

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.ziesapp.githubuserapp.R
import com.ziesapp.githubuserapp.activity.MainActivity

class ReminderReceiver : BroadcastReceiver() {

    companion object {
        const val TYPE_REPEATING = "Reminder"
        const val TEXT_CONTENT = "Don't forget open the app"

        private const val ID_REPEATING = 101
    }

    override fun onReceive(context: Context, intent: Intent) {
        val title = TYPE_REPEATING
        val textContent = TEXT_CONTENT

        showToast(context, title, textContent)
    }

    private fun showToast(context: Context, title: String, textContent: CharSequence) {

        val notificationIntent = Intent(context, MainActivity::class.java)
        val notificationPendingIntent =
            PendingIntent.getActivity(context, ID_REPEATING, notificationIntent, 0)

        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val mBuilder = NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
            .setSmallIcon(R.drawable.fav_solid)
            .setContentTitle(title)
            .setContentText(textContent)
            .setVibrate(longArrayOf(1000))
            .setSound(alarmSound)
            .setContentIntent(notificationPendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                MainActivity.CHANNEL_ID,
                MainActivity.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = MainActivity.CHANNEL_NAME as String?
            mBuilder.setChannelId(MainActivity.CHANNEL_ID)
            mNotificationManager.createNotificationChannel(channel)
        }

        val notification = mBuilder.build()
        mNotificationManager.notify(MainActivity.NOTIFICATION_ID, notification)
    }
}
