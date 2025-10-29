package com.litsbros.livebustrackingsystem.backand.model

data class DriverModel(
    val driver_id: Int,
    val driver_user_id: String,
    val password: String,

    val first_name: String,
    val last_name: String? = null,
    val dob: String? = null,
    val email: String? = null,
    val blood_group: String? = null,
    val phone: String? = null,
    val alt_phone: String? = null,
    val contact_email: String? = null,
    val address: String? = null,
    val state: String? = null,
    val city: String? = null,
    val zip_code: String? = null,

    val license_number: String? = null,
    val license_expiry: String? = null,
    val license_type: String? = null,
    val driving_experience: Int? = null,

    val vehicle_number: String? = null,
    val vehicle_type: String? = null,

    val emergency_name: String? = null,
    val emergency_number: String? = null,
    val emergency_relationship: String? = null,

    val created_at: String? = null
)
