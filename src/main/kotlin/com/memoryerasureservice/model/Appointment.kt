package com.memoryerasureservice.model

import java.time.LocalDateTime

data class Appointment(
    val id: Int,
    val patientId: Int,
    val doctorId: Int,
    val appointmentDate: LocalDateTime,
    val status: String
)
