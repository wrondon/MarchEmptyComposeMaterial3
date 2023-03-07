package com.example.apptemplatebase.data.remote.di

import com.example.apptemplatebase.data.remote.network.NetworkService
import com.wrondon.baset.data.local.database.AppDatabase
import com.wrondon.baset.data.local.database.DataItemTypeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    fun provideNetworkService(): NetworkService {
        return NetworkService
    }
}