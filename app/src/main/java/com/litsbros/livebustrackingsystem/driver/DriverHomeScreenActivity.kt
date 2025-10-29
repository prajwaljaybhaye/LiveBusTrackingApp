package com.litsbros.livebustrackingsystem.driver

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotStarted
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.litsbros.livebustrackingsystem.Activites.RoleActivity
import com.litsbros.livebustrackingsystem.Activites.ui.theme.LiveBusTrackingSystemTheme
import com.litsbros.livebustrackingsystem.backand.ViewModel.DriverViewModel.DriverViewModel
import com.litsbros.livebustrackingsystem.driver.fragments.LiveLocationScreen
import com.litsbros.livebustrackingsystem.driver.fragments.ProfileScreen
import com.litsbros.livebustrackingsystem.driver.fragments.StartRouteScreen

class DriverHomeScreenActivity : ComponentActivity() {
    private val viewModel: DriverViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LiveBusTrackingSystemTheme {
                DriverDashboard(viewModel = viewModel)
            }
        }
    }
}

data class DriverBottomNavItem(val label: String, val icon: ImageVector)

@Composable
fun DriverDashboard(viewModel: DriverViewModel) {
    var selectedItem by remember { mutableStateOf(0) }
    val driver = viewModel.loggedInDriver
    val context = LocalContext.current

    val items = listOf(
        DriverBottomNavItem("Start Route", Icons.Filled.NotStarted),
        DriverBottomNavItem("Live Location", Icons.Filled.LocationOn),
        DriverBottomNavItem("Profile", Icons.Filled.AccountCircle)
    )

    // 🔁 Redirect if session is missing
    LaunchedEffect(driver) {
        if (driver == null) {
            val intent = Intent(context, RoleActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

    if (driver == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

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
            0 -> StartRouteScreen(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                driver = driver
            )
            1 -> LiveLocationScreen(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                driver = driver
            )
            2 -> ProfileScreen(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                driver = driver,
                onLogout = {
                    viewModel.logout()
                    val intent = Intent(context, RoleActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                }
            )
        }
    }
}
