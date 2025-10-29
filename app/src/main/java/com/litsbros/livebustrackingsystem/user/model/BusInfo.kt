package com.litsbros.livebustrackingsystem.user.model

data class BusInfo(
    val bus_number: String,
    val route_info: String,
    val driver_name: String,
    val status: String,
    val model: String
)