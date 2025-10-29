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
import com.litsbros.livebustrackingsystem.MainActivity
import com.litsbros.livebustrackingsystem.backand.RetrofitClient
import com.litsbros.livebustrackingsystem.backand.ViewModel.UserViewModel.UserViewModel
import com.litsbros.livebustrackingsystem.backand.ViewModel.UserViewModel.UserViewModelFactory

class UserLoginActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LiveBusTrackingSystemTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF4338CA)
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text("Login") },
                                navigationIcon = {
                                    IconButton(onClick = { finish() }) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowBack,
                                            contentDescription = "Back",
                                            tint = Color.White
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = Color(0xFF4338CA),
                                    titleContentColor = Color.White
                                )
                            )
                        },
                        containerColor = Color(0xFF4338CA)
                    ) { innerPadding ->
                        LoginScreen(
                            modifier = Modifier.padding(innerPadding),
                            onLoginBtnClick = {
                                // Redirect to MainActivity
                                startActivity(Intent(this@UserLoginActivity, UserHomeScreenActivity::class.java))
                                finish() // Close login screen
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(modifier: Modifier = Modifier, onLoginBtnClick: () -> Unit) {
    val context = LocalContext.current
    val viewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(RetrofitClient.api, context)
    )

    val status by viewModel.statusMessage.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = modifier
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo or Illustration
            Image(
                painter = painterResource(id = R.drawable.login_logo),
                contentDescription = "Login Illustration",
                modifier = Modifier
                    .size(160.dp)
                    .padding(bottom = 16.dp)
            )

            Text(
                text = "Login",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 24.dp)
            )



            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )

            TextButton(
                onClick = { /* Forgot password logic */ },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Forgot password?", color = Color.Gray, fontSize = 12.sp)
            }

            Button(
                onClick = {
                    viewModel.login(email, password)
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4338CA)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp) // adds spacing for all screen sizes
                    .heightIn(min = 48.dp, max = 56.dp) // adjusts height for different screens
                    .wrapContentHeight()
            ) {
                Text(
                    text = if (isLoading) "Logging in..." else "Login", // responsive feedback
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }


            if (!status.isNullOrBlank()) {
                Text(
                    text = status!!,
                    color = if (status == "Login successful") Color(0xFF2E7D32) else Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp)
                )
            }


            LaunchedEffect(status) {
                if (status == "Login successful") {
                    onLoginBtnClick()
                    viewModel.clearStatusMessage()
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LiveBusTrackingSystemTheme {
        LoginScreen(
            modifier = Modifier.fillMaxSize(),
            onLoginBtnClick = { /* Preview does nothing */ }
        )
    }
}
