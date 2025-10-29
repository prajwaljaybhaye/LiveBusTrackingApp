package com.litsbros.livebustrackingsystem.backand.model

data class DriverLocationPayload(
    val driver_id: Int,
    val bus_id: Int,
    val latitude: Double,
    val longitude: Double
)


data class LiveLocationResponse(
    val driver_id: Int,
    val bus_id: Int,
    val latitude: Double,
    val longitude: Double,
    val updated_at: String
)
