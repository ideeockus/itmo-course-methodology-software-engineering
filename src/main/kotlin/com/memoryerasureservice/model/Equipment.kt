package com.memoryerasureservice.model

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
enum class EquipmentStatus {
    Available,
    Reserved,
    Corrupted,
    NotAvailable,
    Busy,
}

@Serializable
data class EquipmentData(
    val id: Int,
    val name: String,
    val type: String,
    val status: EquipmentStatus,
    val location: String?,
    @Serializable(with = LocalDateSerializer::class)
    val maintenanceDate: LocalDate?,
    val serviceLife: Int?
)

