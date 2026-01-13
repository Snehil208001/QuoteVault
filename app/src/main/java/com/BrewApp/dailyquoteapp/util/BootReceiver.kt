package com.BrewApp.dailyquoteapp.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("BootReceiver", "Device booted, rescheduling notifications")
            // Reschedule the notification based on saved preferences
            NotificationScheduler.scheduleDailyNotification(context)
        }
    }
}