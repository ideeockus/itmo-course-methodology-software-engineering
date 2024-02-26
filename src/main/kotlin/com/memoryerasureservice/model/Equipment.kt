package com.memoryerasureservice.model

import java.time.LocalDate

data class EquipmentData(
    val id: Int,
    val name: String,
    val type: String,
    val status: String,
    val location: String?,
    val maintenanceDate: LocalDate?,
    val serviceLife: Int?
)

