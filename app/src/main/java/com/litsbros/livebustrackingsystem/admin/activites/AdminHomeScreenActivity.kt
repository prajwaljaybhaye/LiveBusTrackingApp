package com.litsbros.livebustrackingsystem.admin.activites

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person4
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.litsbros.livebustrackingsystem.admin.activites.ManageDriver.AddDriverActivity
import com.litsbros.livebustrackingsystem.admin.activites.ManageRoute.AddRouteActivity
import com.litsbros.livebustrackingsystem.admin.activities.ManageBus.AddBusActivity
import com.litsbros.livebustrackingsystem.admin.fragments.*
import com.litsbros.livebustrackingsystem.admin.model.AdminBottomNavItem
import com.litsbros.livebustrackingsystem.admin.ui.theme.LiveBusTrackingSystemTheme

class AdminHomeScreenActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LiveBusTrackingSystemTheme {
                // Calculate system-level window size
                val windowSizeClass = calculateWindowSizeClass(this)
                AdminDashboard(windowSizeClass.widthSizeClass)
            }
        }
    }
}

@Composable
fun AdminDashboard(windowWidthSizeClass: WindowWidthSizeClass) {
    var selectedItem by remember { mutableStateOf(0) }
    val context = LocalContext.current

    val items = listOf(
        AdminBottomNavItem("Buses", Icons.Filled.DirectionsBus),
        AdminBottomNavItem("Drivers", Icons.Filled.Person4),
        AdminBottomNavItem("Routes", Icons.Filled.Route),
        AdminBottomNavItem("Profile", Icons.Filled.Person)
    )

    // 🎨 Custom colors
    val bottomBarContainerColor = Color(0xFF1976D2) // Blue
    val selectedIconColor = Color.White
    val unselectedIconColor = Color(0xFFBBDEFB) // Light blue

    when (windowWidthSizeClass) {

        // 📱 Compact / Medium = Bottom Navigation
        WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> {
            Scaffold(
                bottomBar = {
                    NavigationBar(
                        containerColor = bottomBarContainerColor, // 🎨 Bottom bar background
                        tonalElevation = 8.dp
                    ) {
                        items.forEachIndexed { index, item ->
                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        item.icon,
                                        contentDescription = item.label,
                                        tint = if (selectedItem == index)
                                            selectedIconColor else unselectedIconColor
                                    )
                                },
                                label = {
                                    Text(
                                        item.label,
                                        color = if (selectedItem == index)
                                            selectedIconColor else unselectedIconColor
                                    )
                                },
                                selected = selectedItem == index,
                                onClick = { selectedItem = index },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = Color(0xFF1565C0), // darker blue highlight
                                    selectedIconColor = selectedIconColor,
                                    selectedTextColor = selectedIconColor,
                                    unselectedIconColor = unselectedIconColor,
                                    unselectedTextColor = unselectedIconColor
                                )
                            )
                        }
                    }
                }
            ) { innerPadding ->
                MainAdminContent(
                    selectedItem = selectedItem,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    context = context
                )
            }
        }

        // 💻 Expanded = Navigation Rail
        WindowWidthSizeClass.Expanded -> {
            Row(modifier = Modifier.fillMaxSize()) {
                NavigationRail(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(vertical = 16.dp),
                    containerColor = bottomBarContainerColor // 🎨 Same color for consistency
                ) {
                    items.forEachIndexed { index, item ->
                        NavigationRailItem(
                            icon = {
                                Icon(
                                    item.icon,
                                    contentDescription = item.label,
                                    tint = if (selectedItem == index)
                                        selectedIconColor else unselectedIconColor
                                )
                            },
                            label = {
                                Text(
                                    item.label,
                                    color = if (selectedItem == index)
                                        selectedIconColor else unselectedIconColor
                                )
                            },
                            selected = selectedItem == index,
                            onClick = { selectedItem = index },
                            colors = NavigationRailItemDefaults.colors(
                                indicatorColor = Color(0xFF1565C0),
                                selectedIconColor = selectedIconColor,
                                selectedTextColor = selectedIconColor,
                                unselectedIconColor = unselectedIconColor,
                                unselectedTextColor = unselectedIconColor
                            )
                        )
                    }
                }

                Divider(modifier = Modifier.width(1.dp))

                MainAdminContent(
                    selectedItem = selectedItem,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    context = context
                )
            }
        }
    }
}

@Composable
fun MainAdminContent(
    selectedItem: Int,
    modifier: Modifier = Modifier,
    context: android.content.Context
) {
    when (selectedItem) {
        0 -> ManageBusScreen(
            modifier = modifier
        )

        1 -> ManageDriversScreen(
            modifier = modifier,
            onAddDriverClick = {
                val intent = Intent(context, AddDriverActivity::class.java)
                context.startActivity(intent)
            }
        )

        2 -> ManageRoutesScreen(
            modifier = modifier,
            onAddRouteClick = {
                val intent = Intent(context, AddRouteActivity::class.java)
                context.startActivity(intent)
            }
        )

        3 -> AdminProfileScreen(modifier)
    }
}
