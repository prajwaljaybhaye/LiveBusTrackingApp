package com.litsbros.livebustrackingsystem.admin.activities.ManageBus

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.litsbros.livebustrackingsystem.Activites.ui.theme.LiveBusTrackingSystemTheme
import com.litsbros.livebustrackingsystem.backand.ViewModel.BusViewModel
import com.litsbros.livebustrackingsystem.backand.ViewModel.DriverViewModel.DriverViewModel
import com.litsbros.livebustrackingsystem.backand.ViewModel.RouteViewModel.RouteViewModel
import com.litsbros.livebustrackingsystem.backand.model.BusModel

class AddBusActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LiveBusTrackingSystemTheme {
                AddBusScreen(onBack = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBusScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: BusViewModel = viewModel(),
    driverViewModel : DriverViewModel = viewModel(),
    routeViewModel : RouteViewModel = viewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        // 🔄 Fetch data once when the AddBus screen opens
        driverViewModel.reloadDrivers()
        routeViewModel.reloadRoutes()
    }


    var busNumber by remember { mutableStateOf(TextFieldValue("")) }
    var regNumber by remember { mutableStateOf(TextFieldValue("")) }
    var manufacturer by remember { mutableStateOf(TextFieldValue("")) }
    var model by remember { mutableStateOf(TextFieldValue("")) }
    var seatingCapacity by remember { mutableStateOf(TextFieldValue("")) }
    var year by remember { mutableStateOf(TextFieldValue("")) }
    var routeId by remember { mutableStateOf(TextFieldValue("")) }
    var driverId by remember { mutableStateOf(TextFieldValue("")) }
    var statusText by remember { mutableStateOf(TextFieldValue("Active")) }
    var chassis by remember { mutableStateOf(TextFieldValue("")) }
    var engine by remember { mutableStateOf(TextFieldValue("")) }
    var insurance by remember { mutableStateOf(TextFieldValue("")) }
    var insuranceExpiry by remember { mutableStateOf(TextFieldValue("")) }
    var fitnessExpiry by remember { mutableStateOf(TextFieldValue("")) }
    var notes by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Bus", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(Color(0xFFF6F7FB))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionCard("Basic Information", Color(0xFF2196F3)) {
                BusTextField("Bus Number", busNumber) { busNumber = it }
                BusTextField("Registration Number", regNumber) { regNumber = it }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BusTextField("Manufacturer", manufacturer, Modifier.weight(1f)) { manufacturer = it }
                    BusTextField("Model", model, Modifier.weight(1f)) { model = it }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BusTextField("Seating Capacity", seatingCapacity, Modifier.weight(1f)) { seatingCapacity = it }
                    BusTextField("Year", year, Modifier.weight(1f)) { year = it }
                }
            }





            SectionCard("Assignment", Color(0xFF43A047)) {
                // 🔹 Driver Dropdown
                SelectDropdown(
                    label = "Driver (Name & ID)",
                    items = driverViewModel.drivers,
                    getItemLabel = { driver -> "${driver.first_name} ${driver.last_name} - ID: ${driver.driver_id}" },
                    getItemId = { driver -> driver.driver_id },
                    onItemSelected = { id ->
                        driverId = TextFieldValue(id?.toString() ?: "")
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 🔹 Route Dropdown
                SelectDropdown(
                    label = "Route (Name & ID)",
                    items = routeViewModel.routes,
                    getItemLabel = { route -> "${route.route_name ?: "Unnamed Route"} - ID: ${route.route_id}" },
                    getItemId = { route -> route.route_id?.toIntOrNull() },
                    onItemSelected = { id ->
                        routeId = TextFieldValue(id?.toString() ?: "")
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 🔹 Status Field
                BusTextField("Status", statusText) { statusText = it }
            }



            SectionCard("Technical Details", Color(0xFFFF9800)) {
                BusTextField("Chassis Number", chassis) { chassis = it }
                BusTextField("Engine Number", engine) { engine = it }
            }

            SectionCard("Documentation", Color(0xFF7E57C2)) {
                BusTextField("Insurance Policy Number", insurance) { insurance = it }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BusTextField("Insurance Expiry", insuranceExpiry, Modifier.weight(1f)) { insuranceExpiry = it }
                    BusTextField("Fitness Certificate Expiry", fitnessExpiry, Modifier.weight(1f)) { fitnessExpiry = it }
                }
                BusTextField("Additional Notes", notes, Modifier.fillMaxWidth(), maxLines = 3) { notes = it }
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
                        val bus = BusModel(
                            bus_number = busNumber.text,
                            registration_number = regNumber.text,
                            manufacturer = manufacturer.text,
                            model = model.text,
                            seating_capacity = seatingCapacity.text.toIntOrNull(),
                            year = year.text.toIntOrNull(),
                            route_id = routeId.text.toIntOrNull(),
                            driver_id = driverId.text.toIntOrNull(),
                            status = statusText.text,
                            chassis_number = chassis.text,
                            engine_number = engine.text,
                            insurance_policy_number = insurance.text,
                            insurance_expiry = insuranceExpiry.text,
                            fitness_expiry = fitnessExpiry.text,
                            notes = notes.text
                        )

                        viewModel.addBus(
                            bus = bus,
                            onSuccess = {
                                Toast.makeText(context, "Bus added successfully", Toast.LENGTH_SHORT).show()
                                onBack()
                            },
                            onError = { error ->
                                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                            }
                        )
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                ) {
                    Text("Add Bus")
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SelectDropdown(
    label: String,
    items: List<T>,
    getItemLabel: (T) -> String,
    getItemId: (T) -> Int?,
    onItemSelected: (Int?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedLabel by remember { mutableStateOf("") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedLabel.ifEmpty { "Select $label" },
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (items.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("No $label available") },
                    onClick = { expanded = false }
                )
            } else {
                items.forEach { item ->
                    val itemLabel = getItemLabel(item)
                    DropdownMenuItem(
                        text = { Text(itemLabel) },
                        onClick = {
                            selectedLabel = itemLabel
                            onItemSelected(getItemId(item))
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}



@Composable
fun SectionCard(title: String, color: Color, content: @Composable ColumnScope.() -> Unit) {
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
fun BusTextField(
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
