package com.litsbros.livebustrackingsystem.admin.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.litsbros.livebustrackingsystem.backand.ViewModel.DriverViewModel.DriverViewModel
import com.litsbros.livebustrackingsystem.backand.model.DriverModel

private val BlueGradient = listOf(Color(0xFF1976D2), Color(0xFF42A5F5))
private val PrimaryBlue = Color(0xFF1976D2)
private val LightBackground = Color(0xFFF5F6FA)

@Composable
fun ManageDriversScreen(
    modifier: Modifier = Modifier,
    onAddDriverClick: () -> Unit,
    viewModel: DriverViewModel = viewModel()
) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val filteredDrivers by remember(
        searchText,
        viewModel.drivers
    ) {
        derivedStateOf {
            viewModel.drivers.filter {
                it.first_name.contains(
                    searchText.text,
                    ignoreCase = true
                ) || it.last_name?.contains(searchText.text, ignoreCase = true) == true

            }
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
    ) {
        HeaderSection(
            searchText = searchText,
            onAddDriverClick = onAddDriverClick,
            onSearchChange = { searchText = it },
            onReloadClick = { viewModel.reloadDrivers() })

        SummarySection(viewModel.drivers.size)
        if (viewModel.drivers.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator(color = PrimaryBlue) }
        } else {
            DriverList(filteredDrivers)
        }
    }
}

@Composable
private fun HeaderSection(
    searchText: TextFieldValue,
    onAddDriverClick: () -> Unit,
    onSearchChange: (TextFieldValue) -> Unit,
    onReloadClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(BlueGradient))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Manage Drivers",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = onReloadClick) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = "Reload",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value =
                    searchText,
                onValueChange = onSearchChange,
                placeholder = { Text("Search drivers...") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick =
                    onAddDriverClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3949AB)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.White
                )

                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )

                Text("Add New Driver", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun SummarySection(totalDrivers: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SummaryCard(
            "Total Drivers",
            totalDrivers.toString(),
            Color(0xFFE3F2FD)
        )

        SummaryCard(
            "Bus Drivers",
            totalDrivers.toString(),
            Color(0xFFE8F5E9)
        )
        SummaryCard("Others", "—", Color(0xFFFFF3E0))
    }
}

@Composable
fun SummaryCard(title: String, count: String, bgColor: Color) {
    Card(
        modifier = Modifier.padding(horizontal = 4.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontSize = 14.sp, color = Color.DarkGray)
            Text(
                text =
                    count, fontSize = 18.sp, fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DriverList(drivers: List<DriverModel>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) { items(drivers) { driver -> DriverItem(driver) } }
}

@Composable
fun DriverItem(driver: DriverModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(PrimaryBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${driver.first_name.firstOrNull() ?: ""}${driver.last_name?.firstOrNull() ?: ""}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {
                    Text(
                        "👤 ${driver.first_name} ${driver.last_name}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        "📧 ${driver.email}",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                    Text("📞 ${driver.phone}", fontSize = 13.sp, color = Color.Gray)
                }
                Box(
                    modifier =
                        Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFFEEEEEE))
                            .padding(
                                horizontal = 12.dp,
                                vertical = 4.dp
                            )
                ) {
                    Text(
                        text = "Type:${driver.vehicle_type}",
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            InfoRow(
                "📍",
                "${driver.city}, ${driver.state} - ${driver.zip_code}"
            )

            InfoRow("🪪", "License: ${driver.license_number} (${driver.license_type})")

            InfoRow("📅", "Expiry: ${driver.license_expiry}")

            InfoRow("🚌", "Vehicle: ${driver.vehicle_number}")

            InfoRow("💼", "Experience: ${driver.driving_experience} years")

            InfoRow("🚨", "Emergency: ${driver.emergency_name} (${driver.emergency_relationship})")

            InfoRow("📞", driver.emergency_number ?: "Not provided")


            InfoRow("🆔", "Username: ${driver.driver_user_id}")

            InfoRow("🔑", "Password: ${driver.password}")
        }
    }
}
@Composable
fun InfoRow(icon: String, info: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, fontSize = 18.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(info, fontSize = 14.sp, color = Color.DarkGray)
    }
}


