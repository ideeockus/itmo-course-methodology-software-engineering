package com.anamnesia.repository.models

import com.anamnesia.repository.models.PatientCardRepository.PatientFamiliarTable.autoIncrement
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlinx.serialization.Serializable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import java.time.LocalDateTime


enum class WorkerRole {
    ERASER,
    TECHSUPPORT,
    DIRECTOR,
    DOCTOR,
    FAMILIAR_AGENT,
    OTHER,
}

@Serializable
data class WorkerAccount(
    val id: Int,
    val name: String,
    val role: WorkerRole,
)

class WorkerAccountRepository(private val database: Database) {
    object WorkerAccountTable : Table() {
        val id = integer("id").autoIncrement()
        val name = varchar("name", length = 50)
        val role = enumerationByName("role", 50, WorkerRole::class)

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(WorkerAccountTable)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(workerAccount: WorkerAccount): Int = dbQuery {
        WorkerAccountTable.insert {
            it[name] = workerAccount.name
            it[role] = workerAccount.role
        }[WorkerAccountTable.id]
    }

    suspend fun read(workerId: Int): WorkerAccount? {
        return dbQuery {
            WorkerAccountTable.select { WorkerAccountTable.id eq workerId }
                .map {
                    WorkerAccount(
                        it[WorkerAccountTable.id],
                        it[WorkerAccountTable.name],
                        it[WorkerAccountTable.role],
                    )
                }
                .singleOrNull()
        }
    }

    // todo implement update / delete for WorkerAccount
//    suspend fun update(id: Int, card: PatientCard) {
//        dbQuery {
//            PatientCardTable.update({ PatientCardTable.id eq id }) {
//                it[name] = card.name
//                it[age] = card.age
//                it[phone] = card.phone
//                it[address] = card.address
//                it[state] = card.state
////                it[familiars] = card.familiars
//            }
//        }
//    }
//
//    suspend fun delete(id: Int) {
//        dbQuery {
//            PatientCardTable.deleteWhere { PatientCardTable.id.eq(id) }
//        }
//    }
}