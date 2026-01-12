package com.BrewApp.dailyquoteapp.mainui.discoveryscreen.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.BrewApp.dailyquoteapp.ui.theme.PrimaryBlue

@Composable
fun DiscoveryScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Discovery Coming Soon",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryBlue
        )
    }
}