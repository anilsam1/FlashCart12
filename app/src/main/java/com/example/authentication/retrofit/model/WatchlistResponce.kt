package com.example.authentication.retrofit.model

data class WatchlistResponce(
    val `data`: List<DataX>,
    val msg: String,
    val status: Int?
)