package com.example.plannerok_project.feature_auth.di


import com.example.plannerok_project.feature_auth.data.data_source.AuthApi
import com.example.plannerok_project.feature_auth.data.repository.AuthRepositoryImpl
import com.example.plannerok_project.feature_auth.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthRepositoryModule {

    @Provides
    @Singleton
    fun provideApiAuth(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(authApi: AuthApi): AuthRepository {
        return AuthRepositoryImpl(authApi)
    }
}