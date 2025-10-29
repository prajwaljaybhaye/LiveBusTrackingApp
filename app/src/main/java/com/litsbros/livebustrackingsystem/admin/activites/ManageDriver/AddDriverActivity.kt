package com.litsbros.livebustrackingsystem.admin.activites.ManageDriver

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.litsbros.livebustrackingsystem.admin.activites.ManageDriver.ui.theme.LiveBusTrackingSystemTheme
import com.litsbros.livebustrackingsystem.backand.ViewModel.DriverViewModel.DriverViewModel
import com.litsbros.livebustrackingsystem.backand.model.DriverModel


class AddDriverActivity : ComponentActivity() {
    private val viewModel: DriverViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LiveBusTrackingSystemTheme {
                AddNewDriver(
                    onBack = { finish() },
                    onAddDriverClick = { driver ->
                        viewModel.addDriver(
                            driver = driver,
                            onSuccess = {
                                Toast.makeText(this, "Driver added successfully", Toast.LENGTH_SHORT).show()
                                finish()
                            },
                            onError = { error ->
                                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                )
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewDriver(
    onBack: () -> Unit = {},
    onAddDriverClick: (DriverModel) -> Unit = {}
) {
    val scrollState = rememberScrollState()

    // Personal Info
    var firstName by remember { mutableStateOf(TextFieldValue("")) }
    var lastName by remember { mutableStateOf(TextFieldValue("")) }
    var dob by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var bloodGroup by remember { mutableStateOf("") }

    // Contact Info
    var phone by remember { mutableStateOf(TextFieldValue("")) }
    var altPhone by remember { mutableStateOf(TextFieldValue("")) }
    var contactEmail by remember { mutableStateOf(TextFieldValue("")) }
    var address by remember { mutableStateOf(TextFieldValue("")) }
    var state by remember { mutableStateOf(TextFieldValue("")) }
    var city by remember { mutableStateOf(TextFieldValue("")) }
    var zipCode by remember { mutableStateOf(TextFieldValue("")) }

    // License Info
    var licenseNumber by remember { mutableStateOf(TextFieldValue("")) }
    var licenseExpiry by remember { mutableStateOf(TextFieldValue("")) }
    var licenseType by remember { mutableStateOf("") }
    var drivingExperience by remember { mutableStateOf(TextFieldValue("")) }

    // Assignment
    var vehicleNumber by remember { mutableStateOf(TextFieldValue("")) }
    var vehicleType by remember { mutableStateOf("") }

    // Emergency Contact
    var emergencyName by remember { mutableStateOf(TextFieldValue("")) }
    var emergencyNumber by remember { mutableStateOf(TextFieldValue("")) }
    var relationship by remember { mutableStateOf(TextFieldValue("")) }

    var driverUserId by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Driver", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(Color(0xFFF6F7FB))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionCard("Personal Information", Color(0xFF2196F3)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    DriverTextField("First Name", firstName, Modifier.weight(1f)) { firstName = it }
                    DriverTextField("Last Name", lastName, Modifier.weight(1f)) { lastName = it }
                }
                DriverTextField("Date of Birth", dob, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)) { dob = it }
                DriverTextField("Email", email, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)) { email = it }
                DropdownField("Blood Group", bloodGroup, listOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")) {
                    bloodGroup = it
                }
            }

            SectionCard("Contact Information", Color(0xFF4CAF50)) {
                DriverTextField("Phone Number", phone, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)) { phone = it }
                DriverTextField("Alternate Phone Number", altPhone, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)) { altPhone = it }
                DriverTextField("Email", contactEmail, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)) { contactEmail = it }
                DriverTextField("Address", address) { address = it }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    DriverTextField("State", state, Modifier.weight(1f)) { state = it }
                    DriverTextField("City", city, Modifier.weight(1f)) { city = it }
                }
                DriverTextField("Zip Code", zipCode, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)) { zipCode = it }
            }

            SectionCard("License & Experience", Color(0xFFFF9800)) {
                DriverTextField("License Number", licenseNumber) { licenseNumber = it }
                DriverTextField("License Expiry Date", licenseExpiry , keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)) { licenseExpiry = it }
                DropdownField("License Type", licenseType, listOf("LMV", "HMV", "Commercial", "Motorcycle")) {
                    licenseType = it
                }
                DriverTextField("Years of Driving Experience", drivingExperience, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)) { drivingExperience = it }
            }

            SectionCard("Assignment", Color(0xFF7E57C2)) {
                DriverTextField("Vehicle Number", vehicleNumber) { vehicleNumber = it }
                DropdownField("Vehicle Type", vehicleType, listOf("Bus", "Van", "Truck", "Car")) {
                    vehicleType = it
                }
            }

            SectionCard("Emergency Contact", Color(0xFFE91E63)) {
                DriverTextField("Contact Name", emergencyName) { emergencyName = it }
                DriverTextField("Contact Number", emergencyNumber, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)) { emergencyNumber = it }
                DriverTextField("Relationship", relationship) { relationship = it }
            }

            SectionCard("Login Credentials", Color(0xFF009688)) {
                DriverTextField("Driver Id", driverUserId) { driverUserId = it }
                DriverTextField("Password", password, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)) { password = it }
            }



            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = {
                        val driver = DriverModel(
                            driver_id = 0, // or -1 if your backend ignores it
                            driver_user_id = driverUserId.text,
                            password = password.text,
                            first_name = firstName.text,
                            last_name = lastName.text.ifBlank { null },
                            dob = dob.text.ifBlank { null },
                            email = email.text.ifBlank { null },
                            blood_group = bloodGroup.ifBlank { null },
                            phone = phone.text.ifBlank { null },
                            alt_phone = altPhone.text.ifBlank { null },
                            contact_email = contactEmail.text.ifBlank { null },
                            address = address.text.ifBlank { null },
                            state = state.text.ifBlank { null },
                            city = city.text.ifBlank { null },
                            zip_code = zipCode.text.ifBlank { null },
                            license_number = licenseNumber.text.ifBlank { null },
                            license_expiry = licenseExpiry.text.ifBlank { null },
                            license_type = licenseType.ifBlank { null },
                            driving_experience = drivingExperience.text.toIntOrNull(),
                            vehicle_number = vehicleNumber.text.ifBlank { null },
                            vehicle_type = vehicleType.ifBlank { null },
                            emergency_name = emergencyName.text.ifBlank { null },
                            emergency_number = emergencyNumber.text.ifBlank { null },
                            emergency_relationship = relationship.text.ifBlank { null },
                            created_at = null // let backend assign timestamp
                        )


                        onAddDriverClick(driver)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                ) {
                    Text("Add Driver")
                }

            }
        }
    }
}

@Composable
fun SectionCard(
    title: String,
    color: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(color)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun DriverTextField(
    label: String,
    value: TextFieldValue,
    modifier: Modifier = Modifier.fillMaxWidth(),
    maxLines: Int = 1,
    onValueChange: (TextFieldValue) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        maxLines = maxLines,
        singleLine = maxLines == 1
    )
}

@Composable
fun DropdownField(
    label: String,
    selectedValue: String,
    options: List<String>,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(label, fontWeight = FontWeight.SemiBold)
        Box {
            OutlinedTextField(
                value = selectedValue,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                label = { Text(label) }
            )
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DriverTextField(
    label: String,
    value: TextFieldValue,
    modifier: Modifier = Modifier.fillMaxWidth(),
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (TextFieldValue) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        maxLines = maxLines,
        singleLine = maxLines == 1,
        keyboardOptions = keyboardOptions
    )
}
