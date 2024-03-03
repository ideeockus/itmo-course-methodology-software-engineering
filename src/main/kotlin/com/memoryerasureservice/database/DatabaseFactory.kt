package com.memoryerasureservice.database

import org.jetbrains.exposed.sql.Database
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        Database.connect(hikari())
        createTables()
    }

    private fun createTables() {
        transaction {
            SchemaUtils.create(
                Patients,
                ContactCards,
                ContactAttempts,
                Appointments,
                Doctors,
                MemoryScans,
                Equipment,
                ErasureSessions,
                ErasureTeam,
                Statistics,
                PatientFamiliarRelationTable,
                Familiars,
            )
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.postgresql.Driver"
        config.jdbcUrl = "jdbc:postgresql://localhost:5434/mpi_db"
        config.username = "mpi_user"
        config.password = "pass123"
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        return HikariDataSource(config)
    }
}
