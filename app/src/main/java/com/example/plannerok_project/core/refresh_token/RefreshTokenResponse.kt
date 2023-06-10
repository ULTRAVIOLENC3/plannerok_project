package com.example.plannerok_project.core.refresh_token

data class RefreshTokenResponse(
    val access_token: String,
    val refresh_token: String,
    val user_id: Int
)