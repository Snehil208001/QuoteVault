package com.BrewApp.dailyquoteapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.BrewApp.dailyquoteapp.data.auth.AuthManager
import com.BrewApp.dailyquoteapp.data.auth.SupabaseClient
import com.BrewApp.dailyquoteapp.mainui.preferences.viewmodel.PreferencesViewModel
import com.BrewApp.dailyquoteapp.navigation.AppNavGraph
import com.BrewApp.dailyquoteapp.navigation.Screens
import com.BrewApp.dailyquoteapp.ui.theme.DailyQuoteAppTheme
import io.github.jan.supabase.gotrue.handleDeeplinks

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Handle Deep Links
        SupabaseClient.client.handleDeeplinks(intent)
        val authManager = AuthManager()

        setContent {
            // Get ViewModel to observe Theme/Accent/Font preferences
            val prefsViewModel: PreferencesViewModel = viewModel()
            val themeMode by prefsViewModel.themeMode.collectAsState()
            val accentColor by prefsViewModel.accentColor.collectAsState()
            val fontScale by prefsViewModel.fontScale.collectAsState()

            // Apply Theme dynamically
            DailyQuoteAppTheme(
                themeMode = themeMode,
                accentColorName = accentColor,
                fontScale = fontScale
            ) {
                // State to hold the decision and check completion
                var startDestination by remember { mutableStateOf(Screens.Login.route) }
                var isAuthChecked by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    val data = intent?.data
                    Log.d("MainActivity", "Intent data: $data")

                    // Check if this is a password recovery deep link
                    val isRecovery = data?.toString()?.contains("type=recovery") == true ||
                            data?.fragment?.contains("type=recovery") == true ||
                            data?.host == "reset-password" ||
                            data?.host == "login-callback" && data.toString().contains("recovery")

                    Log.d("MainActivity", "Is recovery: $isRecovery")
                    Log.d("MainActivity", "Full URI: ${data?.toString()}")
                    Log.d("MainActivity", "Fragment: ${data?.fragment}")
                    Log.d("MainActivity", "Query: ${data?.query}")

                    if (isRecovery) {
                        Log.d("MainActivity", "Navigating to NewPassword screen")
                        startDestination = Screens.NewPassword.route
                    } else if (authManager.isUserLoggedIn()) {
                        Log.d("MainActivity", "User is logged in, going to Home")
                        startDestination = Screens.Home.route
                    } else {
                        Log.d("MainActivity", "User not logged in, going to Login")
                        startDestination = Screens.Login.route
                    }
                    isAuthChecked = true
                }

                if (isAuthChecked) {
                    AppNavGraph(startDestination = startDestination)
                } else {
                    Box(modifier = Modifier.fillMaxSize().background(Color.White))
                }
            }
        }
    }

    // Handle new intents (when app is already running)
    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        Log.d("MainActivity", "onNewIntent called with: ${intent.data}")
        Log.d("MainActivity", "onNewIntent fragment: ${intent.data?.fragment}")
        SupabaseClient.client.handleDeeplinks(intent)

        // If it's a recovery link and app is running, we need to handle it
        val data = intent.data
        val isRecovery = data?.toString()?.contains("type=recovery") == true ||
                data?.fragment?.contains("type=recovery") == true ||
                data?.host == "reset-password" ||
                data?.host == "login-callback" && data.toString().contains("recovery")

        if (isRecovery) {
            Log.d("MainActivity", "Recovery link detected in running app - should navigate to NewPassword")
            // The deep link will be handled by Supabase client
            // You may want to programmatically navigate here if needed
        }
    }
}