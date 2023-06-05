package com.example.plannerok_project.feature_auth.domain.model.request

data class UserRegisterRequest(
    val name: String,
    val phone: String,
    val username: String
)