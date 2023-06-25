package com.anamnesia.repository.models

import kotlinx.coroutines.Dispatchers
//import kotlinx.datetime.LocalDateTime
//import java.time.LocalDateTime
//import kotlinx.datetime.serializers.LocalDateTimeIso8601Serializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
//import java.time.Instant
//import java.time.ZoneOffset

enum class FamiliarAction {
    PHONE_CALL,
    HOME_VISIT,
    CALL_EMPLOYER,
    WORK_VISIT,
    NOTIFIED,
}

@Serializable
data class FamiliarTransaction(
    val familiarId: Int,
//    @Serializable(with = LocalDateTimeIso8601Serializer::class)
//    val datetime: LocalDateTime,
    val timestamp: Long,
    val action: FamiliarAction,
)


class FamiliarTransactionRepository(private val database: Database) {

    object FamiliarTransactionTable : IntIdTable() {
        val patientId = integer("patientId")
        val timestamp = long("timestamp")
        val action = enumerationByName("action", 35, FamiliarAction::class)
    }

    init {
        transaction(database) {
            SchemaUtils.create(FamiliarTransactionTable)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun addTransaction(transaction: FamiliarTransaction) = dbQuery {
        FamiliarTransactionTable.insert {
            it[patientId] = transaction.familiarId
            it[timestamp] = transaction.timestamp
            it[action] = transaction.action
        }
    }

    suspend fun getHistory(patientId: Int): List<FamiliarTransaction> {
        return dbQuery {
            FamiliarTransactionTable.select { FamiliarTransactionTable.patientId eq patientId }
                .map {
                    FamiliarTransaction(
                        it[FamiliarTransactionTable.patientId],
//                        LocalDateTime.ofInstant(Instant.ofEpochSecond(it[FamiliarTransactionTable.timestamp]), ZoneOffset.UTC),
                        it[FamiliarTransactionTable.timestamp],
                        it[FamiliarTransactionTable.action],
                    )
                }
        }
    }
}