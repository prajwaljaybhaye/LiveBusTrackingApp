package com.litsbros.livebustrackingsystem.user.fragments

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.litsbros.livebustrackingsystem.user.components.TrackBusComponents.LiveBusMap

@Composable
fun TrackBusScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("🚌 Track Bus Screen", fontSize = 24.sp)
        LiveBusMap()
    }
}
