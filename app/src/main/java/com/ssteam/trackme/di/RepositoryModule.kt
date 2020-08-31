package com.ssteam.trackme.di

import com.ssteam.trackme.data.repositories.ResultRepositoryIpm
import com.ssteam.trackme.domain.repositories.ResultRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindResultRepository(repo: ResultRepositoryIpm) : ResultRepository
}