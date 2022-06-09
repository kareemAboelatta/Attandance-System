package com.example.attendance.di

import android.content.Context
import com.example.attendance.repository.Repository
import com.example.attendance.repository.RepositoryAdmin
import com.example.attendance.repository.RepositoryUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


    @Singleton
    @Provides
    fun provideUserRepository(
        refDatabase: DatabaseReference,
        refStorage: StorageReference,
        auth: FirebaseAuth,
        @ApplicationContext
        context: Context
    )= RepositoryUser(refDatabase,refStorage,auth,context)


    @Singleton
    @Provides
    fun provideMainRepository(
        refDatabase: DatabaseReference,
        refStorage: StorageReference,
        auth: FirebaseAuth,
        @ApplicationContext
        context: Context
    )= Repository(refDatabase,refStorage,auth,context)


    @Singleton
    @Provides
    fun provideAdminRepository(
        refDatabase: DatabaseReference,
        refStorage: StorageReference,
        auth: FirebaseAuth,
        @ApplicationContext
        context: Context
    )= RepositoryAdmin(refDatabase,refStorage,auth,context)



}