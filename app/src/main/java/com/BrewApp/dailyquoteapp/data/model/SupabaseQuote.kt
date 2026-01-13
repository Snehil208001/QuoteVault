package com.BrewApp.dailyquoteapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SupabaseQuote(
    // Changed to Long to match Supabase 'bigint' type
    @SerialName("id")
    val id: Long = 0,

    @SerialName("text")
    val text: String,

    @SerialName("author")
    val author: String,

    @SerialName("category")
    val category: String,

    // Optional field for UI state (not in DB, defaults to false)
    val isLiked: Boolean = false
)