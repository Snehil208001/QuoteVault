package com.BrewApp.dailyquoteapp.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.BrewApp.dailyquoteapp.data.local.PreferencesManager
import java.util.Calendar

object NotificationScheduler {

    fun scheduleDailyNotification(context: Context) {
        val prefs = PreferencesManager(context)
        if (!prefs.areNotificationsEnabled()) {
            cancelNotification(context)
            return
        }

        val (hour, minute) = prefs.getNotificationTime()
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Check for exact alarm permission on Android 12+ (API 31+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Log.e("NotificationScheduler", "Cannot schedule exact alarms. Permission missing.")
                return
            }
        }

        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Set the time
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // If time has passed today, schedule for tomorrow
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        try {
            // Use setExactAndAllowWhileIdle for reliable delivery even in Doze mode
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
            Log.d("NotificationScheduler", "Scheduled exactly for ${calendar.time}")
        } catch (e: SecurityException) {
            Log.e("NotificationScheduler", "Security Exception: Permission not granted", e)
        } catch (e: Exception) {
            Log.e("NotificationScheduler", "Error scheduling alarm", e)
        }
    }

    fun cancelNotification(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
        )

        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel() // Also cancel the intent itself
            Log.d("NotificationScheduler", "Notification cancelled")
        }
    }
}