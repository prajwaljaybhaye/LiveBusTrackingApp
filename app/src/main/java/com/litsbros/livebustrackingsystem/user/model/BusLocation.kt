package com.litsbros.livebustrackingsystem.user.model

data class BusLocation(
    val id: String = "",
    val busNumber: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val distanceKm: Double = 0.0,
    val etaMinutes: Int = 0,
    val driverName: String = "",
    val experienceYears: Int = 0
)
