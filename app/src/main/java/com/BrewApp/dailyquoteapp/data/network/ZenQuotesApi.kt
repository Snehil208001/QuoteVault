package com.BrewApp.dailyquoteapp.data.network

import com.BrewApp.dailyquoteapp.data.model.Quote
import retrofit2.http.GET

interface ZenQuotesApi {
    // Fetches 50 random quotes at once (Best practice for ZenQuotes free tier)
    @GET("quotes")
    suspend fun getQuotes(): List<Quote>
}