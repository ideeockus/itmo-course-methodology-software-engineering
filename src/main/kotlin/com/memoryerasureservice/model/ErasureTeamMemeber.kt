package com.memoryerasureservice.model

data class ErasureTeamMember(
    val id: Int,
    val memberName: String,
    val role: String,
    val status: String,
    val assignedVehicle: String?
)
