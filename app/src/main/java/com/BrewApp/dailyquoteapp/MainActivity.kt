package com.BrewApp.dailyquoteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.BrewApp.dailyquoteapp.data.auth.AuthManager
import com.BrewApp.dailyquoteapp.navigation.AppNavGraph
import com.BrewApp.dailyquoteapp.navigation.Screens
import com.BrewApp.dailyquoteapp.ui.theme.DailyQuoteAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authManager = AuthManager()

        setContent {
            DailyQuoteAppTheme {
                // State to hold the decision and check completion
                var startDestination by remember { mutableStateOf(Screens.Login.route) }
                var isAuthChecked by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    // Check session persistence
                    if (authManager.isUserLoggedIn()) {
                        startDestination = Screens.Home.route
                    }
                    isAuthChecked = true
                }

                if (isAuthChecked) {
                    // Once checked, show the app with the correct start screen
                    AppNavGraph(startDestination = startDestination)
                } else {
                    // Minimal Splash/Loading screen while checking auth
                    Box(modifier = Modifier.fillMaxSize().background(Color.White))
                }
            }
        }
    }
}