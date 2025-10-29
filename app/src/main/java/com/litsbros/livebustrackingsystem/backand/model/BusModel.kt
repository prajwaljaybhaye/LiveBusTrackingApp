package com.litsbros.livebustrackingsystem.backand.model

data class BusModel(
    val bus_id: Int? = null, // Matches 'bus_id' in your database
    val bus_number: String,
    val registration_number: String,
    val manufacturer: String,
    val model: String,
    val seating_capacity: Int?,
    val year: Int?,
    val route_id: Int?,
    val driver_id: Int?,
    val status: String,
    val chassis_number: String,
    val engine_number: String,
    val insurance_policy_number: String,
    val insurance_expiry: String,
    val fitness_expiry: String,
    val notes: String
)


