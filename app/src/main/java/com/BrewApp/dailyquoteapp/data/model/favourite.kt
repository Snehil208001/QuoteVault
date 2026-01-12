package com.BrewApp.dailyquoteapp.data.model

data class FavouriteItem(
    val id: String,
    val title: String, // e.g., "Home", "Office", or a Place Name
    val address: String,
    val timestamp: Long
)

// UI State to handle real-world scenarios (Loading, Empty, Data)
sealed class FavouriteUiState {
    object Loading : FavouriteUiState()
    object Empty : FavouriteUiState()
    data class Success(val items: List<FavouriteItem>) : FavouriteUiState()
    data class Error(val message: String) : FavouriteUiState()
}