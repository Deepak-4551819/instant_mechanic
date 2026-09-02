package com.justunfold.instantmechanic.domain.repository

import com.justunfold.instantmechanic.core.util.Resource
import com.justunfold.instantmechanic.domain.model.Mechanic
import kotlinx.coroutines.flow.Flow

interface MechanicRepository {
    fun getMechanics(fetchFromRemote: Boolean): Flow<Resource<List<Mechanic>>>
    suspend fun getMechanicById(id: String): Mechanic?
}
