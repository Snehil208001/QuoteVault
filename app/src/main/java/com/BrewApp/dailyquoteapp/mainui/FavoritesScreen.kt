package com.BrewApp.dailyquoteapp.mainui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.rounded.FormatQuote
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.BrewApp.dailyquoteapp.data.db.FavoriteQuote
import com.BrewApp.dailyquoteapp.ui.theme.*

@Composable
fun FavouriteScreen(
    onBackClick: () -> Unit,
    onItemClick: (FavoriteQuote) -> Unit,
    viewModel: FavouriteViewModel = viewModel()
) {
    val favorites by viewModel.favorites.collectAsState()

    // 1. Force BackgroundCream to match HomeScreen exactly
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundCream) // Use the same Cream background as Home
            .statusBarsPadding()
    ) {
        // --- Header Section ---
        FavoritesHeader(onBackClick)

        // --- Content Section ---
        if (favorites.isEmpty()) {
            EmptyStateView()
        } else {
            LazyColumn(
                contentPadding = PaddingValues(top = 16.dp, start = 24.dp, end = 24.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(favorites, key = { it.id }) { quote ->
                    FavoriteQuoteCard(
                        quote = quote,
                        onDelete = { viewModel.removeFavorite(quote) }
                    )
                }
            }
        }
    }
}

@Composable
fun FavoritesHeader(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp), // Matched Home padding
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back Button (Styled like Home buttons - White Circle)
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(SurfaceLight) // White background
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Go back",
                tint = TextPrimary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Title
        Text(
            text = "My Favorites",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun FavoriteQuoteCard(
    quote: FavoriteQuote,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp), // Soft roundness like Home
        colors = CardDefaults.cardColors(
            containerColor = SurfaceLight // White Card
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.padding(24.dp)) {

            // Delete Button (Top Right)
            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 12.dp, y = (-12).dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete quote",
                    tint = TextMuted.copy(alpha = 0.7f),
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                // Quote Icon
                Icon(
                    imageVector = Icons.Rounded.FormatQuote,
                    contentDescription = null,
                    tint = PrimaryBlue.copy(alpha = 0.2f),
                    modifier = Modifier.size(32.dp)
                )

                // Quote Text - Using Serif to match Home
                Text(
                    text = "\"${quote.text}\"",
                    fontFamily = FontFamily.Serif, // Match Home Font
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 28.sp,
                    color = TextPrimary
                )

                // Footer: Author
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Small Blue Line like Home
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .height(2.dp)
                            .background(PrimaryBlue.copy(alpha = 0.3f))
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = quote.author,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyStateView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.FormatQuote,
            contentDescription = null,
            tint = TextMuted.copy(alpha = 0.3f),
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Favorites Yet",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextSecondary
        )
        Text(
            text = "Save quotes from the Home screen to see them here.",
            fontSize = 14.sp,
            color = TextMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
        )
    }
}