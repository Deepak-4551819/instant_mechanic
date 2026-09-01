package com.justunfold.instantmechanic.domain.model

data class Mechanic(
    val id: String,
    val name: String,
    val rating: Double,
    val distance: String,
    val location: String,
    val address: String,
    val isOpen: Boolean,
    val workingHours: String,
    val phoneNumber: String,
    val imageUrl: String,
    val services: List<String>
)
