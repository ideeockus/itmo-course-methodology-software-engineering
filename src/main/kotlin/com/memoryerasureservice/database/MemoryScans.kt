package com.memoryerasureservice.database

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object MemoryScans : IntIdTable() {
    val patientId = reference("patient_id", Patients)
    val dateTime = datetime("date_time")
    val memoryItem = varchar("memory_item", 255)
    val activityData = text("activity_data")
}
