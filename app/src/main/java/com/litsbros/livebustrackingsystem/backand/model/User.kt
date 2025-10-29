package com.litsbros.livebustrackingsystem.backand.model

data class User(
    val id: Int? = null,
    val full_name: String,
    val email: String,
    val phone_number: Long,
    val password: String
)

