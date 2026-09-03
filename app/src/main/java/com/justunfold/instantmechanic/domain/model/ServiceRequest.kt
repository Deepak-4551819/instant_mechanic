package com.justunfold.instantmechanic.domain.model

data class ServiceRequest(
    val id: String = "",
    val userId: String = "",
    val mechanicId: String = "",
    val mechanicName: String = "",
    val customerName: String = "",
    val phoneNumber: String = "",
    val vehicleNumber: String = "",
    val selectedService: String = "",
    val problemDescription: String = "",
    val status: String = "Pending",
    val timestamp: Long = System.currentTimeMillis()
)
