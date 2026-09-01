package com.justunfold.instantmechanic.data.mapper

import com.justunfold.instantmechanic.data.dto.MechanicDto
import com.justunfold.instantmechanic.domain.model.Mechanic

fun MechanicDto.toDomain(): Mechanic {
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
        services = services
    )
}
