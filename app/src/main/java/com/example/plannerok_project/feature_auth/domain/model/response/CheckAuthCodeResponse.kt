package com.example.plannerok_project.feature_auth.domain.model.response

data class CheckAuthCodeResponse(
    val access_token: String,
    val is_user_exists: Boolean,
    val refresh_token: String,
    val user_id: Int
)