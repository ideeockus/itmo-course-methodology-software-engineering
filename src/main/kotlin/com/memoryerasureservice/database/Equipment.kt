package com.memoryerasureservice.database

import org.jetbrains.exposed.dao.id.IntIdTable

object Equipment : IntIdTable() {
    val name = varchar("name", 255)
    val type = varchar("type", 50)
    val status = varchar("status", 50) // Например, "available", "in_use", "maintenance"
}
