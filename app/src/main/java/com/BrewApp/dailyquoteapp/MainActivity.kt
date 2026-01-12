package com.BrewApp.dailyquoteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.BrewApp.dailyquoteapp.mainui.HomeScreen
import com.BrewApp.dailyquoteapp.ui.theme.DailyQuoteAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DailyQuoteAppTheme {
                // Call the HomeScreen composable here
                HomeScreen()
            }
        }
    }
}