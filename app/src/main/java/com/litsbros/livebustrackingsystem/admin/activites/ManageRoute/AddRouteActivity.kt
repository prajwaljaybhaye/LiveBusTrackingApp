package com.litsbros.livebustrackingsystem.admin.activites.ManageRoute

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import com.litsbros.livebustrackingsystem.admin.activites.ManageRoute.ui.theme.LiveBusTrackingSystemTheme
import com.litsbros.livebustrackingsystem.backand.ViewModel.RouteViewModel.RouteViewModel
import com.litsbros.livebustrackingsystem.backand.model.RouteModel

class AddRouteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LiveBusTrackingSystemTheme {
                AddRouteScreen(onBack = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRouteScreen(modifier: Modifier = Modifier, onBack: () -> Unit) {
    val viewModel: RouteViewModel = viewModel()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope() // ✅ Use this for snackbar
    var isLoading by remember { mutableStateOf(false) }

    // Form fields
    var routeName by remember { mutableStateOf(TextFieldValue("")) }
    var routeCode by remember { mutableStateOf(TextFieldValue("")) }
    var routeColor by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var startLocation by remember { mutableStateOf(TextFieldValue("")) }
    var endLocation by remember { mutableStateOf(TextFieldValue("")) }
    var distance by remember { mutableStateOf(TextFieldValue("")) }
    var duration by remember { mutableStateOf(TextFieldValue("")) }
    var stop1 by remember { mutableStateOf(TextFieldValue("")) }
    var stop2 by remember { mutableStateOf(TextFieldValue("")) }
    var arrivalTime2 by remember { mutableStateOf(TextFieldValue("")) }
    var status by remember { mutableStateOf("Active") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Route", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(Color(0xFFF6F7FB))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Basic Information
            SectionCard("Basic Information", Color(0xFF2196F3)) {
                RouteTextField("Route Name", routeName) { routeName = it }
                RouteTextField("Route Code", routeCode) { routeCode = it }
                RouteTextField("Route Color", routeColor) { routeColor = it }
                RouteTextField("Description", description, Modifier.fillMaxWidth(), maxLines = 3) { description = it }
            }

            // Location & Distance
            SectionCard("Location & Distance", Color(0xFF43A047)) {
                RouteTextField("Start Location", startLocation) { startLocation = it }
                RouteTextField("End Location", endLocation) { endLocation = it }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    RouteTextField("Distance (km)", distance, Modifier.weight(1f)) { distance = it }
                    RouteTextField("Duration (min)", duration, Modifier.weight(1f)) { duration = it }
                }
            }

            // Stops
            SectionCard("Route Stops", Color(0xFFFF9800)) {
                RouteTextField("Stop 1 Name", stop1) { stop1 = it }
                RouteTextField("Stop 2 Name", stop2) { stop2 = it }
                RouteTextField("Arrival Time (Stop 2)", arrivalTime2) { arrivalTime2 = it }
            }

            // Status
            SectionCard("Status", Color(0xFF7E57C2)) {
                RouteTextField("Route Status", TextFieldValue(status)) { status = it.text }
            }

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = {
                        // Validation
                        if (routeName.text.isBlank() || startLocation.text.isBlank()) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Please fill all required fields")
                            }
                            return@Button
                        }

                        val newRoute = RouteModel(
                            route_id = "",
                            route_code = routeCode.text,
                            route_name = routeName.text,
                            route_color = routeColor.text.ifBlank { "#1976D2" },
                            description = description.text,
                            start_location = startLocation.text,
                            end_location = endLocation.text,
                            distance_km = distance.text,
                            duration_min = duration.text,
                            stop1_name = stop1.text,
                            stop2_name = stop2.text,
                            arrival_time_stop2 = arrivalTime2.text,
                            status = status,
                            created_at = ""
                        )

                        isLoading = true
                        viewModel.addRoute(
                            route = newRoute,
                            onSuccess = {
                                isLoading = false
                                onBack()
                            },
                            onError = { msg ->
                                isLoading = false
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Failed: $msg")
                                }
                            }
                        )
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Add Route")
                    }
                }
            }
        }
    }
}

@Composable
fun SectionCard(
    title: String,
    color: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(color)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun RouteTextField(
    label: String,
    value: TextFieldValue,
    modifier: Modifier = Modifier.fillMaxWidth(),
    maxLines: Int = 1,
    onValueChange: (TextFieldValue) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        maxLines = maxLines,
        singleLine = maxLines == 1
    )
}
