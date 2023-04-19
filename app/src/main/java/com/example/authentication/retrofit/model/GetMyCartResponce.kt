package com.example.authentication.retrofit.model

data class GetMyCartResponce(
    val cartTotal: Double?,
    val `data`: List<DataXX>?,
    val msg: String?,
    val status: Int?
)