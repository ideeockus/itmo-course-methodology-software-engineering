package com.memoryerasureservice.database

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object Patients : IntIdTable() {
    val name = varchar("name", 255)
    val phone = varchar("phone", 20)
    val email = varchar("email", 255).nullable()
    val appointmentDate = datetime("appointment_date")
}
