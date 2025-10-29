package com.litsbros.livebustrackingsystem.user

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.litsbros.livebustrackingsystem.Activites.ui.theme.LiveBusTrackingSystemTheme
import com.litsbros.livebustrackingsystem.R
import com.litsbros.livebustrackingsystem.backand.ApiInterface.AdminApi
import com.litsbros.livebustrackingsystem.backand.RetrofitClient
import com.litsbros.livebustrackingsystem.backand.ViewModel.UserViewModel.UserViewModel
import com.litsbros.livebustrackingsystem.backand.ViewModel.UserViewModel.UserViewModelFactory
import com.litsbros.livebustrackingsystem.backand.model.User


class UesrRegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LiveBusTrackingSystemTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF4338CA)
                ) {
                    RegistrationScreen(
                        onBackClick = { finish() },
                        onRegisterClick = {
                            startActivity(
                                Intent(
                                    this@UesrRegistrationActivity,
                                    UserHomeScreenActivity::class.java
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
fun RegistrationScreen(
    onBackClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val viewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(
            api = RetrofitClient.api,
            context = context
        )
    )

    val isLoading by viewModel.isLoading.observeAsState(false)
    val status by viewModel.statusMessage.observeAsState("")

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    LaunchedEffect(status) {
        if (status.startsWith("User created")) {
            onRegisterClick()
            viewModel.clearStatusMessage()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(40.dp))
                .background(
                    brush = Brush.verticalGradient(
                        listOf(Color.White, Color(0xFFF4F4FF))
                    )
                )
                .padding(24.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF4338CA)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.login_logo),
                    contentDescription = "Register Illustration",
                    modifier = Modifier
                        .size(160.dp)
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = "Create Account",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            )

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 12.dp),
                    color = Color(0xFF4338CA)
                )
            }

            Button(
                onClick = {
                    if (password != confirmPassword) {
                        viewModel.setStatusMessage("Passwords do not match")
                        return@Button
                    }

                    val user = User(
                        full_name = fullName,
                        email = email,
                        phone_number = phone.toLongOrNull() ?: 0L,
                        password = password
                    )
                    viewModel.createUser(user)
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4338CA)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp) // responsive spacing
                    .heightIn(min = 48.dp, max = 56.dp) // adjusts height for small/large screens
                    .wrapContentHeight()
            ) {
                Text(
                    text = if (isLoading) "Registering..." else "Register", // responsive feedback
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }


            if (status.isNotBlank()) {
                Text(
                    text = status,
                    color = if (status.startsWith("User created")) Color(0xFF2E7D32) else Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp)
                )
            }

            TextButton(
                onClick = { /* Navigate to login screen */ },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Already have an account? Login",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistrationScreenPreview() {
    LiveBusTrackingSystemTheme {
        RegistrationScreen()
    }
}
