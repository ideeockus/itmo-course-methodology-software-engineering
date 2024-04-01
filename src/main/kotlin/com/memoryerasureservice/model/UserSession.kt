package com.memoryerasureservice.model

import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val userId: Int,
    val name: String,
    val role: UserRole,
    val token: String,
)
