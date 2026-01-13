package com.BrewApp.dailyquoteapp.mainui.homescreen.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.BrewApp.dailyquoteapp.data.db.AppDatabase
import com.BrewApp.dailyquoteapp.data.local.PreferencesManager
import com.BrewApp.dailyquoteapp.data.model.Quote
import com.BrewApp.dailyquoteapp.data.repository.QuoteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    // Setup DB, Repository and Prefs
    private val database = AppDatabase.getDatabase(application)
    private val repository = QuoteRepository(database.favoriteDao())
    private val prefs = PreferencesManager(application)

    // 1. Current Quote displayed
    private val _currentQuote = MutableStateFlow(Quote("Loading...", "Please wait"))
    val currentQuote: StateFlow<Quote> = _currentQuote.asStateFlow()

    // 2. Favorite Status (Automatically updates when currentQuote changes)
    @OptIn(ExperimentalCoroutinesApi::class)
    val isFavorite: StateFlow<Boolean> = _currentQuote
        .flatMapLatest { quote -> repository.isQuoteFavorite(quote.text) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    // 3. Loading State
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadDailyQuote()
    }

    private fun loadDailyQuote() {
        viewModelScope.launch {
            _isLoading.value = true

            // Check if we have a quote for today stored locally
            if (prefs.isQuoteFromToday()) {
                val storedQuote = prefs.getDailyQuote()
                if (storedQuote != null) {
                    _currentQuote.value = storedQuote
                    _isLoading.value = false
                    return@launch
                }
            }

            // If not today (or first run), fetch from API
            try {
                val fetchedQuotes = repository.fetchRandomQuotes()
                if (fetchedQuotes.isNotEmpty()) {
                    val newDailyQuote = fetchedQuotes.first()
                    // Save as today's quote
                    prefs.saveDailyQuote(newDailyQuote)
                    _currentQuote.value = newDailyQuote
                }
            } catch (e: Exception) {
                // Keep loading or show error state if needed
            }
            _isLoading.value = false
        }
    }

    // Manual refresh for the user (optional, overrides the daily rule temporarily)
    fun getNextQuote() {
        viewModelScope.launch {
            _isLoading.value = true
            val fetchedQuotes = repository.fetchRandomQuotes()
            if (fetchedQuotes.isNotEmpty()) {
                _currentQuote.value = fetchedQuotes.first()
            }
            _isLoading.value = false
        }
    }

    // Called when user clicks the Heart icon
    fun toggleFavorite() {
        viewModelScope.launch {
            val quote = _currentQuote.value
            val currentStatus = isFavorite.value
            repository.toggleFavorite(quote, currentStatus)
        }
    }
}