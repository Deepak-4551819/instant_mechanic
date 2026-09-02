package com.justunfold.instantmechanic.data.mapper

import com.justunfold.instantmechanic.data.database.entity.MechanicEntity
import com.justunfold.instantmechanic.data.dto.MechanicDto
import com.justunfold.instantmechanic.domain.model.Mechanic

fun MechanicDto.toEntity(): MechanicEntity {
    return MechanicEntity(
        id = id,
        name = name,
        rating = rating,
        distance = distance,
        location = location,
        address = address,
        isOpen = isOpen,
        workingHours = workingHours,
        phoneNumber = phoneNumber,
        imageUrl = imageUrl,
        servicesCsv = services.joinToString(",")
    )
}

fun MechanicEntity.toDomain(): Mechanic {
    return Mechanic(
        id = id,
        name = name,
        rating = rating,
        distance = distance,
        location = location,
        address = address,
        isOpen = isOpen,
        workingHours = workingHours,
        phoneNumber = phoneNumber,
        imageUrl = imageUrl,
        services = if (servicesCsv.isBlank()) emptyList() else servicesCsv.split(",")
    )
}
