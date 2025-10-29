package com.litsbros.livebustrackingsystem.admin.fragments

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.litsbros.livebustrackingsystem.Activites.RoleActivity
import com.litsbros.livebustrackingsystem.SessionManager.SessionManager
import com.litsbros.livebustrackingsystem.backand.model.Admin

@Composable
fun AdminProfileScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val admin = remember { sessionManager.getSavedAdmin() }

    if (admin == null) {
        Text("⚠️ No admin session found", modifier = Modifier.padding(16.dp))
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AdminHeader(admin)
        AdminContactInfo(admin)
        AdminSettingsSection()
        AdminLogoutSection(sessionManager)
    }
}

@Composable
fun AdminHeader(admin: Admin) {
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
            val initials = admin.full_name.split(" ").mapNotNull { it.firstOrNull()?.toString() }
                .joinToString("").take(2)
            Text(initials, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text("🧑 ${admin.full_name}", style = MaterialTheme.typography.titleLarge)
            Text("📧 ${admin.email}", fontSize = 14.sp, color = Color.Gray)
            Text("📱 ${admin.phone_number}", fontSize = 14.sp, color = Color.Gray)
            Text("🎓 ${admin.role}", fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
fun AdminContactInfo(admin: Admin) {
    InfoCard(title = "📞 Contact Information") {
        Text("Email: ${admin.email}")
        Text("Phone: ${admin.phone_number}")
        Text("Role: ${admin.role}")
    }
}

@Composable
fun AdminSettingsSection() {
    InfoCard(title = "⚙️ Settings") {
        Text("Push Notifications: Receive system alerts")
        Text("Email Reports: Daily admin summaries")
        Text("Security: Manage access and roles")
        Text("About: Version 1.0.0")
    }
}

@Composable
fun AdminLogoutSection(sessionManager: SessionManager) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Version 1.0.0", fontSize = 12.sp, color = Color.Gray)

        Button(
            onClick = {
                // Clear session
                sessionManager.clearAdminSession()

                // Navigate to RoleActivity and clear back stack
                val intent = Intent(context, RoleActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
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





@Composable
fun ProfileCard(name: String, role: String, email: String, phone: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(role, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Email: $email", fontSize = 13.sp)
            Text("Phone: $phone", fontSize = 13.sp)
            Text("Role: $role", fontSize = 13.sp)
        }
    }
}

@Composable
fun SettingsToggle(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(title, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            Text(description, fontSize = 12.sp, color = Color.Gray)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun AppInfoFooter() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Live Bus Tracking System - Admin", fontSize = 14.sp, fontWeight = FontWeight.Medium)
        Text("Version: 1.0.0", fontSize = 12.sp, color = Color.Gray)
        Text("© 2025 All rights reserved", fontSize = 12.sp, color = Color.Gray)
    }
}
