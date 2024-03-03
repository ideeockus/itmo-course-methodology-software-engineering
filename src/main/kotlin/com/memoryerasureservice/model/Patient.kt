package com.memoryerasureservice.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID


@Serializable
enum class PatientState {
    Stage1,
    Stage2,
    Stage3,
    Stage4,
}

@Serializable
data class Patient(
    val id: Int,
    var name: String,
    var phone: String,
    var email: String?,
    @Serializable(with = LocalDateTimeSerializer::class)
    val appointmentDate: LocalDateTime?,

    var age: Int?,
    var address: String?,

    val memoryScan: MemoryScan?,
    val erasureSession: ErasureSession?,

    val familiars: List<Familiar>,
    var state: PatientState,
    @Serializable(with = UUIDSerializer::class)
    val userToken: UUID?,
)

