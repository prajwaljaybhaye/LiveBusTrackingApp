package com.litsbros.livebustrackingsystem.admin.model

data class Route(
    val name: String,
    val status: String,
    val stops: Int,
    val distanceKm: Double,
    val durationMin: Int,
    val buses: Int,
    val students: Int
)
