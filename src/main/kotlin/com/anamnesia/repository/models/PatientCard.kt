package com.anamnesia.repository.models

import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import kotlinx.serialization.Serializable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*

@Serializable
data class PatientCard(
    val name: String,
    val age: Int,
)

class PatientCardRepository(private val database: Database) {
    object PatientCards : Table() {
        val id = integer("id").autoIncrement()
        val name = varchar("name", length = 50)
        val age = integer("age")

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(PatientCards)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(card: PatientCard): Int = dbQuery {
        PatientCards.insert {
            it[name] = card.name
            it[age] = card.age
        }[PatientCards.id]
    }

//    fun createBlocking(card: PatientCard) {
//        PatientCards.insert {
//            it[name] = card.name
//            it[age] = card.age
//        }[PatientCards.id]
//    }

    suspend fun read(id: Int): PatientCard? {
        return dbQuery {
            PatientCards.select { PatientCards.id eq id }
                .map { PatientCard(it[PatientCards.name], it[PatientCards.age]) }
                .singleOrNull()
        }
    }

    suspend fun update(id: Int, user: PatientCard) {
        dbQuery {
            PatientCards.update({ PatientCards.id eq id }) {
                it[name] = user.name
                it[age] = user.age
            }
        }
    }

    suspend fun delete(id: Int) {
        dbQuery {
            PatientCards.deleteWhere { PatientCards.id.eq(id) }
        }
    }
}