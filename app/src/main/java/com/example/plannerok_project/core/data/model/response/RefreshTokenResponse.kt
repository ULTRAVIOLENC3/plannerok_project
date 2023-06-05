package com.example.plannerok_project.core.data.model.response

data class RefreshTokenResponse(
    val access_token: String,
    val refresh_token: String,
    val user_id: Int
)