package com.litsbros.livebustrackingsystem.backand.ViewModel.DriverViewModel

import android.Manifest
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.litsbros.livebustrackingsystem.SessionManager.SessionManager
import com.litsbros.livebustrackingsystem.backand.RetrofitClient.api
import com.litsbros.livebustrackingsystem.backand.model.DriverAuth
import com.litsbros.livebustrackingsystem.backand.model.DriverModel
import com.litsbros.livebustrackingsystem.backand.model.DriverLocationPayload
import com.litsbros.livebustrackingsystem.backand.model.LiveLocationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DriverViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application.applicationContext)
    private val context = application.applicationContext

    private val _drivers = mutableStateListOf<DriverModel>()
    val drivers: List<DriverModel> get() = _drivers

    var loggedInDriver by mutableStateOf<DriverModel?>(null)
        private set

    private var locationUpdateJob: Job? = null

    init {
        loggedInDriver = sessionManager.getSavedDriver()
        fetchDrivers()
    }

    fun fetchDrivers() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.getAllDrivers()
                if (response.isSuccessful) {
                    response.body()?.let { list ->
                        withContext(Dispatchers.Main) {
                            _drivers.clear()
                            _drivers.addAll(list)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("DriverViewModel", "Error fetching drivers: ${e.message}")
            }
        }
    }

    fun reloadDrivers() = fetchDrivers()

    fun addDriver(driver: DriverModel, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (driver.driver_user_id.isBlank() || driver.password.isBlank()) {
            onError("Username and password are required")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.addDriver(driver)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Log.d("DriverViewModel", "Driver added: ${driver.first_name}")
                        onSuccess()
                        fetchDrivers()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError("API error: ${response.code()} ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Network error: ${e.localizedMessage}")
                }
            }
        }
    }

    fun loginDriver(username: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.driverLogin(DriverAuth(username, password))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == "success" && body.driver != null) {
                        loggedInDriver = body.driver
                        sessionManager.saveDriverSession(body.driver)

                        withContext(Dispatchers.Main) {
                            onSuccess()
                            startLocationUpdates() // ✅ Start location updates after login
                        }
                    } else {
                        withContext(Dispatchers.Main) { onError("Invalid login credentials") }
                    }
                } else {
                    withContext(Dispatchers.Main) { onError("Server error: ${response.message()}") }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { onError("Network error: ${e.localizedMessage}") }
            }
        }
    }

    fun logout() {
        stopLocationUpdates()
        sessionManager.clearDriverSession()
        loggedInDriver = null
    }

    fun isLoggedIn(): Boolean = sessionManager.isDriverLoggedIn()

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun startLocationUpdates() {
        val driver = loggedInDriver ?: return
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        locationUpdateJob?.cancel()
        locationUpdateJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val location = fusedLocationClient.lastLocation.await()
                    location?.let {
                        val payload = DriverLocationPayload(
                            driver_id = driver.driver_id,
                            bus_id = driver.vehicle_number?.toIntOrNull() ?: 0,
                            latitude = it.latitude,
                            longitude = it.longitude
                        )
                        val response = api.updateLocation(payload)
                        if (!response.isSuccessful) {
                            Log.e("LocationUpdate", "Failed: ${response.code()} ${response.message()}")
                        }
                    } ?: Log.w("LocationFetch", "Location is null")
                } catch (e: Exception) {
                    Log.e("LocationUpdate", "Error: ${e.localizedMessage}", e)
                }
                delay(3000)
            }
        }
    }

    fun stopLocationUpdates() {
        locationUpdateJob?.cancel()
        locationUpdateJob = null
    }



    //user view model

    fun fetchLiveBusLocations(
        onResult: (List<LiveLocationResponse>) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.getAllLiveLocations()
                if (response.isSuccessful) {
                    response.body()?.let { onResult(it) }
                } else {
                    onError("API error: ${response.code()}")
                }
            } catch (e: Exception) {
                onError("Network error: ${e.localizedMessage}")
            }
        }
    }



}

