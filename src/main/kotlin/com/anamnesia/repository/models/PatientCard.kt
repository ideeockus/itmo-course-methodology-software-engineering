package com.anamnesia.repository.models

import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import kotlinx.serialization.Serializable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*

typealias PhoneNumber = String

@Serializable
data class PatientFamiliar(
    val name: String,
    val homePhone: PhoneNumber?,
    val workPhone: PhoneNumber?,
    val homeAddress: String?,
    val workAddress: String?,
)

@Serializable
data class PatientCard(
    val name: String,
    val age: Int?,
    val phone: PhoneNumber,
    val address: String?,
    val token: String,
//    val familiars: List<Int>, // List of familiars ids
)

class PatientCardRepository(private val database: Database) {

//    object PatientFamiliars: IntIdTable() {
//        val firstname = varchar("firstname", 50)
//        val lastname = varchar("lastname", 50)
//    }
//    class Actor(id: EntityID<Int>): IntEntity(id) {
//        companion object : IntEntityClass<Actor>(PatientFamiliars)
//        var firstname by PatientFamiliars.firstname
//        var lastname by PatientFamiliars.lastname
//    }

    object PatientFamiliarTable : Table() {
        val id = integer("id").autoIncrement()
        val name = varchar("name", length = 50)
        val homePhone = varchar("homePhone", length = 15)
        val workPhone = varchar("workPhone", length = 15)
        val homeAddress = varchar("homeAddress", length = 50)
        val workAddress = varchar("workAddress", length = 50)
        override val primaryKey = PrimaryKey(id)
    }

    object PatientFamiliarRelationTable : IntIdTable() {
        val familiar = integer("familiar").references(PatientFamiliarTable.id)
        val patient = integer("patient").references(PatientCardTable.id)
    }

    object PatientCardTable : Table() {
        val id = integer("id").autoIncrement()
        val name = varchar("name", length = 50)
        val age = integer("age").nullable()
        val phone = varchar("phone", length = 15)
        val address = varchar("address", length = 50).nullable()
        val token = varchar("token", length = 50)

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(PatientCardTable)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(card: PatientCard): Int = dbQuery {
        PatientCardTable.insert {
            it[name] = card.name
            it[age] = card.age
            it[phone] = card.phone
            it[address] = card.address
            it[token] = card.token
        }[PatientCardTable.id]
    }

//    fun createBlocking(card: PatientCard) {
//        PatientCards.insert {
//            it[name] = card.name
//            it[age] = card.age
//        }[PatientCards.id]
//    }

    suspend fun read(id: Int): PatientCard? {
        return dbQuery {
            PatientCardTable.select { PatientCardTable.id eq id }
                .map {
                    PatientCard(
                        it[PatientCardTable.name],
                        it[PatientCardTable.age],
                        it[PatientCardTable.phone],
                        it[PatientCardTable.address],
                        it[PatientCardTable.token],
                    )
                }
                .singleOrNull()
        }
    }

    suspend fun getByToken(token: String): PatientCard? {
        return dbQuery {
            PatientCardTable.select { PatientCardTable.token eq token }
                .map {
                    PatientCard(
                        it[PatientCardTable.name],
                        it[PatientCardTable.age],
                        it[PatientCardTable.phone],
                        it[PatientCardTable.address],
                        it[PatientCardTable.token],
                    )
                }
                .singleOrNull()
        }
    }

    suspend fun update(id: Int, card: PatientCard) {
        dbQuery {
            PatientCardTable.update({ PatientCardTable.id eq id }) {
                it[name] = card.name
                it[age] = card.age
                it[phone] = card.phone
                it[address] = card.address
//                it[familiars] = card.familiars
            }
        }
    }

    suspend fun delete(id: Int) {
        dbQuery {
            PatientCardTable.deleteWhere { PatientCardTable.id.eq(id) }
        }
    }
}