package com.justunfold.instantmechanic.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mechanics")
data class MechanicEntity(
    @PrimaryKey
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
    val servicesCsv: String
)
