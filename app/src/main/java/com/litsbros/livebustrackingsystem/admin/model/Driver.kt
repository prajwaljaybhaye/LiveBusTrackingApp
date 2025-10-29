package com.litsbros.livebustrackingsystem.admin.model

data class Driver(
    val name: String,
    val phone: String,
    val email: String,
    val route: String,
    val busNo: String,
    val status: String
)
