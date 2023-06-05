package com.example.plannerok_project.feature_profile.di

import com.example.plannerok_project.feature_profile.data.data_source.ProfileApi
import com.example.plannerok_project.feature_profile.data.repository.ProfileRepositoryImpl
import com.example.plannerok_project.feature_profile.domain.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileRepositoryModule {

    @Provides
    @Singleton
    fun provideProfileAuth(retrofit: Retrofit): ProfileApi {
        return retrofit.create(ProfileApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(profileApi: ProfileApi): ProfileRepository {
        return ProfileRepositoryImpl(profileApi)
    }
}