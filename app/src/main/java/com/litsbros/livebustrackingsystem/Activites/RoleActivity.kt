package com.litsbros.livebustrackingsystem.Activites

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.litsbros.livebustrackingsystem.Activites.ui.theme.LiveBusTrackingSystemTheme
import com.litsbros.livebustrackingsystem.R
import com.litsbros.livebustrackingsystem.admin.activites.AdminAuthActivity
import com.litsbros.livebustrackingsystem.admin.activites.AdminHomeScreenActivity
import com.litsbros.livebustrackingsystem.driver.DriverAuthActivity
import com.litsbros.livebustrackingsystem.driver.DriverHomeScreenActivity
import com.litsbros.livebustrackingsystem.user.UserAuthActivity

class RoleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LiveBusTrackingSystemTheme {
                RoleSelectionScreen(
                    onUserClick = {
                        startActivity(Intent(this@RoleActivity, UserAuthActivity::class.java))
                    },
                    onDriverClick = {
                        startActivity(Intent(this@RoleActivity, DriverAuthActivity::class.java))
                    },
                    onAdminClick = {
                        startActivity(Intent(this@RoleActivity , AdminAuthActivity::class.java))
                    }
                )
            }
        }
    }
}

@Composable
fun RoleSelectionScreen(
    onUserClick: () -> Unit,
    onDriverClick: () -> Unit,
    onAdminClick: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- USER Button ---
            Button(
                onClick = onUserClick,
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3) // Blue
                ),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.userlogo),
                        contentDescription = "User Logo",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = 12.dp)
                    )
                    Text(
                        text = "USER",
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

        // --- DRIVER Button ---
            Button(
                onClick = onDriverClick,
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50) // Green
                ),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.driver),
                        contentDescription = "User Logo",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = 12.dp)
                    )
                    Text(
                        text = "DRIVER",
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- ADMIN Button ---
            Button(
                onClick = onAdminClick,
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF44336) // Red
                ),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.adminlogo),
                        contentDescription = "Admin Logo",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = 12.dp)
                    )
                    Text(
                        text = "ADMIN",
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
