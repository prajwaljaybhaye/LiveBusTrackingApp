package com.litsbros.livebustrackingsystem.admin.model

data class Bus(
    val id: String,
    val name: String,
    val number: String,
    val route: String,
    val driver: String,
    val status: String,
    val seats: Int
)
