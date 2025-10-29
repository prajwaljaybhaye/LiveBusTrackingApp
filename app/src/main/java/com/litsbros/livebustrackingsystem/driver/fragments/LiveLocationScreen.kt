package com.litsbros.livebustrackingsystem.driver.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.*
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.litsbros.livebustrackingsystem.backand.RetrofitClient
import com.litsbros.livebustrackingsystem.backand.model.DriverLocationPayload
import com.litsbros.livebustrackingsystem.backand.model.DriverModel
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LiveLocationScreen(modifier: Modifier = Modifier, driver: DriverModel?) {
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    var permissionGranted by remember { mutableStateOf(locationPermissionState.status.isGranted) }

    LaunchedEffect(Unit) {
        while (!permissionGranted) {
            locationPermissionState.launchPermissionRequest()
            delay(1000)
            permissionGranted = locationPermissionState.status.isGranted
        }
    }

    if (permissionGranted) {
        if (driver != null) {
            LiveLocationContent(modifier, driver)
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Driver not logged in.")
            }
        }
    } else {
        PermissionRequestUI(locationPermissionState)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRequestUI(permissionState: PermissionState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Location permission is required to show your live location.")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { permissionState.launchPermissionRequest() }) {
            Text("Grant Permission")
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun LiveLocationContent(modifier: Modifier = Modifier, driver: DriverModel) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var location by remember { mutableStateOf<Location?>(null) }

    val cameraPositionState = rememberCameraPositionState()
    val coroutineScope = rememberCoroutineScope()
    var locationJob by remember { mutableStateOf<Job?>(null) }

    // 🔄 Start location updates
    LaunchedEffect(driver) {
        locationJob?.cancel()
        locationJob = coroutineScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val currentLocation = fusedLocationClient.lastLocation.await()
                    currentLocation?.let {
                        location = it

                        val payload = DriverLocationPayload(
                            driver_id = driver.driver_id,
                            bus_id = driver.vehicle_number?.toIntOrNull() ?: 0,
                            latitude = it.latitude,
                            longitude = it.longitude
                        )

                        try {
                            val response = RetrofitClient.api.updateLocation(payload)
                            if (!response.isSuccessful) {
                                Log.e("LocationUpdate", "Failed: ${response.code()} ${response.message()}")
                            }
                        } catch (e: Exception) {
                            Log.e("LocationUpdate", "API error: ${e.localizedMessage}", e)
                        }
                    } ?: Log.w("LocationFetch", "Location is null")
                } catch (e: Exception) {
                    Log.e("LocationFetch", "Location fetch error: ${e.localizedMessage}", e)
                }

                delay(3000) // ⏱ Update every 3 seconds
            }
        }
    }

    // 🗺️ Safely move camera when location updates
    LaunchedEffect(location) {
        location?.let {
            val latLng = LatLng(it.latitude, it.longitude)
            try {
                cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
            } catch (e: Exception) {
                Log.e("CameraUpdate", "Map not ready: ${e.localizedMessage}")
            }
        }
    }

    // 📍 UI values
    val latitude = location?.latitude ?: 0.0
    val longitude = location?.longitude ?: 0.0
    val accuracy = location?.accuracy ?: 0f

    val pulseAnim = rememberInfiniteTransition()
    val pulseRadius by pulseAnim.animateFloat(
        initialValue = 50f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // 🧭 UI layout
    Column(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            DriverMap(location, pulseRadius, cameraPositionState)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF0F2F5))
                .padding(16.dp)
        ) {
            LocationInfoCard(
                latitude = latitude,
                longitude = longitude,
                accuracy = accuracy,
                connectedUsers = 3,
                signalStrength = "Strong"
            )
        }
    }
}

@Composable
fun DriverMap(location: Location?, pulseRadius: Float, cameraPositionState: CameraPositionState) {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = true)
    ) {
        location?.let {
            val latLng = LatLng(it.latitude, it.longitude)
            Circle(
                center = latLng,
                radius = pulseRadius.toDouble(),
                strokeColor = Color.Blue,
                fillColor = Color(0x552196F3)
            )
            Marker(
                state = MarkerState(position = latLng),
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                title = "Your Location"
            )
        }
    }
}

@Composable
fun LocationInfoCard(
    latitude: Double,
    longitude: Double,
    accuracy: Float,
    connectedUsers: Int,
    signalStrength: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("📍 Current Location", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("📡 Location Sharing Active", style = MaterialTheme.typography.titleMedium)
            Text("Your live location is being broadcast")

            Spacer(modifier = Modifier.height(12.dp))
            Text("👥 Connected Users: $connectedUsers")
            Text("📶 Broadcasting: Live")

            Spacer(modifier = Modifier.height(12.dp))
            Text("🌐 Latitude: $latitude")
            Text("🌐 Longitude: $longitude")
            Text("🎯 Accuracy: High (${accuracy.toInt()}m)")
            Text("📶 Signal Strength: $signalStrength")

            Spacer(modifier = Modifier.height(12.dp))
            Text("🔄 Location updates every 3 seconds for accurate tracking", fontSize = 12.sp, color = Color.Gray)
        }
    }
}
