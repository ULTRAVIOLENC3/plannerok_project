package com.example.plannerok_project.feature_auth.data.data_source

import com.example.plannerok_project.core.refresh_token.RefreshTokenRequest
import com.example.plannerok_project.core.refresh_token.RefreshTokenResponse
import com.example.plannerok_project.feature_auth.domain.model.request.CheckAuthCodeRequest
import com.example.plannerok_project.feature_auth.domain.model.request.SendAuthCodeRequest
import com.example.plannerok_project.feature_auth.domain.model.request.UserRegisterRequest
import com.example.plannerok_project.feature_auth.domain.model.response.CheckAuthCodeResponse
import com.example.plannerok_project.feature_auth.domain.model.response.SendAuthCodeResponse
import com.example.plannerok_project.feature_auth.domain.model.response.UserRegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/v1/users/send-auth-code/")
    suspend fun sendAuthCode(@Body sendAuthCodeRequest : SendAuthCodeRequest) : Response<SendAuthCodeResponse>

    @POST("/api/v1/users/check-auth-code/")
    suspend fun checkAuthCode(@Body checkAuthCodeRequest: CheckAuthCodeRequest) : Response<CheckAuthCodeResponse>

    @POST("/api/v1/users/register/")
    suspend fun userRegister(@Body userRegisterRequest: UserRegisterRequest) : Response<UserRegisterResponse>

    @POST("/api/v1/users/refresh-token/")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest) : Response<RefreshTokenResponse>
}