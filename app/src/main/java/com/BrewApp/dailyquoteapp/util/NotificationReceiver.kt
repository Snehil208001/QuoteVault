package com.BrewApp.dailyquoteapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.BrewApp.dailyquoteapp.MainActivity
import com.BrewApp.dailyquoteapp.R
import com.BrewApp.dailyquoteapp.data.local.PreferencesManager
import com.BrewApp.dailyquoteapp.data.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // 1. Reschedule the next alarm immediately (since we use Exact alarms now)
        NotificationScheduler.scheduleDailyNotification(context)

        val prefs = PreferencesManager(context)

        // Use a Coroutine to fetch data if needed
        val goAsync = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 2. Get Quote (Reuse today's or fetch new if day changed)
                var quote = prefs.getDailyQuote()

                if (!prefs.isQuoteFromToday()) {
                    // Try to fetch a fresh one
                    try {
                        val quotes = RetrofitInstance.api.getQuotes()
                        if (quotes.isNotEmpty()) {
                            quote = quotes.first()
                            prefs.saveDailyQuote(quote!!)
                        }
                    } catch (e: Exception) {
                        // Fallback to existing or default if network fails
                        if (quote == null) {
                            // Absolute fallback
                            quote = com.BrewApp.dailyquoteapp.data.model.Quote("Start your day with positivity.", "QuoteVault")
                        }
                    }
                }

                // 3. Show Notification
                showNotification(context, quote!!.text, quote!!.author)
            } finally {
                goAsync.finish()
            }
        }
    }

    private fun showNotification(context: Context, title: String, message: String) {
        val channelId = "daily_quote_channel"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create Channel for Android O+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Daily Quote",
                NotificationManager.IMPORTANCE_HIGH // Increased importance
            ).apply {
                description = "Daily inspirational quotes"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Tap action
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        // Use ic_launcher_foreground for the small icon (usually better for notifications)
        // or ensure mipmap is valid. ic_launcher_foreground is usually a vector/transparent.
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Quote of the Day")
            .setContentText("\"$title\"")
            .setStyle(NotificationCompat.BigTextStyle().bigText("\"$title\"\n- $message"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1001, notification)
    }
}