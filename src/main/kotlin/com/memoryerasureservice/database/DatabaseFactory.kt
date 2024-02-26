package com.memoryerasureservice.database

import org.jetbrains.exposed.sql.Database
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

object DatabaseFactory {
    fun init() {
        Database.connect(hikari())
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.postgresql.Driver"
        config.jdbcUrl = "jdbc:postgresql://localhost:5432/memoryerasureservice"
        config.username = "user"
        config.password = "password"
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        return HikariDataSource(config)
    }
}
