package com.memoryerasureservice.services

import com.memoryerasureservice.database.MemoryScans
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class MemoryScanService {
    fun recordMemoryScan(patientId: Int, memoryItem: String, activityData: String) {
        transaction {
            MemoryScans.insert {
                it[MemoryScans.patientId] = patientId
                it[MemoryScans.dateTime] = java.time.LocalDateTime.now()
                it[MemoryScans.memoryItem] = memoryItem
                it[MemoryScans.activityData] = activityData
            }
        }
    }
}
