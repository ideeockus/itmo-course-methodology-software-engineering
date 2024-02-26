package com.memoryerasureservice.database

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.javatime.date

object Equipment : IntIdTable() {
    val name = varchar("name", 255)
    val type = varchar("type", 50)
    val status = varchar("status", 50) // Например, "available", "in_use"
    val location = varchar("location", 255)
    val maintenanceDate = date("maintenance_date").nullable()
    val serviceLife = integer("service_life") // Срок службы в месяцах
}
