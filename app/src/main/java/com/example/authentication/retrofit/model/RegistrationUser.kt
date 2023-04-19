package com.example.authentication.retrofit.model

data class RegistrationUser(
    val emailId: String?,
    val mobileNo: String,
    val name: String,
    val password: String
)