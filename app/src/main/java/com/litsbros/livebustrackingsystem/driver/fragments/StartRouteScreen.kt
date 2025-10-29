package com.litsbros.livebustrackingsystem.driver.fragments

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.litsbros.livebustrackingsystem.backand.model.DriverModel
import kotlinx.coroutines.delay

@Composable
fun StartRouteScreen(modifier: Modifier = Modifier, driver: DriverModel?) {
    var isRouteActive by remember { mutableStateOf(false) }
    var secondsElapsed by remember { mutableIntStateOf(0) }
    var onboardCount by remember { mutableStateOf(0) }
    var selectedTab by remember { mutableStateOf("Route") }

    LaunchedEffect(isRouteActive) {
        if (isRouteActive) {
            while (true) {
                delay(1000)
                secondsElapsed++
            }
        } else {
            secondsElapsed = 0
        }
    }

    val formattedDuration = formatDuration(secondsElapsed)

    Scaffold(
        bottomBar = {
            BottomNavigationBar(selectedTab = selectedTab, onTabSelected = { selectedTab = it })
        },
        containerColor = Color(0xFFF0F2F5)
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RouteHeader()

            if (isRouteActive) {
                ActiveRouteStatus(duration = formattedDuration, onboardCount = onboardCount)
            } else {
                InactiveRouteStatus()
            }

            RouteDetails()
            PreRouteChecklist()

            Button(
                onClick = { isRouteActive = !isRouteActive },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRouteActive) Color(0xFFF44336) else Color(0xFF4CAF50)
                )
            ) {
                Text(if (isRouteActive) "Stop Route" else "Start Route")
            }
        }
    }
}

@Composable
fun RouteHeader() {
    Text(
        text = "🧭 Route Control – Manage your bus route",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF333333)
    )
}

@Composable
fun BottomNavigationBar(selectedTab: String, onTabSelected: (String) -> Unit) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = selectedTab == "Route",
            onClick = { onTabSelected("Route") },
            icon = { Icon(Icons.Default.Route, contentDescription = "Route") },
            label = { Text("Start Route") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF4CAF50),
                selectedTextColor = Color(0xFF4CAF50)
            )
        )
        NavigationBarItem(
            selected = selectedTab == "Map",
            onClick = { onTabSelected("Map") },
            icon = { Icon(Icons.Default.Map, contentDescription = "Live Map") },
            label = { Text("Live Map") }
        )
        NavigationBarItem(
            selected = selectedTab == "Students",
            onClick = { onTabSelected("Students") },
            icon = { Icon(Icons.Default.CheckCircle, contentDescription = "Students") },
            label = { Text("Students") }
        )
        NavigationBarItem(
            selected = selectedTab == "Profile",
            onClick = { onTabSelected("Profile") },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}

@Composable
fun InactiveRouteStatus() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDECEA))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Route Status: INACTIVE", color = Color(0xFF721C24), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Start your route to begin tracking and location sharing", fontSize = 14.sp)
        }
    }
}

@Composable
fun ActiveRouteStatus(duration: String, onboardCount: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE6F4EA))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Active", tint = Color(0xFF155724))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Route Status: ACTIVE", color = Color(0xFF155724), fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("⏱️ Duration: $duration", fontSize = 14.sp, color = Color(0xFF155724))
            Text("📍 Location Sharing: Active", fontSize = 14.sp, color = Color(0xFF155724))
        }
    }
}

@Composable
fun RouteDetails() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Today's Route", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("🚌 Route Number: 42 - Morning Shift")
            Text("👨‍🎓 Expected Students: 45")
            Text("⏰ Scheduled Time: 7:00 AM - 9:00 AM")
        }
    }
}

@Composable
fun PreRouteChecklist() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("✅ Pre-Route Checklist", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            ChecklistItem("Vehicle inspection completed")
            ChecklistItem("GPS tracking enabled")
            ChecklistItem("Student list verified")
        }
    }
}

@Composable
fun ChecklistItem(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.CheckCircle, contentDescription = "Checklist", tint = Color(0xFF4CAF50))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}

fun formatDuration(seconds: Int): String {
    val hrs = seconds / 3600
    val mins = (seconds % 3600) / 60
    val secs = seconds % 60
    return "%02d:%02d:%02d".format(hrs, mins, secs)
}
