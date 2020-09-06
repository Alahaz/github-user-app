package com.ziesapp.githubuserapp.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ziesapp.githubuserapp.R
import com.ziesapp.githubuserapp.provider.ReminderReceiver
import kotlinx.android.synthetic.main.activity_setting.*
import java.util.*

class SettingActivity : AppCompatActivity() {

    companion object {
        private const val SHARED_PREFERENCES = "sharedpreference"
        private const val BOOLEAN_KEY = "booleankey"
    }

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        alarmMgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(this, ReminderReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(this, 0, intent, 0)
        }

        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val getBoolean = sharedPreferences.getBoolean(BOOLEAN_KEY, false)
        switch_reminder.isChecked = getBoolean

        setupActionBar()

        switch_reminder.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val editor = sharedPreferences.edit()
                editor.apply {
                    putBoolean(BOOLEAN_KEY, true)
                }.apply()
                val calendar: Calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, 9)
                }
                alarmMgr?.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    alarmIntent
                )
//                alarmMgr?.set(
//                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                    SystemClock.elapsedRealtime() + 60 * 1000,
//                    alarmIntent
//                )
                Toast.makeText(
                    this,
                    "Notification activated",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val editor = sharedPreferences.edit()
                editor.apply {
                    putBoolean(BOOLEAN_KEY, false)
                }.apply()
                Toast.makeText(this, "Notification Disabled", Toast.LENGTH_SHORT).show()
            }
        }

        card_change_language.setOnClickListener {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }

    }


    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Favorited User"
    }
}