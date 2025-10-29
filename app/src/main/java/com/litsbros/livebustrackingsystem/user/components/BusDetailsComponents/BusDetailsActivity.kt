package com.litsbros.livebustrackingsystem.user.components.BusDetailsComponents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.litsbros.livebustrackingsystem.user.components.BusDetailsComponents.ui.theme.LiveBusTrackingSystemTheme

class BusDetailsActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val busNumber = intent.getStringExtra("busNumber") ?: ""
        val route = intent.getStringExtra("route") ?: ""
        val driver = intent.getStringExtra("driver") ?: ""
        val status = intent.getStringExtra("status") ?: ""
        val type = intent.getStringExtra("type") ?: ""

        setContent {
            LiveBusTrackingSystemTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Bus Details") },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.White,
                                titleContentColor = Color.Black,
                                navigationIconContentColor = Color.Black
                            )
                        )
                    },
                    containerColor = Color(0xFFF0F2F5)
                ) { innerPadding ->
                    BusDetailsScreen(
                        busNumber = busNumber,
                        route = route,
                        driver = driver,
                        status = status,
                        type = type,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun BusDetailsScreen(
    busNumber: String,
    route: String,
    driver: String,
    status: String,
    type: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AnimatedSection {
            InfoCard("🚌 Bus Identification") {
                Text("Bus Number: $busNumber")
                Text("Status: $status")
                Text("Route: $route")
            }
        }

        AnimatedSection {
            InfoCard("🚍 Bus Details") {
                Text("Bus Type: $type")
                Text("Capacity: 45 seats")
                Text("Registered: 2023-05-15")
                Text("Status: Verified", color = Color(0xFF4CAF50))
            }
        }

        AnimatedSection {
            InfoCard("👨‍✈️ Driver Information") {
                Text("Name: $driver")
                Text("Phone: +91 98765 43210")
                Text("Email: rajesh.kumar@busservice.com")
                Text("License No.: DL-AB123456")
            }
        }

        AnimatedSection {
            InfoCard("🗺️ Route Details") {
                Text("Green Valley Society – 07:15 AM")
                Text("Central Park – 07:25 AM")
                Text("Main Market – 07:35 AM")
                Text("City Hospital – 07:45 AM")
                Text("University Campus – 07:55 AM")
                Text("School Campus – 08:00 AM")
            }
        }

        AnimatedSection {
            InfoCard("🚨 Emergency Contact") {
                Text("In case of emergency, call: 1800-123-4567", color = Color.Red)
            }
        }
    }
}

@Composable
fun AnimatedSection(content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(tween(300)) + slideInVertically(initialOffsetY = { it / 2 }),
        exit = fadeOut()
    ) {
        content()
    }
}

@Composable
fun InfoCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontSize = 18.sp, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}
