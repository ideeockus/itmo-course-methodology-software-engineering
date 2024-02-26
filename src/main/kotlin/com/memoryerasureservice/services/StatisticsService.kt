package com.memoryerasureservice.services

import com.memoryerasureservice.database.Statistics
import com.memoryerasureservice.model.Statistic
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class StatisticsService {
    fun getAllStatistics(): List<Statistic> = transaction {
        Statistics.selectAll().map { toStatistic(it) }
    }

    fun updateStatistic(key: String, value: String) = transaction {
        Statistics.update({ Statistics.key eq key }) {
            it[Statistics.value] = value
        }
    }

    fun addStatistic(key: String, value: String) = transaction {
        Statistics.insert {
            it[Statistics.key] = key
            it[Statistics.value] = value
        }
    }

    fun incrementStatistic(key: String, incrementBy: Long = 1) = transaction {
        val current = Statistics.select { Statistics.key eq key }
            .mapNotNull { it[Statistics.value].toLongOrNull() }
            .singleOrNull() ?: 0L

        val newValue = current + incrementBy

        if (current == 0L) {
            Statistics.insert {
                it[Statistics.key] = key
                it[Statistics.value] = newValue.toString()
            }
        } else {
            Statistics.update({ Statistics.key eq key }) {
                it[Statistics.value] = newValue.toString()
            }
        }
    }

    private fun toStatistic(row: ResultRow): Statistic =
        Statistic(
            key = row[Statistics.key],
            value = row[Statistics.value]
        )
}
