package com.example.plannerok_project.feature_profile.data.data_source

import com.example.plannerok_project.feature_profile.domain.model.request.UpdateUserRequest
import com.example.plannerok_project.feature_profile.domain.model.response.GetCurrentUserResponse
import com.example.plannerok_project.feature_profile.domain.model.response.UpdateUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT

interface ProfileApi {
    @GET("/api/v1/users/me/")
    suspend fun getCurrentUser(@Header("Authorization") accessToken: String?) : Response<GetCurrentUserResponse>

    @PUT("/api/v1/users/me/")
    suspend fun updateUser(
        @Header("Authorization") accessToken: String?,
        @Body updateUserRequest: UpdateUserRequest) : Response<UpdateUserResponse>
}