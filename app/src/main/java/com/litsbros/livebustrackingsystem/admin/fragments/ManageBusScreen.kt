package com.litsbros.livebustrackingsystem.admin.fragments

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.litsbros.livebustrackingsystem.admin.activities.ManageBus.AddBusActivity
import com.litsbros.livebustrackingsystem.backand.ViewModel.BusViewModel


private val BlueGradient = listOf(Color(0xFF1976D2), Color(0xFF42A5F5))
private val PrimaryBlue = Color(0xFF1976D2)
private val LightBackground = Color(0xFFF5F6FA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageBusScreen(
    viewModel: BusViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val buses = viewModel.filteredBuses
    val searchQuery = viewModel.searchQuery
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchBuses()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Buses", fontWeight = FontWeight.Bold, color = Color.White) },
                actions = {
                    IconButton(onClick = {
                        isRefreshing = true
                        viewModel.fetchBuses()
                        isRefreshing = false
                    }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Reload",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                modifier = Modifier.background(
                    Brush.verticalGradient(listOf(Color(0xFF1976D2), Color(0xFF42A5F5)))
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val intent = Intent(context, AddBusActivity::class.java)
                    context.startActivity(intent)
                },
                containerColor = Color(0xFF1976D2)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Bus", tint = Color.White)
            }
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 60.dp)
                .background(Color(0xFFF5F6FA))
        ) {
            // 🔍 Search + Add Button Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF1976D2),
                                Color(0xFF42A5F5)
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = viewModel::updateSearch,
                    placeholder = { Text("Search buses...") },
                    maxLines = 1,
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
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
                    onClick = {
                        val intent = Intent(context, AddBusActivity::class.java)
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add New Bus", color = Color.White, fontSize = 16.sp)
                }
            }

            // 📊 Summary + List Section
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = {
                    isRefreshing = true
                    viewModel.fetchBuses()
                    isRefreshing = false
                }
            ) {
                if (buses.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF1976D2))
                    }
                } else {
                    val total = buses.size
                    val active = buses.count { it.status == "Active" }
                    val maintenance = buses.count { it.status == "Maintenance" }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                            .padding(top = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                SummaryCard(
                                    "Total",
                                    total,
                                    Color(0xFFE3F2FD),
                                    Color(0xFF1976D2),
                                    Modifier.weight(1f)
                                )
                                SummaryCard(
                                    "Active",
                                    active,
                                    Color(0xFFC8E6C9),
                                    Color(0xFF388E3C),
                                    Modifier.weight(1f)
                                )
                                SummaryCard(
                                    "Maintenance",
                                    maintenance,
                                    Color(0xFFFFF3E0),
                                    Color(0xFFF57C00),
                                    Modifier.weight(1f)
                                )
                            }
                        }

                        items(buses) { bus ->
                            BusCard(
                                id = bus.bus_number,
                                name = "${bus.manufacturer} ${bus.model}",
                                number = bus.registration_number,
                                route = "Route ID: ${bus.route_id}",
                                driver = "Driver ID: ${bus.driver_id}",
                                status = bus.status,
                                seats = bus.seating_capacity ?: 0
                            )
                        }

                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }
}



// ───────── UI Components ─────────
@Composable
fun SummaryCard(
    title: String,
    count: Int,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(bgColor, RoundedCornerShape(12.dp))
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title, color = Color.Gray, fontSize = 14.sp)
        Text(
            text = count.toString(),
            color = textColor,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BusCard(
    id: String,
    name: String,
    number: String,
    route: String,
    driver: String,
    status: String,
    seats: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.DirectionsBus,
                        contentDescription = null,
                        tint = Color(0xFF1976D2)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(id, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(name, fontSize = 13.sp, color = Color.Gray)
                        Text(number, fontSize = 12.sp, color = Color.Gray)
                    }
                }
                StatusChip(status)
            }

            Spacer(modifier = Modifier.height(8.dp))
            InfoBox(Icons.Default.LocationOn, "Assigned Route", route, Color(0xFFE3F2FD))
            InfoBox(Icons.Default.Person, "Driver", driver, Color(0xFFC8E6C9))

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("$seats Seats", color = Color.Gray, fontSize = 12.sp)
                TextButton(onClick = { /* Manage */ }) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = null,
                        tint = Color(0xFF3F51B5)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Manage", color = Color(0xFF3F51B5), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun InfoBox(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    bgColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Color.Gray)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(label, fontSize = 12.sp, color = Color.Gray)
                Text(value, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val color = when (status) {
        "Active" -> Color(0xFF4CAF50)
        "Maintenance" -> Color(0xFFFF9800)
        else -> Color.Gray
    }
    Box(
        modifier = Modifier
            .background(color, CircleShape)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(status, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}
