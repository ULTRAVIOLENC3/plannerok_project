package com.example.plannerok_project.feature_profile.data.repository

import com.example.plannerok_project.feature_profile.data.data_source.ProfileApi
import com.example.plannerok_project.feature_profile.domain.model.request.UpdateUserRequest
import com.example.plannerok_project.feature_profile.domain.model.response.GetCurrentUserResponse
import com.example.plannerok_project.feature_profile.domain.model.response.UpdateUserResponse
import com.example.plannerok_project.feature_profile.domain.repository.ProfileRepository
import com.example.plannerok_project.core.utils.Resource
import java.io.IOException
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileApi: ProfileApi
    ): ProfileRepository {

    override suspend fun getCurrentUser(accessToken: String?): Resource<GetCurrentUserResponse> {
        return try {
            val response = profileApi.getCurrentUser(accessToken = "Bearer $accessToken")
            if (response.isSuccessful && response.body() != null){
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("GetCurrentUser API call failed.")
            }
        } catch (e: IOException) {
            Resource.Error("IOException")
        } catch (e: Exception) {
            Resource.Error("Exception")
        }
    }

    override suspend fun updateUser(accessToken: String?, updateUserRequest: UpdateUserRequest): Resource<UpdateUserResponse> {
        return try {
            val response = profileApi.updateUser(accessToken, updateUserRequest)
            if (response.isSuccessful && response.body() != null){
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("UpdateUser API call failed.")
            }
        } catch (e: IOException) {
            Resource.Error("IOException")
        } catch (e: Exception) {
            Resource.Error("Exception")
        }
    }

}