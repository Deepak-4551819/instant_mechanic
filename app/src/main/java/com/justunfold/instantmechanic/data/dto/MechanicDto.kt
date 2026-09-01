package com.justunfold.instantmechanic.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MechanicDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("rating") val rating: Double,
    @SerialName("distance") val distance: String,
    @SerialName("location") val location: String,
    @SerialName("address") val address: String,
    @SerialName("isOpen") val isOpen: Boolean,
    @SerialName("workingHours") val workingHours: String,
    @SerialName("phoneNumber") val phoneNumber: String,
    @SerialName("imageUrl") val imageUrl: String,
    @SerialName("services") val services: List<String>
)
