package com.litsbros.livebustrackingsystem.user.fragments

import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.litsbros.livebustrackingsystem.backand.ViewModel.BusViewModel
import com.litsbros.livebustrackingsystem.user.components.BusDetailsComponents.BusDetailsActivity
import com.litsbros.livebustrackingsystem.backand.model.BusModel
import com.litsbros.livebustrackingsystem.user.model.BusInfo


@Composable
fun BusDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: BusViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val busList by remember { derivedStateOf { viewModel.busesInfo } }

    LaunchedEffect(Unit) {
        viewModel.fetchAllBusInfos()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        Text("🚌 Available Buses", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        if (busList.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                itemsIndexed(busList) { index, bus ->
                    AnimatedVisibility(
                        visible = true,
                        enter = slideInVertically(
                            initialOffsetY = { it * (index + 1) },
                            animationSpec = tween(durationMillis = 300)
                        ) + fadeIn(animationSpec = tween(300))
                    ) {
                        BusCard(bus)
                    }
                }
            }
        }
    }
}

@Composable
fun BusCard(bus: BusInfo) {
    val context = LocalContext.current
    val statusColor = when (bus.status) {
        "Active" -> Color(0xFF4CAF50)
        "Maintenance" -> Color(0xFFF44336)
        else -> Color.Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(context, BusDetailsActivity::class.java).apply {
                    putExtra("busNumber", bus.bus_number)
                    putExtra("route", bus.route_info)
                    putExtra("driver", bus.driver_name)
                    putExtra("status", bus.status)
                    putExtra("type", bus.model)
                }
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DirectionsBus,
                contentDescription = "Bus Icon",
                tint = statusColor,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(bus.bus_number, fontWeight = FontWeight.Bold)
                Text(bus.route_info, fontSize = 14.sp)
                Text("Driver: ${bus.driver_name}", fontSize = 14.sp)
                Text("Type: ${bus.model}", fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                bus.status,
                color = statusColor,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

