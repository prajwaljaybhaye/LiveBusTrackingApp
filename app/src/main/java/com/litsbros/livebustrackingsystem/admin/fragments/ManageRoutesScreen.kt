package com.litsbros.livebustrackingsystem.admin.fragments

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import com.litsbros.livebustrackingsystem.backand.ViewModel.RouteViewModel.RouteViewModel
import com.litsbros.livebustrackingsystem.backand.model.RouteModel

private val BlueGradient = listOf(Color(0xFF1976D2), Color(0xFF42A5F5))
private val PrimaryBlue = Color(0xFF1976D2)
private val LightBackground = Color(0xFFF5F6FA)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ManageRoutesScreen(
    modifier: Modifier = Modifier,
    onAddRouteClick: () -> Unit = {},
    viewModel: RouteViewModel = viewModel()
) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var isRefreshing by remember { mutableStateOf(false) }

    val filteredRoutes by remember(searchText, viewModel.routes) {
        derivedStateOf {
            viewModel.routes.filter { route ->
                route.route_name.isNotEmpty() &&
                        route.route_name.contains(searchText.text, ignoreCase = true)
            }
        }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            viewModel.fetchAllRoutes()
        },
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
    ) {
        Column(Modifier.fillMaxSize()) {
            // 🔹 Header
            RouteHeaderSection(
                searchText = searchText,
                onAddRouteClick = onAddRouteClick,
                onSearchChange = { searchText = it },
                onReloadClick = {
                    isRefreshing = true
                    viewModel.fetchAllRoutes()
                }
            )

            // 🔹 Summary section (Fixed crash here)
            RouteSummarySection(
                activeCount = viewModel.routes.count { it.status == "Active" },
                totalStops = viewModel.routes.sumOf { route ->
                    // Replace invalid .toInt() call — use safe conversion
                    val time = route.arrival_time_stop2
                    if (time.contains(":")) {
                        // Convert "HH:mm:ss" → total minutes
                        val parts = time.split(":")
                        val hours = parts.getOrNull(0)?.toIntOrNull() ?: 0
                        val minutes = parts.getOrNull(1)?.toIntOrNull() ?: 0
                        hours * 60 + minutes
                    } else {
                        time.toIntOrNull() ?: 0
                    }
                }
            )

            // 🔹 Loading or list
            if (viewModel.routes.isEmpty() && !isRefreshing) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryBlue)
                }
            } else {
                RouteList(filteredRoutes)
            }
        }

        // Stop refreshing when data updates
        LaunchedEffect(viewModel.routes) {
            isRefreshing = false
        }
    }
}

@Composable
private fun RouteHeaderSection(
    searchText: TextFieldValue,
    onAddRouteClick: () -> Unit,
    onSearchChange: (TextFieldValue) -> Unit,
    onReloadClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(BlueGradient))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Manage Routes", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = onReloadClick) {
                Icon(Icons.Default.Refresh, contentDescription = "Reload", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = searchText,
            onValueChange = onSearchChange,
            placeholder = { Text("Search routes...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onAddRouteClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3949AB)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add New Route", color = Color.White, fontSize = 16.sp)
        }
    }
}

@Composable
private fun RouteSummarySection(activeCount: Int, totalStops: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        RouteSummaryCard("Active Routes", activeCount.toString(), Color(0xFFE3F2FD))
        RouteSummaryCard("Total Duration (min)", totalStops.toString(), Color(0xFFE8F5E9))
    }
}

@Composable
fun RouteSummaryCard(title: String, count: String, bgColor: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, fontSize = 14.sp, color = Color.DarkGray)
            Text(count, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun RouteList(routes: List<RouteModel>) {
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(routes) { route ->
            ExpandableRouteCard(route)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExpandableRouteCard(route: RouteModel) {
    var expanded by remember { mutableStateOf(false) }

    val isActive = route.status.equals("Active", ignoreCase = true)
    val statusColor = if (isActive) Color(0xFF43A047) else Color(0xFFBDBDBD)
    val statusIcon = if (isActive) Icons.Default.CheckCircle else Icons.Default.Cancel

    // ✅ Safe color parsing
    val safeColor = try {
        Color(android.graphics.Color.parseColor(route.route_color.ifEmpty { "#1976D2" }))
    } catch (_: Exception) {
        Color(0xFF1976D2)
    }

    // ✅ Safe distance parsing
    val distance = route.distance_km.toDoubleOrNull() ?: 0.0

    // ✅ Handle duration (e.g. "45" or "08:35:00")
    val duration = route.duration_min
    val durationDisplay = if (duration.contains(":")) {
        val parts = duration.split(":")
        val hours = parts.getOrNull(0)?.toIntOrNull() ?: 0
        val minutes = parts.getOrNull(1)?.toIntOrNull() ?: 0
        "${hours}h ${minutes}m"
    } else {
        "${duration.toIntOrNull() ?: 0} min"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // 🔹 Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(safeColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        route.route_name.firstOrNull()?.toString() ?: "?",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(route.route_name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(
                        "$distance km · $durationDisplay",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(statusColor)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(
                        statusIcon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = route.status,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // 🔹 Expandable Details
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Divider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Description: ${route.description}", fontSize = 13.sp, color = Color.DarkGray)
                    Text("Start: ${route.start_location}", fontSize = 13.sp, color = Color.DarkGray)
                    Text("End: ${route.end_location}", fontSize = 13.sp, color = Color.DarkGray)
                    Text("Stops: ${route.stop1_name} → ${route.stop2_name}", fontSize = 13.sp, color = Color.DarkGray)
                    Text("Arrival Time (Stop 2): ${route.arrival_time_stop2}", fontSize = 13.sp, color = Color.DarkGray)
                    Text("Created At: ${route.created_at}", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}
