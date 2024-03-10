package com.memoryerasureservice.database

import com.memoryerasureservice.model.EquipmentStatus
import com.memoryerasureservice.model.PatientState
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.javatime.date

object Equipment : IntIdTable() {
    val name = varchar("name", 255)
    val type = varchar("type", 50)
    val status = enumerationByName("status", 50, EquipmentStatus::class)
    val location = varchar("location", 255)
    val maintenanceDate = date("maintenance_date").nullable()
    val serviceLife = integer("service_life") // Срок службы в месяцах
}
