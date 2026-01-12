package com.BrewApp.dailyquoteapp.data.repository

import com.BrewApp.dailyquoteapp.data.db.FavoriteDao
import com.BrewApp.dailyquoteapp.data.db.FavoriteQuote
import com.BrewApp.dailyquoteapp.data.model.Quote
import com.BrewApp.dailyquoteapp.data.network.RetrofitInstance
import kotlinx.coroutines.flow.Flow

class QuoteRepository(private val favoriteDao: FavoriteDao) {
    private val api = RetrofitInstance.api

    // --- API OPERATIONS ---
    suspend fun fetchRandomQuotes(): List<Quote> {
        return try {
            api.getQuotes()
        } catch (e: Exception) {
            // Fallback if offline
            listOf(Quote("Failure is simply the opportunity to begin again.", "Henry Ford"))
        }
    }

    // --- DATABASE OPERATIONS ---

    // Check if current quote is in DB
    fun isQuoteFavorite(text: String): Flow<Boolean> = favoriteDao.isFavorite(text)

    // Add or Remove logic
    suspend fun toggleFavorite(quote: Quote, isFavorite: Boolean) {
        if (isFavorite) {
            favoriteDao.deleteByText(quote.text)
        } else {
            favoriteDao.insert(FavoriteQuote(text = quote.text, author = quote.author))
        }
    }

    // Get list for Favorites Screen
    fun getAllFavorites(): Flow<List<FavoriteQuote>> = favoriteDao.getAllFavorites()
}