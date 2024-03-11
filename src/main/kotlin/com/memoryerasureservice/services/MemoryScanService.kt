package com.memoryerasureservice.services

import com.memoryerasureservice.database.MemoryScans
import com.memoryerasureservice.database.Patients
import com.memoryerasureservice.model.MemoryScan
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.*

class MemoryScanService {
    fun recordMemoryScan(patientId: Int, memoryItem: String, activityData: String) {
        val memoryScanId = transaction {
            MemoryScans.insert {
                it[MemoryScans.patientId] = patientId
                it[MemoryScans.dateTime] = java.time.LocalDateTime.now()
                it[MemoryScans.memoryItem] = memoryItem
                it[MemoryScans.activityData] = activityData
            } get MemoryScans.id
        }

        transaction {
            Patients.update({ Patients.id eq patientId }) {
                it[Patients.memoryScanId] = memoryScanId.value
            }
        }
    }
}

fun getMemoryScanById(id: UUID): MemoryScan? = transaction {
    MemoryScans.select { MemoryScans.id eq id }
        .mapNotNull { row ->
            MemoryScan(
                id = row[MemoryScans.id].value,
                patientId = row[MemoryScans.patientId].value,
                dateTime = row[MemoryScans.dateTime],
                memoryItem = row[MemoryScans.memoryItem],
                activityData = row[MemoryScans.activityData]
            )
        }.singleOrNull()
}
