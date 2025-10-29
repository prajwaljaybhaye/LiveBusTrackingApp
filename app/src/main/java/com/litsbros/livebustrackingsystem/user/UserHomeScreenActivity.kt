package com.litsbros.livebustrackingsystem.user

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.litsbros.livebustrackingsystem.user.fragments.BusDetailsScreen
import com.litsbros.livebustrackingsystem.user.fragments.NotificationsScreen
import com.litsbros.livebustrackingsystem.user.fragments.TrackBusScreen
import com.litsbros.livebustrackingsystem.user.fragments.UserProfileScreen
import com.litsbros.livebustrackingsystem.user.model.UserBottomNavItem

import com.litsbros.livebustrackingsystem.user.ui.theme.LiveBusTrackingSystemTheme


class UserHomeScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LiveBusTrackingSystemTheme {
                UserDashboard()
            }
        }
    }
}

@Composable
fun UserDashboard() {
    var selectedItem by remember { mutableStateOf(0) }

    val items = listOf(
        UserBottomNavItem("Track Bus", Icons.Filled.Map),
        UserBottomNavItem("Bus Details", Icons.Filled.DirectionsBus),
        UserBottomNavItem("Notifications", Icons.Filled.Notifications),
        UserBottomNavItem("Profile", Icons.Filled.AccountCircle)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        when (selectedItem) {
            0 -> TrackBusScreen(Modifier.fillMaxSize().padding(innerPadding))
            1 -> BusDetailsScreen(Modifier.fillMaxSize().padding(innerPadding))
            2 -> NotificationsScreen(Modifier.fillMaxSize().padding(innerPadding))
            3 -> UserProfileScreen(Modifier.fillMaxSize().padding(innerPadding))
        }
    }
}