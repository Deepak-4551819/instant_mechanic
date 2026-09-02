package com.justunfold.instantmechanic.domain.usecase

import com.justunfold.instantmechanic.core.util.Resource
import com.justunfold.instantmechanic.domain.model.Mechanic
import com.justunfold.instantmechanic.domain.repository.MechanicRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMechanicsUseCase @Inject constructor(
    private val repository: MechanicRepository
) {
    operator fun invoke(fetchFromRemote: Boolean = false): Flow<Resource<List<Mechanic>>> {
        return repository.getMechanics(fetchFromRemote)
    }
}
