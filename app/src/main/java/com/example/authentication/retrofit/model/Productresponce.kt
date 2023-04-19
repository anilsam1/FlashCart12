package com.example.authentication.retrofit.model

data class Productresponce(
    val `data`: List<DataX>,
    val msg: String,
    val status: Int?,
    val totalProduct: Int
)