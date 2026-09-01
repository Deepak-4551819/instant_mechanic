package com.justunfold.instantmechanic.data.api

import com.justunfold.instantmechanic.data.dto.MechanicDto
import retrofit2.http.GET

interface MechanicApiService {
    @GET("mechanics.json")
    suspend fun getMechanics(): List<MechanicDto>
}
