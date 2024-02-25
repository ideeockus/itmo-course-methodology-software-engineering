package com.memoryerasureservice.model

import java.time.LocalDateTime
import java.util.UUID

data class MemoryScan(
    val id: UUID,
    val patientId: Int,
    val dateTime: LocalDateTime,
    val memoryItem: String,
    val activityData: String
)
