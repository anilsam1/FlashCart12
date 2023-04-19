package com.example.authentication.retrofit.model

data class ChangePassword(
    val confirmPass: String,
    val newPass: String
)