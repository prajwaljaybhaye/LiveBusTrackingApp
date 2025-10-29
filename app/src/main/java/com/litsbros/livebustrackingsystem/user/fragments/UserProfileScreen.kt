package com.litsbros.livebustrackingsystem.user.fragments

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.litsbros.livebustrackingsystem.Activites.RoleActivity
import com.litsbros.livebustrackingsystem.SessionManager.SessionManager
import com.litsbros.livebustrackingsystem.backand.RetrofitClient
import com.litsbros.livebustrackingsystem.backand.ViewModel.UserViewModel.UserViewModel
import com.litsbros.livebustrackingsystem.backand.ViewModel.UserViewModel.UserViewModelFactory
import com.litsbros.livebustrackingsystem.backand.model.User

@Composable
fun UserProfileScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(RetrofitClient.api, context)
    )

    val sessionManager = remember { SessionManager(context) }

    val user by viewModel.selectedUser.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchLoggedInUser()
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProfileHeader(user)
        ContactInfo(user)
        SettingsSection()
        LogoutSection(sessionManager)
    }
}

@Composable
fun ProfileHeader(user: User?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color(0xFF1976D2)),
            contentAlignment = Alignment.Center
        ) {
            val initials = user?.full_name?.split(" ")?.mapNotNull { it.firstOrNull()?.toString() }
                ?.joinToString("")?.take(2) ?: "U"
            Text(initials, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(user?.full_name ?: "Unknown", style = MaterialTheme.typography.titleLarge)
            Text("Email: ${user?.email ?: "N/A"}", fontSize = 14.sp, color = Color.Gray)
            Text("Phone: ${user?.phone_number ?: "N/A"}", fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ContactInfo(user: User?) {
    InfoCard(title = "📞 Contact Information") {
        Text("Email: ${user?.email}")
        Text("Phone: ${user?.phone_number}")
    }
}

@Composable
fun SettingsSection() {
    InfoCard(title = "⚙️ Settings") {
        Text("Push Notifications: Receive bus arrival alerts")
        Text("Location Services: Enable for better tracking")
        Text("Privacy & Security: Manage privacy settings")
        Text("About: Version 2.1.0")
    }
}



@Composable
fun LogoutSection(sessionManager: SessionManager) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Version 2.1.0", fontSize = 12.sp, color = Color.Gray)
        Button(


                onClick = {
            sessionManager.clearDriverSession()
            sessionManager.clearUserSession()

            val intent = Intent(context, RoleActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout", color = Color.White)
        }
    }
}


@Composable
fun InfoCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}


