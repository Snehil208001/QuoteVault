package com.BrewApp.dailyquoteapp.mainui

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.BrewApp.dailyquoteapp.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val context = LocalContext.current

    // Observe Data from ViewModel
    val currentQuote by viewModel.currentQuote.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Date Logic
    val currentDate = remember { LocalDate.now() }
    val dayOfWeek = currentDate.format(DateTimeFormatter.ofPattern("EEEE"))
    val monthDay = currentDate.format(DateTimeFormatter.ofPattern("MMM dd"))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundCream)
    ) {

        // 1. Top Bar / Date Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween, // Keeps date aligned left
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = dayOfWeek.uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondary,
                    letterSpacing = 2.sp
                )
                Text(
                    text = monthDay,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
            // Removed Settings Icon Button here
        }

        // 2. Main Content Area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = PrimaryBlue)
            } else {
                // Background Quote Mark
                Text(
                    text = "“",
                    fontSize = 120.sp,
                    fontFamily = FontFamily.Serif,
                    color = PrimaryBlue.copy(alpha = 0.1f),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = (-10).dp, y = (-40).dp)
                )

                // Quote Content
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = currentQuote.text,
                        fontSize = 32.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 40.sp,
                        textAlign = TextAlign.Center,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Box(
                        modifier = Modifier
                            .width(48.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(50))
                            .background(PrimaryBlue.copy(alpha = 0.3f))
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "— ${currentQuote.author}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary,
                        letterSpacing = 1.sp
                    )
                }
            }
        }

        // 3. Actions Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp, start = 24.dp, end = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Top
        ) {
            // Refresh Action
            VerticalActionButton(
                icon = Icons.Outlined.Refresh,
                label = "New Quote",
                isPrimary = false,
                onClick = { viewModel.getNextQuote() }
            )

            // Save/Favorite Action (Connected to DB)
            VerticalActionButton(
                icon = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                label = if (isFavorite) "Saved" else "Save",
                isPrimary = true,
                onClick = { viewModel.toggleFavorite() }
            )

            // Share Action
            VerticalActionButton(
                icon = Icons.Outlined.IosShare,
                label = "Share",
                isPrimary = false,
                onClick = {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "\"${currentQuote.text}\" - ${currentQuote.author}\n\nVia Daily Quotes App")
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                }
            )
        }
    }
}

@Composable
fun VerticalActionButton(
    icon: ImageVector,
    label: String,
    isPrimary: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(if (isPrimary) 64.dp else 56.dp)
                .shadow(
                    elevation = if (isPrimary) 10.dp else 2.dp,
                    shape = CircleShape,
                    spotColor = if (isPrimary) PrimaryBlue.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.1f)
                )
                .clip(CircleShape)
                .background(if (isPrimary) PrimaryBlue else SurfaceLight)
                .then(
                    if (!isPrimary) Modifier.border(1.dp, BorderLight, CircleShape) else Modifier
                )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isPrimary) Color.White else TextSecondary,
                modifier = Modifier.size(if (isPrimary) 28.dp else 24.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (isPrimary) TextPrimary else TextMuted
        )
    }
}