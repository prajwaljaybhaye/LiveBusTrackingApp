package com.litsbros.livebustrackingsystem.driver

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.litsbros.livebustrackingsystem.backand.ViewModel.DriverViewModel.DriverViewModel
import com.litsbros.livebustrackingsystem.driver.ui.theme.LiveBusTrackingSystemTheme

class DriverAuthActivity : ComponentActivity() {
    private val viewModel: DriverViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LiveBusTrackingSystemTheme {
                DriverLoginScreen(
                    viewModel = viewModel,
                    onLoginSuccess = {
                        startActivity(Intent(this, DriverHomeScreenActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun DriverLoginScreen(
    viewModel: DriverViewModel,
    onLoginSuccess: () -> Unit
) {
    var driverId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 🔷 Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF1976D2), Color(0xFF42A5F5))
                    )
                )
                .padding(top = 48.dp, bottom = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsBus,
                        contentDescription = "Bus Icon",
                        tint = Color(0xFF1976D2),
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text("Driver Portal", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Welcome back, driver!", color = Color.White, fontSize = 14.sp)
            }
        }

        // 🔳 Login Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Login to Continue", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = driverId,
                    onValueChange = { driverId = it },
                    label = { Text("Driver ID") },
                    placeholder = { Text("Enter your Driver ID Number ") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    placeholder = { Text("Enter your password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = { /* TODO: Forgot password */ }) {
                    Text("Forgot Password?", color = Color(0xFF1976D2))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        isLoading = true
                        viewModel.loginDriver(
                            username = driverId,
                            password = password,
                            onSuccess = {
                                isLoading = false
                                onLoginSuccess()
                            },
                            onError = {
                                isLoading = false
                                errorMessage = it
                            }
                        )
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp) // adds responsive spacing
                        .heightIn(min = 48.dp, max = 56.dp) // allows slight adjustment based on device size
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1976D2),
                        disabledContainerColor = Color(0xFF90CAF9) // lighter color when disabled
                    ),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Text(
                        text = if (isLoading) "Logging in..." else "Login",
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }


                errorMessage?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it, color = Color.Red, fontSize = 14.sp)
                }

                if (isLoading) {
                    Spacer(modifier = Modifier.height(12.dp))
                    CircularProgressIndicator()
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Contact your school administrator if you have login issues.",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
