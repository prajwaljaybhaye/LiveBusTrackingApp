package com.litsbros.livebustrackingsystem.user.fragments

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class NotificationItem(
    val title: String,
    val message: String,
    val timestamp: String,
    val isUnread: Boolean
)

@Composable
fun NotificationsScreen(modifier: Modifier = Modifier) {
    val notifications = remember {
        listOf(
            NotificationItem("Bus Running Late", "Your bus is running 10 minutes late due to heavy traffic on Main Road.", "5 mins ago", true),
            NotificationItem("Route Change Notice", "Starting tomorrow, the bus will take a new route via Highway Road.", "1 hour ago", true),
            NotificationItem("Bus Approaching", "Your bus is 2 km away and will arrive in approximately 8 minutes.", "1 hour ago", false),
            NotificationItem("Weather Alert", "Heavy rain expected. Bus might experience delays. Please plan accordingly.", "2 hours ago", false),
            NotificationItem("Holiday Notice", "Bus service will be unavailable on Sunday due to Independence Day celebrations.", "2 hours ago", false),
            NotificationItem("Driver Change", "Bus driver Rajesh Kumar will be replaced by Amit Singh for this week.", "2 hours ago", false)
        )
    }

    var selectedTab by remember { mutableStateOf("All") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5))
            .padding(16.dp)
    ) {
        Text("🔔 Notifications", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        TabRow(
            selectedTabIndex = if (selectedTab == "All") 0 else 1,
            containerColor = Color.White
        ) {
            Tab(selected = selectedTab == "All", onClick = { selectedTab = "All" }) {
                Text("All (${notifications.size})", modifier = Modifier.padding(12.dp))
            }
            Tab(selected = selectedTab == "Unread", onClick = { selectedTab = "Unread" }) {
                val unreadCount = notifications.count { it.isUnread }
                Text("Unread ($unreadCount)", modifier = Modifier.padding(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        val filtered = if (selectedTab == "All") notifications else notifications.filter { it.isUnread }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            itemsIndexed(filtered) { index, item ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(tween(300)) + slideInVertically(initialOffsetY = { it * (index + 1) }),
                    exit = fadeOut()
                ) {
                    NotificationCard(item)
                }
            }
        }
    }
}

@Composable
fun NotificationCard(item: NotificationItem) {
    val bgColor = if (item.isUnread) Color(0xFFE3F2FD) else Color.White

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(item.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(item.message, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(item.timestamp, fontSize = 12.sp, color = Color.Gray)
        }
    }
}
