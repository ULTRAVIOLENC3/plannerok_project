package com.example.plannerok_project.core.di

import android.content.Context
import com.example.plannerok_project.core.local_data.LocalData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {

    @Provides
    @Singleton
    fun provideLocalData(@ApplicationContext context: Context): LocalData {
        return LocalData(context)
    }
}