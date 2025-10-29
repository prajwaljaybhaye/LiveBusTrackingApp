package com.litsbros.livebustrackingsystem.driver.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.litsbros.livebustrackingsystem.backand.model.DriverModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    driver: DriverModel?,
    onLogout: () -> Unit
) {
    if (driver == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProfileHeader(driver)
        PersonalInfo(driver)
        ContactInfo(driver)
        LicenseInfo(driver)
        WorkDetails(driver)
        EmergencyContact(driver)
        SystemDetails(driver)
        MonthlyStats()
        LogoutSection(onLogout)
    }
}

@Composable
fun ProfileHeader(driver: DriverModel) {
    val initials = "${driver.first_name.firstOrNull() ?: ""}${driver.last_name?.firstOrNull() ?: ""}"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color(0xFF1976D2)),
            contentAlignment = Alignment.Center
        ) {
            Text(initials, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text("${driver.first_name} ${driver.last_name.orEmpty()}", style = MaterialTheme.typography.titleLarge)
            Text("Driver ID: ${driver.driver_user_id}", fontSize = 14.sp, color = Color.Gray)
            Text("Internal ID: ${driver.driver_id}", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Text("⭐ 4.8 • 1247 Trips", fontSize = 14.sp, color = Color(0xFF4CAF50))
        }
    }
}

@Composable
fun PersonalInfo(driver: DriverModel) {
    InfoCard("👤 Personal Information") {
        Text("First Name: ${driver.first_name}")
        Text("Last Name: ${driver.last_name ?: "N/A"}")
        Text("DOB: ${driver.dob ?: "N/A"}")
        Text("Blood Group: ${driver.blood_group ?: "N/A"}")
        Text("Driving Experience: ${driver.driving_experience ?: 0} years")
    }
}

@Composable
fun ContactInfo(driver: DriverModel) {
    InfoCard("📞 Contact Information") {
        Text("Phone: ${driver.phone ?: "N/A"}")
        Text("Alt Phone: ${driver.alt_phone ?: "N/A"}")
        Text("Email: ${driver.email ?: "N/A"}")
        Text("Contact Email: ${driver.contact_email ?: "N/A"}")
        Text("Address: ${driver.address ?: "N/A"}")
        Text("City: ${driver.city ?: "N/A"}")
        Text("State: ${driver.state ?: "N/A"}")
        Text("ZIP Code: ${driver.zip_code ?: "N/A"}")
    }
}

@Composable
fun LicenseInfo(driver: DriverModel) {
    InfoCard("🪪 License Details") {
        Text("License Number: ${driver.license_number ?: "N/A"}")
        Text("License Type: ${driver.license_type ?: "N/A"}")
        Text("License Expiry: ${driver.license_expiry ?: "N/A"}")
    }
}

@Composable
fun WorkDetails(driver: DriverModel) {
    InfoCard("🚌 Work Details") {
        Text("Vehicle Number: ${driver.vehicle_number ?: "N/A"}")
        Text("Vehicle Type: ${driver.vehicle_type ?: "N/A"}")
        Text("Assigned Route: [Add route info if available]")
    }
}

@Composable
fun EmergencyContact(driver: DriverModel) {
    InfoCard("🚨 Emergency Contact") {
        Text("Name: ${driver.emergency_name ?: "N/A"}")
        Text("Number: ${driver.emergency_number ?: "N/A"}")
        Text("Relationship: ${driver.emergency_relationship ?: "N/A"}")
    }
}

@Composable
fun SystemDetails(driver: DriverModel) {
    InfoCard("⚙️ System Info") {
        Text("Password: ${driver.password ?: "N/A"}")
        Text("Created At: ${driver.created_at ?: "N/A"}")
    }
}

@Composable
fun MonthlyStats() {
    InfoCard("📊 This Month") {
        Text("Trips Completed: 42")
        Text("On-Time Rate: 98%")
        Text("Average Rating: 4.8")
    }
}

@Composable
fun LogoutSection(onLogout: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Version 2.1.0 (Silver App)", fontSize = 12.sp, color = Color.Gray)
        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout", color = Color.White)
        }
    }
}

@Composable
fun InfoCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}
