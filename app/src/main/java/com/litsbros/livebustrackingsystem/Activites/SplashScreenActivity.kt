package com.litsbros.livebustrackingsystem.Activites

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.litsbros.livebustrackingsystem.Activites.ui.theme.LiveBusTrackingSystemTheme
import com.litsbros.livebustrackingsystem.SessionManager.SessionManager
import com.litsbros.livebustrackingsystem.driver.DriverHomeScreenActivity
import kotlinx.coroutines.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.litsbros.livebustrackingsystem.R
import com.litsbros.livebustrackingsystem.admin.activites.AdminHomeScreenActivity
import com.litsbros.livebustrackingsystem.user.UserHomeScreenActivity

class SplashScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LiveBusTrackingSystemTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SplashScreen(
                        onSplashFinished = {
                            val sessionManager = SessionManager(this@SplashScreenActivity)
                            val intent = if (sessionManager.isDriverLoggedIn()) {
                                Intent(this@SplashScreenActivity, DriverHomeScreenActivity::class.java)
                            } else if(sessionManager.isUserLoggedIn()){
                                Intent(this@SplashScreenActivity, UserHomeScreenActivity::class.java)
                            }else if(sessionManager.isAdminLoggedIn()){
                                Intent(this@SplashScreenActivity , AdminHomeScreenActivity::class.java)
                            }else {
                                Intent(this@SplashScreenActivity, RoleActivity::class.java)
                            }
                            startActivity(intent)
                            finish()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(3000L) // Show splash for 3 seconds
        onSplashFinished()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo_vertical),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(300.dp)
                .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(20.dp))
        )
    }
}
