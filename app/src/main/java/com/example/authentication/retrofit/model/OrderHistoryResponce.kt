package com.example.authentication.retrofit.model

data class OrderHistoryResponce(
    val `data`: List<OrderHistoryResponceData>,
    val msg: String,
    val status: Int
)