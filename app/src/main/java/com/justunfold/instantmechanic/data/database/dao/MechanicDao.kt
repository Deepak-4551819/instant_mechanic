package com.justunfold.instantmechanic.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.justunfold.instantmechanic.data.database.entity.MechanicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MechanicDao {

    @Query("SELECT * FROM mechanics")
    fun getAllMechanics(): Flow<List<MechanicEntity>>

    @Query("SELECT * FROM mechanics WHERE id = :id")
    suspend fun getMechanicById(id: String): MechanicEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMechanics(mechanics: List<MechanicEntity>)

    @Query("DELETE FROM mechanics")
    suspend fun clearAllMechanics()
}
