package com.justunfold.instantmechanic.di

import com.justunfold.instantmechanic.data.repository.MechanicRepositoryImpl
import com.justunfold.instantmechanic.domain.repository.MechanicRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMechanicRepository(
        mechanicRepositoryImpl: MechanicRepositoryImpl
    ): MechanicRepository
}
