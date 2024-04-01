package com.memoryerasureservice.model

enum class UserRole {
    ADMIN, DOCTOR, TECHNICIAN, NOTIFICATION_AGENT, ERASER, MANAGER
}

data class User(
    val id: Int,
    val name: String,
    val passwordHash: String, // Пароль должен быть захеширован
    val role: UserRole
    // todo add photo
)
