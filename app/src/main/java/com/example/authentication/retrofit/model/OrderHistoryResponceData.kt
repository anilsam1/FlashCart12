package com.example.authentication.retrofit.model

data class OrderHistoryResponceData(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val description: String?,
    val imageUrl: String,
    val orderId: String,
    val price: String,
    val productId: String,
    val productTotalAmount: String,
    val quantity: Int,
    val title: String,
    val updatedAt: String,
    val userId: String
)