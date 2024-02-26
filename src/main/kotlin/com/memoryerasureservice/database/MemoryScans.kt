package com.memoryerasureservice.database

import com.memoryerasureservice.model.MemoryScan
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object MemoryScans : Table() {
    val id = uuid("id")
    val patientId = reference("patient_id", Patients)
    val dateTime = datetime("date_time")
    val memoryItem = varchar("memory_item", 255)
    val activityData = text("activity_data")
}

fun ResultRow.toMemoryScan() = MemoryScan(
    id = this[MemoryScans.id],
    patientId = this[MemoryScans.patientId].value,
    dateTime = this[MemoryScans.dateTime],
    memoryItem = this[MemoryScans.memoryItem],
    activityData = this[MemoryScans.activityData]
)
