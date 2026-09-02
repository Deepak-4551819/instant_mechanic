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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestServiceScreen(
    mechanicId: String,
    mechanicName: String,
    onBackClick: () -> Unit,
    viewModel: RequestViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    var customerName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var vehicleNumber by remember { mutableStateOf("") }
    var problemDescription by remember { mutableStateOf("") }

    val defaultServices = listOf("General Inspection", "Brake Repair", "Oil Change", "Engine Diagnostics", "Tire Replacement", "Emergency Towing")
    var expandedDropdown by remember { mutableStateOf(false) }
    var selectedService by remember { mutableStateOf(defaultServices.first()) }

    val isPhoneValid = phoneNumber.matches(Regex("^[0-9]{10}$"))
    val isFormValid = customerName.trim().length >= 2 &&
            isPhoneValid &&
            vehicleNumber.trim().length >= 4 &&
            problemDescription.trim().isNotBlank() &&
            !state.isSubmitting

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

            OutlinedTextField(
                value = customerName,
                onValueChange = { customerName = it },
                label = { Text("Customer Full Name *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { if (it.length <= 10 && it.all { char -> char.isDigit() }) phoneNumber = it },
                label = { Text("Phone Number (10 digits) *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                isError = phoneNumber.isNotEmpty() && !isPhoneValid
            )

            OutlinedTextField(
                value = vehicleNumber,
                onValueChange = { vehicleNumber = it.uppercase() },
                label = { Text("Vehicle Plate (e.g. MH02AB1234) *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

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
                    modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
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

            OutlinedTextField(
                value = problemDescription,
                onValueChange = { problemDescription = it },
                label = { Text("Describe the Problem *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                minLines = 3,
                maxLines = 5
            )

            state.errorMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.submitBooking(
                        mechanicId = mechanicId,
                        mechanicName = mechanicName,
                        customerName = customerName,
                        phoneNumber = phoneNumber,
                        vehicleNumber = vehicleNumber,
                        selectedService = selectedService,
                        description = problemDescription
                    )
                },
                enabled = isFormValid,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                if (state.isSubmitting) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Confirm & Submit to Firestore", style = MaterialTheme.typography.titleSmall)
                }
            }
        }
    }

    if (state.isSuccess) {
        AlertDialog(
            onDismissRequest = { onBackClick() },
            title = { Text("Request Synced to Cloud!", fontWeight = FontWeight.Bold) },
            text = {
                Text("Your request for $selectedService has been stored in Firebase Firestore. The garage team will reach out at $phoneNumber.")
            },
            confirmButton = {
                Button(onClick = { onBackClick() }) {
                    Text("View Bookings")
                }
            }
        )
    }
}
