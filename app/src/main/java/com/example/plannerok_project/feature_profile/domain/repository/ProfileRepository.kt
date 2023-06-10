package com.example.plannerok_project.feature_profile.domain.repository

import com.example.plannerok_project.core.utils.Resource
import com.example.plannerok_project.feature_profile.domain.model.request.UpdateUserRequest
import com.example.plannerok_project.feature_profile.domain.model.response.GetCurrentUserResponse
import com.example.plannerok_project.feature_profile.domain.model.response.UpdateUserResponse

interface ProfileRepository {
    suspend fun getCurrentUser(accessToken: String?) : Resource<GetCurrentUserResponse>

    suspend fun updateUser(accessToken: String?, updateUserRequest: UpdateUserRequest) : Resource<UpdateUserResponse>
}