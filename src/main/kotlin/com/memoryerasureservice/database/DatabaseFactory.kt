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
                Users,
                UserTokens,
            )
        }
    }

    private fun hikari(): HikariDataSource {
        val dbHost = System.getenv("DB_HOST") ?: "pg"
        val dbPort = System.getenv("DB_PORT") ?: "5432"
        val dbName = System.getenv("DB_NAME") ?: "studs"
        val dbUser = System.getenv("DB_USER") ?: "s370037"
        val dbPassword = System.getenv("DB_PASSWORD") ?: "LMvVLrpaX9nWi9Zm"


        val config = HikariConfig()
        config.driverClassName = "org.postgresql.Driver"
        config.jdbcUrl = "jdbc:postgresql://$dbHost:$dbPort/$dbName"
        config.username = dbUser
        config.password = dbPassword
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        return HikariDataSource(config)
    }
}
