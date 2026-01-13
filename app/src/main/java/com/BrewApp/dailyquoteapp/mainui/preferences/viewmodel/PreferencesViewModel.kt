package com.BrewApp.dailyquoteapp.mainui.preferences.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.BrewApp.dailyquoteapp.data.local.PreferencesManager
import com.BrewApp.dailyquoteapp.util.NotificationScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreferencesViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = PreferencesManager(application)
    private val context = application.applicationContext

    private val _notificationsEnabled = MutableStateFlow(false)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    private val _notificationTime = MutableStateFlow(Pair(8, 0)) // Hour, Minute
    val notificationTime: StateFlow<Pair<Int, Int>> = _notificationTime.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        _notificationsEnabled.value = prefs.areNotificationsEnabled()
        _notificationTime.value = prefs.getNotificationTime()
    }

    fun toggleNotifications(enabled: Boolean) {
        _notificationsEnabled.value = enabled
        prefs.setNotificationsEnabled(enabled)

        if (enabled) {
            NotificationScheduler.scheduleDailyNotification(context)
        } else {
            NotificationScheduler.cancelNotification(context)
        }
    }

    fun updateTime(hour: Int, minute: Int) {
        _notificationTime.value = Pair(hour, minute)
        prefs.setNotificationTime(hour, minute)

        // Reschedule if enabled
        if (_notificationsEnabled.value) {
            NotificationScheduler.scheduleDailyNotification(context)
        }
    }
}