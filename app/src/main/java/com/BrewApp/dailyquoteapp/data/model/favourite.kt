package com.BrewApp.dailyquoteapp.data.model

import com.BrewApp.dailyquoteapp.data.db.FavoriteQuote

// UI State wrapper
sealed class FavouriteUiState {
    object Loading : FavouriteUiState()
    object Empty : FavouriteUiState()
    // Updated to hold the correct DB Entity
    data class Success(val items: List<FavoriteQuote>) : FavouriteUiState()
    data class Error(val message: String) : FavouriteUiState()
}