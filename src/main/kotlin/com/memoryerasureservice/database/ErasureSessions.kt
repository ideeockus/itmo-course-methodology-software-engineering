package com.memoryerasureservice.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object ErasureSessions : Table() {
    val id = MemoryScans.uuid("id")
    val patientId = reference("patient_id", Patients)
    val startTime = datetime("start_time")
    val endTime = datetime("end_time").nullable()
    val status = varchar("status", 50) // Например, "planned", "in_progress", "completed", "failed"
}
