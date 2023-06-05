package com.example.plannerok_project.feature_auth.domain.model.response

data class UserRegisterResponse(
    val access_token: String,
    val refresh_token: String,
    val user_id: Int
)