package com.litsbros.livebustrackingsystem.user.components.TrackBusComponents

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BusInfoCard(
    busNumber: String,
    distance: Double,
    eta: Int,
    driverName: String,
    experience: String,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize() // occupy full screen
            .padding(top = 16.dp), // optional padding from top
        contentAlignment = Alignment.TopCenter // align card at the top
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("🚌 Bus Number: $busNumber", style = MaterialTheme.typography.titleMedium)
                Text("📍 Status: On Route", style = MaterialTheme.typography.bodyMedium)
                Text(
                    "📏 Distance: ${String.format("%.1f", distance)} km",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text("⏱ ETA: $eta min (approx.)", style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "👤 $driverName",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "🧑‍✈️ Driver • $experience years exp.",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { onDismiss() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close")
                }
            }
        }
    }
}
