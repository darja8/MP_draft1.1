package com.example.mp_draft10.di

import com.example.mp_draft10.repository.ImageSearchRepository
import com.example.mp_draft10.repository.ImageSearchRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindScheduleRepository(albumRepositoryImpl: ImageSearchRepositoryImpl): ImageSearchRepository

}