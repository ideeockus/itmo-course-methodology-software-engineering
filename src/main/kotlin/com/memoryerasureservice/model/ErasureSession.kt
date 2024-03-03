package com.memoryerasureservice.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class ErasureSession(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val patientId: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    val startTime: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val endTime: LocalDateTime?,
    val status: String
)
