package com.litsbros.livebustrackingsystem.SessionManager


import android.content.Context
import com.litsbros.livebustrackingsystem.backand.model.Admin
import com.litsbros.livebustrackingsystem.backand.model.DriverModel

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("driver_session", Context.MODE_PRIVATE)
    private val editor = prefs.edit()



    // Save driver session
    fun saveDriverSession(driver: DriverModel) {
        editor.apply {
            putInt("driver_id", driver.driver_id)
            putString("driver_user_id", driver.driver_user_id)
            putString("password", driver.password)

            putString("first_name", driver.first_name)
            putString("last_name", driver.last_name)
            putString("dob", driver.dob)
            putInt("driving_experience", driver.driving_experience ?: -1)

            putString("email", driver.email)
            putString("phone", driver.phone)
            putString("alt_phone", driver.alt_phone)
            putString("contact_email", driver.contact_email)

            putString("address", driver.address)
            putString("city", driver.city)
            putString("state", driver.state)
            putString("zip_code", driver.zip_code)

            putString("blood_group", driver.blood_group)
            putString("license_number", driver.license_number)
            putString("license_expiry", driver.license_expiry)
            putString("license_type", driver.license_type)

            putString("vehicle_number", driver.vehicle_number)
            putString("vehicle_type", driver.vehicle_type)

            putString("emergency_name", driver.emergency_name)
            putString("emergency_number", driver.emergency_number)
            putString("emergency_relationship", driver.emergency_relationship)

            putString("created_at", driver.created_at)
            apply()
        }
    }

    // Restore driver session
    fun getSavedDriver(): DriverModel? {
        val id = prefs.getInt("driver_id", -1)
        if (id == -1) return null

        return DriverModel(
            driver_id = id,
            driver_user_id = prefs.getString("driver_user_id", "") ?: "",
            password = prefs.getString("password", "") ?: "",

            first_name = prefs.getString("first_name", "") ?: "",
            last_name = prefs.getString("last_name", "")?.takeIf { it.isNotBlank() },
            dob = prefs.getString("dob", "")?.takeIf { it.isNotBlank() },
            driving_experience = prefs.getInt("driving_experience", -1).takeIf { it != -1 },
            email = prefs.getString("email", "")?.takeIf { it.isNotBlank() },
            phone = prefs.getString("phone", "") ?: "",
            alt_phone = prefs.getString("alt_phone", "")?.takeIf { it.isNotBlank() },
            contact_email = prefs.getString("contact_email", "")?.takeIf { it.isNotBlank() },

            address = prefs.getString("address", "") ?: "",
            city = prefs.getString("city", "") ?: "",
            state = prefs.getString("state", "") ?: "",
            zip_code = prefs.getString("zip_code", "") ?: "",

            blood_group = prefs.getString("blood_group", "")?.takeIf { it.isNotBlank() },
            license_number = prefs.getString("license_number", "") ?: "",
            license_expiry = prefs.getString("license_expiry", "")?.takeIf { it.isNotBlank() },
            license_type = prefs.getString("license_type", "")?.takeIf { it.isNotBlank() },

            vehicle_number = prefs.getString("vehicle_number", "") ?: "",
            vehicle_type = prefs.getString("vehicle_type", "") ?: "",

            emergency_name = prefs.getString("emergency_name", "")?.takeIf { it.isNotBlank() },
            emergency_number = prefs.getString("emergency_number", "")?.takeIf { it.isNotBlank() },
            emergency_relationship = prefs.getString("emergency_relationship", "")?.takeIf { it.isNotBlank() },

            created_at = prefs.getString("created_at", "")?.takeIf { it.isNotBlank() }
        )
    }


    // Clear session
    fun clearDriverSession() {
        prefs.edit().clear().apply()
    }

    // Check if logged in
    fun isDriverLoggedIn(): Boolean = prefs.getInt("driver_id", -1) != -1



    //----------------------user session ----------------------
    fun saveUserSession(userId: Int, fullName: String, email: String, phoneNumber: Long) {
        editor.apply {
            putInt("user_id", userId)
            putString("user_full_name", fullName)
            putString("user_email", email)
            putLong("user_phone", phoneNumber)
            apply()
        }
    }

    fun getSavedUserId(): Int? {
        val id = prefs.getInt("user_id", -1)
        return if (id != -1) id else null
    }

    fun isUserLoggedIn(): Boolean = prefs.getInt("user_id", -1) != -1

    fun clearUserSession() {
        editor.remove("user_id")
        editor.remove("user_full_name")
        editor.remove("user_email")
        editor.remove("user_phone")
        editor.apply()
    }


    // ---------------------- admin session ----------------------

    // ---------------------- admin session ----------------------

    fun saveAdminSession(id: String, adminId: String, fullName: String, email: String, phoneNumber: Long, role: String) {
        editor.apply {
            putString("admin_id_internal", id) // internal DB id
            putString("admin_id", adminId)     // public admin ID
            putString("admin_full_name", fullName)
            putString("admin_email", email)
            putLong("admin_phone", phoneNumber)
            putString("admin_role", role)
            apply()
        }
    }

    fun isAdminLoggedIn(): Boolean = prefs.getString("admin_id", null) != null

    fun clearAdminSession() {
        editor.remove("admin_id_internal")
        editor.remove("admin_id")
        editor.remove("admin_full_name")
        editor.remove("admin_email")
        editor.remove("admin_phone")
        editor.remove("admin_role")
        editor.apply()
    }
    fun getSavedAdmin(): Admin? {
        val id = prefs.getString("admin_id_internal", null) ?: return null
        val adminId = prefs.getString("admin_id", null) ?: return null
        val fullName = prefs.getString("admin_full_name", "") ?: ""
        val email = prefs.getString("admin_email", "") ?: ""
        val phone = prefs.getLong("admin_phone", -1)
        val role = prefs.getString("admin_role", "") ?: ""

        return Admin(
            id = id,
            admin_id = adminId,
            full_name = fullName,
            email = email,
            phone_number = phone,
            role = role
        )
    }



}
