package com.litsbros.livebustrackingsystem.user

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.litsbros.livebustrackingsystem.Activites.ui.theme.LiveBusTrackingSystemTheme
import com.litsbros.livebustrackingsystem.R

class UserAuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LiveBusTrackingSystemTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF4B3EFF) // Purple background
                ) {
                    WelcomeScreen(
                        onLoginClick = {
                            startActivity(
                                Intent(
                                    this@UserAuthActivity,
                                    UserLoginActivity::class.java
                                )
                            )
                        },
                        onSignUpClick = {
                            startActivity(
                                Intent(
                                    this@UserAuthActivity,
                                    UesrRegistrationActivity::class.java
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun WelcomeScreen(
    onLoginClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // White card container
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                // Top Illustration Image
                Image(
                    painter = painterResource(id = R.drawable.auth_screen_logo), // 🖼️ Add your drawable
                    contentDescription = "Welcome Image",
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                )

                // Text Section
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Hello",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "Track Smarter, Travel Easier ",
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // Buttons
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val configuration = LocalConfiguration.current
                    val screenWidth = configuration.screenWidthDp.dp

                    val buttonHeight = when {
                        screenWidth < 360.dp -> 44.dp  // small devices
                        screenWidth < 600.dp -> 48.dp  // normal phones
                        else -> 54.dp                  // tablets / large screens
                    }

                    val fontSize = when {
                        screenWidth < 360.dp -> 14.sp
                        else -> 16.sp
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // 🔵 Login Button
                        Button(
                            onClick = { onLoginClick() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B3EFF)),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(buttonHeight)
                        ) {
                            Text(
                                text = "Login",
                                color = Color.White,
                                fontSize = fontSize,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // ⚪ Outlined Sign Up Button
                        OutlinedButton(
                            onClick = { onSignUpClick() },
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(1.dp, Color(0xFF4B3EFF)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(buttonHeight)
                        ) {
                            Text(
                                text = "Sign Up",
                                color = Color(0xFF4B3EFF),
                                fontSize = fontSize,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                }

            }
        }
    }
}



