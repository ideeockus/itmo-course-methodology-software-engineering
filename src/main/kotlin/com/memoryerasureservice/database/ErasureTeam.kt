package com.memoryerasureservice.database

import org.jetbrains.exposed.dao.id.IntIdTable

object ErasureTeam : IntIdTable() {
    val memberName = varchar("member_name", 255)
    val role = varchar("role", 50) // Например, "lead", "technician", "assistant"
    val status = varchar("status", 50) // Например, "ready", "on_route", "at_location"
    val assignedVehicle = varchar("assigned_vehicle", 255).nullable() // ID или название транспортного средства
}
