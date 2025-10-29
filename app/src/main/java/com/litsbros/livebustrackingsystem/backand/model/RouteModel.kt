package com.litsbros.livebustrackingsystem.backand.model

data class RouteModel(
    val arrival_time_stop2: String,
    val created_at: String,
    val description: String,
    val distance_km: String,
    val duration_min: String,
    val end_location: String,
    val route_code: String,
    val route_color: String,
    val route_id: String,
    val route_name: String,
    val start_location: String,
    val status: String,
    val stop1_name: String,
    val stop2_name: String
)