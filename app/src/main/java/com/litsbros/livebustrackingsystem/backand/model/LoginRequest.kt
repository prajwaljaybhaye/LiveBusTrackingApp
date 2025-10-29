package com.litsbros.livebustrackingsystem.backand.model

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val status: String,
    val message: String?,
    val user: User?
)
