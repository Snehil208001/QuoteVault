package com.BrewApp.dailyquoteapp.mainui.favouritescreen.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.BrewApp.dailyquoteapp.data.db.AppDatabase
import com.BrewApp.dailyquoteapp.data.db.FavoriteQuote
import com.BrewApp.dailyquoteapp.data.model.Quote
import com.BrewApp.dailyquoteapp.data.repository.QuoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavouriteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: QuoteRepository

    init {
        // Initialize the DB and Repository
        val database = AppDatabase.Companion.getDatabase(application)
        repository = QuoteRepository(database.favoriteDao())
    }

    // Expose the list of favorites directly from the DB as a StateFlow
    val favorites: StateFlow<List<FavoriteQuote>> = repository.getAllFavorites()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun removeFavorite(favorite: FavoriteQuote) {
        viewModelScope.launch {
            // Logic to remove from DB. Since existing repository uses 'toggle' logic based on Quote object:
            val quote = Quote(text = favorite.text, author = favorite.author)
            // Passing isFavorite=true forces the toggle logic to remove it
            repository.toggleFavorite(quote, isFavorite = true)
        }
    }
}