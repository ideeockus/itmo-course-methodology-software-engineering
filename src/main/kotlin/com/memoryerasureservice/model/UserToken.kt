package com.memoryerasureservice.model

import java.time.LocalDateTime

data class UserToken(
    val userId: Int,
    val token: String,
    val createdAt: LocalDateTime,
    val expiresAt: LocalDateTime?
)
