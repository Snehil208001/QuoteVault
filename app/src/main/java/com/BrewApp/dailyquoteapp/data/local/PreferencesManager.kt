package com.BrewApp.dailyquoteapp.data.local


import android.content.Context
import android.content.SharedPreferences
import com.BrewApp.dailyquoteapp.data.model.Quote
import java.time.LocalDate

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("daily_quote_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_QUOTE_TEXT = "daily_quote_text"
        private const val KEY_QUOTE_AUTHOR = "daily_quote_author"
        private const val KEY_QUOTE_DATE = "daily_quote_date"

        private const val KEY_NOTIF_ENABLED = "notif_enabled"
        private const val KEY_NOTIF_HOUR = "notif_hour"
        private const val KEY_NOTIF_MINUTE = "notif_minute"
    }

    // --- Daily Quote Logic ---
    fun saveDailyQuote(quote: Quote) {
        val today = LocalDate.now().toString()
        prefs.edit()
            .putString(KEY_QUOTE_TEXT, quote.text)
            .putString(KEY_QUOTE_AUTHOR, quote.author)
            .putString(KEY_QUOTE_DATE, today)
            .apply()
    }

    fun getDailyQuote(): Quote? {
        val text = prefs.getString(KEY_QUOTE_TEXT, null)
        val author = prefs.getString(KEY_QUOTE_AUTHOR, null)
        if (text != null && author != null) {
            return Quote(text, author)
        }
        return null
    }

    fun getLastQuoteDate(): String? {
        return prefs.getString(KEY_QUOTE_DATE, null)
    }

    fun isQuoteFromToday(): Boolean {
        val lastDate = getLastQuoteDate()
        val today = LocalDate.now().toString()
        return lastDate == today
    }

    // --- Notification Settings ---
    fun setNotificationsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_NOTIF_ENABLED, enabled).apply()
    }

    fun areNotificationsEnabled(): Boolean {
        return prefs.getBoolean(KEY_NOTIF_ENABLED, false)
    }

    fun setNotificationTime(hour: Int, minute: Int) {
        prefs.edit()
            .putInt(KEY_NOTIF_HOUR, hour)
            .putInt(KEY_NOTIF_MINUTE, minute)
            .apply()
    }

    fun getNotificationTime(): Pair<Int, Int> {
        val hour = prefs.getInt(KEY_NOTIF_HOUR, 8) // Default 8:00 AM
        val minute = prefs.getInt(KEY_NOTIF_MINUTE, 0)
        return Pair(hour, minute)
    }
}