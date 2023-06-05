package com.example.plannerok_project.feature_auth.domain.model.request

data class CheckAuthCodeRequest(
    val phone: String?,
    val code: String?
)