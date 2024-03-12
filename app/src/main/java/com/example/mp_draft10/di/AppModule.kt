package com.example.mp_draft10.di

import com.example.mp_draft10.auth.AuthRepository
import com.example.mp_draft10.auth.AuthRepositoryImpl
import com.example.mp_draft10.database.DatabaseRepository
import com.example.mp_draft10.database.DatabaseRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesRepositoryImpl(firebaseAuth:FirebaseAuth): AuthRepository {
        return  AuthRepositoryImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun providesDatabaseRepositoryImpl(): DatabaseRepository {
        return DatabaseRepositoryImpl()
    }
}
