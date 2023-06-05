package com.example.plannerok_project.feature_auth.data.repository


import com.example.plannerok_project.feature_auth.data.data_source.AuthApi
import com.example.plannerok_project.feature_auth.domain.model.request.CheckAuthCodeRequest
import com.example.plannerok_project.feature_auth.domain.model.request.SendAuthCodeRequest
import com.example.plannerok_project.feature_auth.domain.model.request.UserRegisterRequest
import com.example.plannerok_project.feature_auth.domain.model.response.CheckAuthCodeResponse
import com.example.plannerok_project.feature_auth.domain.model.response.SendAuthCodeResponse
import com.example.plannerok_project.feature_auth.domain.model.response.UserRegisterResponse
import com.example.plannerok_project.feature_auth.domain.repository.AuthRepository
import com.example.plannerok_project.core.utils.Resource
import com.example.plannerok_project.core.data.model.request.RefreshTokenRequest
import com.example.plannerok_project.core.data.model.response.RefreshTokenResponse
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    ): AuthRepository {

    override suspend fun sendAuthCode(sendAuthCodeRequest: SendAuthCodeRequest): Resource<SendAuthCodeResponse> {
        return try {
            val response = authApi.sendAuthCode(sendAuthCodeRequest)
            if (response.isSuccessful && response.body() != null){
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("SendAuthCode API call failed.")
            }
        } catch (e: IOException) {
            Resource.Error("IOException")
        } catch (e: Exception) {
            Resource.Error(" Exception")
        }
    }

    override suspend fun checkAuthCode(checkAuthCodeRequest: CheckAuthCodeRequest): Resource<CheckAuthCodeResponse> {
        return try {
            val response = authApi.checkAuthCode(checkAuthCodeRequest)
            if (response.isSuccessful && response.body() != null){
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("checkAuthCode API call failed.")
            }
        } catch (e: IOException) {
            Resource.Error("IOException")
        } catch (e: Exception) {
            Resource.Error(" Exception")
        }
    }

    override suspend fun userRegister(userRegisterRequest: UserRegisterRequest): Resource<UserRegisterResponse> {
        return try {
            val response = authApi.userRegister(userRegisterRequest)
            if (response.isSuccessful && response.body() != null){
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("userRegister API call failed.")
            }
        } catch (e: IOException) {
            Resource.Error("IOException")
        } catch (e: Exception) {
            Resource.Error(" Exception")
        }
    }

    override suspend fun refreshToken(refreshTokenRequest: RefreshTokenRequest): Resource<RefreshTokenResponse> {
        return try {
            val response = authApi.refreshToken(refreshTokenRequest)
            if (response.isSuccessful && response.body() != null){
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("userRegister API call failed.")
            }
        } catch (e: IOException) {
            Resource.Error("IOException")
        } catch (e: Exception) {
            Resource.Error(" Exception")
        }
    }

}