package com.justunfold.instantmechanic.presentation.request

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestServiceScreen(
    mechanicName: String,
    onBackClick: () -> Unit
) {
    var customerName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var vehicleNumber by remember { mutableStateOf("") }
    var problemDescription by remember { mutableStateOf("") }

    // Service Dropdown
    val defaultServices = listOf("General Inspection", "Brake Repair", "Oil Change", "Engine Diagnostics", "Tire Replacement", "Emergency Towing")
    var expandedDropdown by remember { mutableStateOf(false) }
    var selectedService by remember { mutableStateOf(defaultServices.first()) }

    var showDialog by remember { mutableStateOf(false) }

    // Strict Validations
    val isPhoneValid = phoneNumber.matches(Regex("^[0-9]{10}$"))
    val isVehicleValid = vehicleNumber.trim().length >= 4
    val isFormValid = customerName.trim().length >= 2 &&
            isPhoneValid &&
            isVehicleValid &&
            problemDescription.trim().isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Request Service", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Booking at: $mechanicName",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                )
            }

            // Customer Name
            OutlinedTextField(
                value = customerName,
                onValueChange = { customerName = it },
                label = { Text("Customer Full Name *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Strict Phone Number Input
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { if (it.length <= 10 && it.all { char -> char.isDigit() }) phoneNumber = it },
                label = { Text("Phone Number (10 digits) *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                supportingText = {
                    if (phoneNumber.isNotEmpty() && !isPhoneValid) {
                        Text("Enter a valid 10-digit mobile number", color = MaterialTheme.colorScheme.error)
                    }
                },
                isError = phoneNumber.isNotEmpty() && !isPhoneValid
            )

            // Vehicle Number
            OutlinedTextField(
                value = vehicleNumber,
                onValueChange = { vehicleNumber = it.uppercase() },
                label = { Text("Vehicle Plate (e.g. MH02AB1234) *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Select Service Exposed Dropdown Menu
            ExposedDropdownMenuBox(
                expanded = expandedDropdown,
                onExpandedChange = { expandedDropdown = !expandedDropdown }
            ) {
                OutlinedTextField(
                    value = selectedService,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Service *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expandedDropdown,
                    onDismissRequest = { expandedDropdown = false }
                ) {
                    defaultServices.forEach { service ->
                        DropdownMenuItem(
                            text = { Text(service) },
                            onClick = {
                                selectedService = service
                                expandedDropdown = false
                            }
                        )
                    }
                }
            }

            // Problem Description
            OutlinedTextField(
                value = problemDescription,
                onValueChange = { problemDescription = it },
                label = { Text("Describe the Problem *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                minLines = 3,
                maxLines = 5
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { showDialog = true },
                enabled = isFormValid,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Confirm & Submit Request", style = MaterialTheme.typography.titleSmall)
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Service Request Confirmed!", fontWeight = FontWeight.Bold) },
            text = {
                Text("Thank you, $customerName.\n\nYour request for $selectedService on vehicle $vehicleNumber has been sent to $mechanicName. They will call you at +91 $phoneNumber shortly.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        onBackClick()
                    }
                ) {
                    Text("Done")
                }
            }
        )
    }
}
