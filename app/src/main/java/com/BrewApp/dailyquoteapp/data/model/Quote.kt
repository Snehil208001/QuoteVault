package com.BrewApp.dailyquoteapp.data.model

import com.google.gson.annotations.SerializedName

data class Quote(
    @SerializedName("q")
    val text: String,

    @SerializedName("a")
    val author: String,

    @SerializedName("h")
    val html: String? = null
)