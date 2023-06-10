package com.example.plannerok_project.feature_profile.domain.model.request

data class UpdateUserRequest(
    val avatar: Avatar?,
    val birthday: String?,
    val city: String?,
    val instagram: String?,
    val name: String?,
    val status: String?,
    val username: String?,
    val vk: String?
)