package com.memoryerasureservice.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class MemoryScan(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val patientId: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    val dateTime: LocalDateTime,
    val memoryItem: String,
    val activityData: String
)
