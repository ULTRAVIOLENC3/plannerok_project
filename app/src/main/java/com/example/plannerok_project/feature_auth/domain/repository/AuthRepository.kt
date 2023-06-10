package com.example.plannerok_project.feature_auth.domain.repository

import com.example.plannerok_project.feature_auth.domain.model.request.CheckAuthCodeRequest
import com.example.plannerok_project.feature_auth.domain.model.request.SendAuthCodeRequest
import com.example.plannerok_project.feature_auth.domain.model.request.UserRegisterRequest
import com.example.plannerok_project.feature_auth.domain.model.response.CheckAuthCodeResponse
import com.example.plannerok_project.feature_auth.domain.model.response.SendAuthCodeResponse
import com.example.plannerok_project.feature_auth.domain.model.response.UserRegisterResponse
import com.example.plannerok_project.core.utils.Resource
import com.example.plannerok_project.core.refresh_token.RefreshTokenRequest
import com.example.plannerok_project.core.refresh_token.RefreshTokenResponse

interface AuthRepository {
    suspend fun sendAuthCode(sendAuthCodeRequest : SendAuthCodeRequest) : Resource<SendAuthCodeResponse>

    suspend fun checkAuthCode(checkAuthCodeRequest: CheckAuthCodeRequest) : Resource<CheckAuthCodeResponse>

    suspend fun userRegister(userRegisterRequest: UserRegisterRequest) : Resource<UserRegisterResponse>

    suspend fun refreshToken(refreshTokenRequest: RefreshTokenRequest) : Resource<RefreshTokenResponse>
}