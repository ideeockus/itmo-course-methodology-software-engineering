package com.memoryerasureservice.model

import java.time.LocalDateTime

data class Patient(
    val id: Int,
    val name: String,
    val phone: String,
    val email: String?,
    val appointmentDate: LocalDateTime
)