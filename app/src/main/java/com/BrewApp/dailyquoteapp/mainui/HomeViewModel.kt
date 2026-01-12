package com.BrewApp.dailyquoteapp.mainui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.BrewApp.dailyquoteapp.data.db.AppDatabase
import com.BrewApp.dailyquoteapp.data.model.Quote
import com.BrewApp.dailyquoteapp.data.repository.QuoteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    // Setup DB and Repository
    private val database = AppDatabase.getDatabase(application)
    private val repository = QuoteRepository(database.favoriteDao())

    // Buffer for quotes to avoid hitting API too often
    private var quoteBuffer: List<Quote> = emptyList()
    private var currentIndex = 0

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
        fetchQuotes()
    }

    fun fetchQuotes() {
        viewModelScope.launch {
            _isLoading.value = true
            val fetchedQuotes = repository.fetchRandomQuotes()
            if (fetchedQuotes.isNotEmpty()) {
                quoteBuffer = fetchedQuotes
                currentIndex = 0
                _currentQuote.value = quoteBuffer[currentIndex]
            }
            _isLoading.value = false
        }
    }

    fun getNextQuote() {
        if (quoteBuffer.isNotEmpty()) {
            currentIndex = (currentIndex + 1) % quoteBuffer.size
            _currentQuote.value = quoteBuffer[currentIndex]
        } else {
            fetchQuotes()
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