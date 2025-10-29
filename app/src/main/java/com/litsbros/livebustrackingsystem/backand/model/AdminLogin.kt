package com.litsbros.livebustrackingsystem.backand.model

data class AdminLogin(
    val admin_id: String,
    val password: String
)


data class AdminLoginResponse(
    val status: String,
    val message: String,
    val user: Admin
)

data class Admin(
    val id: String,
    val admin_id: String,
    val full_name: String,
    val email: String,
    val phone_number: Long,
    val role: String
)