package com.memoryerasureservice.model

import java.time.LocalDateTime
import java.util.UUID

data class ErasureSession(
    val id: UUID,
    val patientId: Int,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime?,
    val status: String
)
