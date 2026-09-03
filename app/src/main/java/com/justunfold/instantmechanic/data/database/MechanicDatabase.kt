package com.justunfold.instantmechanic.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.justunfold.instantmechanic.data.database.dao.MechanicDao
import com.justunfold.instantmechanic.data.database.entity.MechanicEntity

@Database(entities = [MechanicEntity::class], version = 1, exportSchema = false)
abstract class MechanicDatabase : RoomDatabase() {
    abstract val mechanicDao: MechanicDao
}
