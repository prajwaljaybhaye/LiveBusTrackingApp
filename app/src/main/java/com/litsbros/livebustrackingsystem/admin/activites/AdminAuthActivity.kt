package com.litsbros.livebustrackingsystem.admin.activites

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.litsbros.livebustrackingsystem.Activites.ui.theme.LiveBusTrackingSystemTheme
import com.litsbros.livebustrackingsystem.backand.RetrofitClient
import com.litsbros.livebustrackingsystem.backand.ViewModel.AdminViewModel.AdminViewModel
import com.litsbros.livebustrackingsystem.backand.ViewModel.AdminViewModel.AdminViewModelFactory

class AdminAuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel =
            AdminViewModelFactory(RetrofitClient.api, this).create(AdminViewModel::class.java)

        setContent {
            LiveBusTrackingSystemTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AdminLoginScreen(
                        viewModel = viewModel,
                        onLoginSuccess = {
                            startActivity(Intent(this, AdminHomeScreenActivity::class.java))
                            finish()
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun AdminLoginScreen(
    viewModel: AdminViewModel,
    onLoginSuccess: () -> Unit
) {
    var adminId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    val isLoading by viewModel.isLoading.observeAsState(false)
    val statusMessage by viewModel.statusMessage.observeAsState("")

    LaunchedEffect(statusMessage) {
        if (statusMessage == "Login successful") {
            onLoginSuccess()
            viewModel.clearStatusMessage()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0D47A1), Color(0xFF42A5F5))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_secure),
                    contentDescription = "Admin Icon",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Admin Portal",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                "Live Bus Tracking System",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Sign in to Dashboard", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = adminId,
                        onValueChange = { adminId = it },
                        label = { Text("Admin ID") },
                        placeholder = { Text("Enter Admin ID") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Person2,
                                contentDescription = "AdminId"
                            )
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        placeholder = { Text("Enter your password") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it })
                            Text("Remember me", fontSize = 12.sp)
                        }

                        TextButton(onClick = { /* Forgot Password */ }) {
                            Text("Forgot Password?", color = Color(0xFF0D47A1), fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.login(adminId, password) },
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp, max = 56.dp) // allows small adaptive height
                            .padding(horizontal = 16.dp, vertical = 8.dp) // adds responsive spacing
                            .wrapContentHeight()
                            .clip(RoundedCornerShape(12.dp)), // ensures consistent shape even on small screens
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (isLoading) "Signing In..." else "Sign In",
                            color = Color.White,
                            fontSize = 16.sp, // consistent readable size
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp) // avoids text clipping on smaller screens
                        )
                    }


                    if (statusMessage.isNotBlank() && statusMessage != "Login successful") {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = statusMessage,
                            color = Color.Red,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "© 2025 Live Bus Tracking System. All rights reserved.",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}
