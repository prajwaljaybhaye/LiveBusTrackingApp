package com.litsbros.livebustrackingsystem.user.components.TrackBusComponents


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.content.PermissionChecker
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Card
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.maps.android.SphericalUtil
import com.google.maps.android.compose.*
import com.litsbros.livebustrackingsystem.R
import androidx.compose.material3.Card
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import com.litsbros.livebustrackingsystem.backand.ViewModel.DriverViewModel.DriverViewModel
import com.litsbros.livebustrackingsystem.backand.ViewModel.BusViewModel
import com.litsbros.livebustrackingsystem.user.model.BusLocation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@SuppressLint("MissingPermission")
@Composable
fun LiveBusMap(
    viewModel: DriverViewModel = viewModel(),
    busModel: BusViewModel = viewModel()
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var userLatLng by remember { mutableStateOf<LatLng?>(null) }
    var busList by remember { mutableStateOf<List<BusLocation>>(emptyList()) }
    var selectedBus by remember { mutableStateOf<BusLocation?>(null) }

    val scope = rememberCoroutineScope()
    val markerStates = remember { mutableStateMapOf<String, MarkerState>() }

    var hasLocationPermission by remember {
        mutableStateOf(
            PermissionChecker.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) ==
                    PermissionChecker.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasLocationPermission = isGranted
            scope.launch {
                if (isGranted) {
                    try {
                        val location = fusedLocationClient.lastLocation.await()
                        location?.let {
                            userLatLng = LatLng(it.latitude, it.longitude)
                        }
                    } catch (e: Exception) {
                        Log.e("LocationFetch", "Error: ${e.localizedMessage}")
                    }
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            try {
                val location = fusedLocationClient.lastLocation.await()
                location?.let {
                    userLatLng = LatLng(it.latitude, it.longitude)
                }
            } catch (e: Exception) {
                Log.e("LocationFetch", "Error: ${e.localizedMessage}")
            }
        }
    }

    // 🔄 Fetch and animate bus locations every 5 seconds
    LaunchedEffect(Unit) {
        while (true) {
            viewModel.fetchLiveBusLocations(
                onResult = { locations ->
                    busList = locations.map { location ->
                        val busId = location.driver_id.toString()
                        val newLatLng = LatLng(location.latitude, location.longitude)
                        val markerState =
                            markerStates.getOrPut(busId) { MarkerState(position = newLatLng) }

                        scope.launch {
                            val start = markerState.position
                            val end = newLatLng
                            val steps = 20
                            val duration = 1000L
                            val interval = duration / steps

                            for (i in 1..steps) {
                                val lat =
                                    start.latitude + (end.latitude - start.latitude) * (i / steps.toDouble())
                                val lng =
                                    start.longitude + (end.longitude - start.longitude) * (i / steps.toDouble())
                                markerState.position = LatLng(lat, lng)
                                delay(interval)
                            }
                        }

                        val distance = userLatLng?.let { user ->
                            SphericalUtil.computeDistanceBetween(user, newLatLng) / 1000
                        } ?: 0.0

                        val averageSpeedKmph = 30.0
                        val eta =
                            if (distance > 0) ((distance / averageSpeedKmph) * 60).toInt() else 0

                        BusLocation(
                            id = busId,
                            busNumber = "Bus-${location.bus_id}",
                            latitude = location.latitude,
                            longitude = location.longitude,
                            distanceKm = distance,
                            etaMinutes = eta,
                            driverName = "Driver ${location.driver_id}",
                            experienceYears = 0
                        )
                    }
                },
                onError = { error -> Log.e("LiveBusMap", error) }
            )
            delay(5000)
        }
    }

    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(userLatLng) {
        userLatLng?.let {
            delay(500)
            cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(it, 13f))
        }
    }

    LaunchedEffect(busList) {
        if (userLatLng == null && busList.isNotEmpty()) {
            val fallbackLatLng = LatLng(busList.first().latitude, busList.first().longitude)
            delay(500)
            cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(fallbackLatLng, 13f))
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = hasLocationPermission)
        ) {
            val customUserIcon = bitmapDescriptorFromVector(context, R.drawable.userlogo, 100, 100)
            val customBusIcon = bitmapDescriptorFromVector(context, R.drawable.bus, 100, 100)

            userLatLng?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "You",
                    icon = customUserIcon
                )
            }

            busList.forEach { bus ->
                val markerState = markerStates[bus.id] ?: MarkerState(
                    position = LatLng(
                        bus.latitude,
                        bus.longitude
                    )
                )
                Marker(
                    state = markerState,
                    title = bus.busNumber,
                    snippet = "Driver: ${bus.driverName}",
                    icon = customBusIcon,
                    onClick = {
                        selectedBus = bus
                        busModel.clearBusInfo()
                        val busId = bus.busNumber.removePrefix("Bus-").toIntOrNull()
                        busId?.let { busModel.fetchBusInfoByBusId(it) }
                        false
                    }
                )
            }

            selectedBus?.let { bus ->
                userLatLng?.let { userPos ->
                    Polyline(
                        points = listOf(userPos, LatLng(bus.latitude, bus.longitude)),
                        color = Color.Blue,
                        width = 8f
                    )
                }
            }
        }

        val busDetails by remember { derivedStateOf { busModel.busesInfo } }

        selectedBus?.let { bus ->
            val info = busDetails.firstOrNull()
            BusInfoCard(
                busNumber = info?.bus_number ?: bus.busNumber,
                routeInfo = info?.route_info ?: "N/A",
                driverName = info?.driver_name ?: bus.driverName,
                status = info?.status ?: "Unknown",
                model = info?.model ?: "Unknown",
                distance = bus.distanceKm,
                eta = bus.etaMinutes,
                onDismiss = { selectedBus = null }
            )
        }
    }
}

@Composable
fun bitmapDescriptorFromVector(
    context: Context,
    vectorResId: Int,
    width: Int,
    height: Int
): BitmapDescriptor? {
    val vectorDrawable = getDrawable(context, vectorResId) ?: return null
    vectorDrawable.setBounds(0, 0, width, height)
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}
@Composable
fun BusInfoCard(
    busNumber: String,
    routeInfo: String,
    driverName: String,
    status: String,
    model: String,
    distance: Double,
    eta: Int,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.85f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ){
            Text("🚌 Bus Number: $busNumber", style = MaterialTheme.typography.titleMedium)
            Text("👨‍✈️ Driver: $driverName", style = MaterialTheme.typography.bodyMedium)
            Text("🗺️ Route: $routeInfo", style = MaterialTheme.typography.bodyMedium)
            Text("📶 Status: $status", style = MaterialTheme.typography.bodyMedium)
            Text("🚐 Model: $model", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("📍 Distance: %.2f km".format(distance), style = MaterialTheme.typography.bodySmall)
            Text("⏱️ ETA: $eta minutes", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onDismiss) {
                Text("❌ Close")
            }

        }
    }

}
