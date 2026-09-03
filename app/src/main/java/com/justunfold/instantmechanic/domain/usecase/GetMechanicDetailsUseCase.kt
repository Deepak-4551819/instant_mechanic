package com.justunfold.instantmechanic.domain.usecase

import com.justunfold.instantmechanic.domain.model.Mechanic
import com.justunfold.instantmechanic.domain.repository.MechanicRepository
import javax.inject.Inject

class GetMechanicDetailsUseCase @Inject constructor(
    private val repository: MechanicRepository
) {
    suspend operator fun invoke(id: String): Mechanic? {
        return repository.getMechanicById(id)
    }
}
