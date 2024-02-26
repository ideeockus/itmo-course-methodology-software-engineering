package com.memoryerasureservice.database

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object Appointments : IntIdTable() {
    val patientId = reference("patient_id", Patients)
    val doctorId = reference("doctor_id", Doctors)
    val appointmentDate = datetime("appointment_date")
    val status = varchar("status", 20)  // Например, "planned", "completed", "cancelled"
}
