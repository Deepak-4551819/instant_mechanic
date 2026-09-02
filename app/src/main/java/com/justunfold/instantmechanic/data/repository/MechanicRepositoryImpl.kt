package com.justunfold.instantmechanic.data.repository

import com.justunfold.instantmechanic.core.util.Resource
import com.justunfold.instantmechanic.data.api.MechanicApiService
import com.justunfold.instantmechanic.data.database.MechanicDatabase
import com.justunfold.instantmechanic.data.mapper.toDomain
import com.justunfold.instantmechanic.data.mapper.toEntity
import com.justunfold.instantmechanic.domain.model.Mechanic
import com.justunfold.instantmechanic.domain.repository.MechanicRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class MechanicRepositoryImpl @Inject constructor(
    private val api: MechanicApiService,
    private val db: MechanicDatabase
) : MechanicRepository {

    private val dao = db.mechanicDao

    override fun getMechanics(fetchFromRemote: Boolean): Flow<Resource<List<Mechanic>>> = flow {
        emit(Resource.Loading(true))

        // 1. Emit existing cached records immediately
        val cachedEntities = dao.getAllMechanics().first()
        val cachedList = cachedEntities.map { it.toDomain() }
        emit(Resource.Success(cachedList))

        val isDbEmpty = cachedList.isEmpty()
        val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote

        if (shouldJustLoadFromCache) {
            emit(Resource.Loading(false))
            return@flow
        }

        // 2. Fetch fresh data from network
        try {
            val remoteList = api.getMechanics()
            dao.clearAllMechanics()
            dao.upsertMechanics(remoteList.map { it.toEntity() })
            val freshList = dao.getAllMechanics().first().map { it.toDomain() }
            emit(Resource.Success(freshList))
        } catch (e: IOException) {
            e.printStackTrace()
            emit(Resource.Error("Couldn't reach server. Showing offline data.", cachedList))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred", cachedList))
        }

        // 3. Emit loading complete
        emit(Resource.Loading(false))
    }

    override suspend fun getMechanicById(id: String): Mechanic? {
        return dao.getMechanicById(id)?.toDomain()
    }
}
