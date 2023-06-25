package com.anamnesia.repository.models

import com.anamnesia.repository.models.PatientCardRepository.PatientFamiliarTable.autoIncrement
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import kotlinx.serialization.Serializable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import java.time.LocalDateTime

typealias PhoneNumber = String

enum class PatientState {
    Stage1,
    Stage2,
    Stage3,
    Stage4,
}

enum class FamiliarState {
    NotifiedSuccessful,
    NotProcessed,
    FailedToNotify,
    ProcessedUnsuccessful,
}

@Serializable
data class PatientFamiliar(
    val familiarId: Int,
    val name: String,
    val homePhone: PhoneNumber?,
    val workPhone: PhoneNumber?,
    val homeAddress: String?,
    val workAddress: String?,
    val state: FamiliarState,
)

@Serializable
data class PatientCard(
    val id: Int,
    val name: String,
    val age: Int?,
    val phone: PhoneNumber,
    val address: String?,
    val token: String,
    val familiars: List<PatientFamiliar>, // List of familiars ids
    val state: PatientState,
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
        val state = enumerationByName("state", 25, FamiliarState::class)
        override val primaryKey = PrimaryKey(id)
    }

    object PatientFamiliarRelationTable : IntIdTable() {
//    object PatientFamiliarRelationTable : Table() {
//        val id = integer("id").autoIncrement()
        val familiar = integer("familiar").references(PatientFamiliarTable.id)
        val patient = integer("patient").references(PatientCardTable.id)
//        override val primaryKey = PrimaryKey(id)
//        override val primaryKey = PrimaryKey(familiar, patient)
    }

    object PatientCardTable : Table() {
        val id = integer("id").autoIncrement()
        val name = varchar("name", length = 50)
        val age = integer("age").nullable()
        val phone = varchar("phone", length = 15)
        val address = varchar("address", length = 50).nullable()
        val token = varchar("token", length = 50)
        val state = enumerationByName("state", 25, PatientState::class)

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(PatientCardTable)
            SchemaUtils.create(PatientFamiliarRelationTable)
            SchemaUtils.create(PatientFamiliarTable)
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
            it[state] = card.state
        }[PatientCardTable.id]
    }

    suspend fun addFamiliar(patientId: Int, familiar: PatientFamiliar): Int = dbQuery {
        val familiarId = PatientFamiliarTable.insert {
            it[name] = familiar.name
            it[homePhone] = familiar.homePhone ?: ""
            it[workPhone] = familiar.workPhone ?: ""
            it[homeAddress] = familiar.homeAddress ?: ""
            it[workAddress] = familiar.workPhone ?: ""
            it[state] = familiar.state
        }[PatientFamiliarTable.id]

        PatientFamiliarRelationTable.insert {
            it[PatientFamiliarRelationTable.patient] = patientId
            it[PatientFamiliarRelationTable.familiar] = familiarId
        }

        familiarId
    }

//    fun createBlocking(card: PatientCard) {
//        PatientCards.insert {
//            it[name] = card.name
//            it[age] = card.age
//        }[PatientCards.id]
//    }

    suspend fun getPatientFamiliars(patientId: Int): List<PatientFamiliar> {
        val familiarsIds = dbQuery {
            PatientFamiliarRelationTable.select(PatientFamiliarRelationTable.patient eq patientId) }
            .map {
                it[PatientFamiliarRelationTable.familiar]
            }

        val familiars = dbQuery {
            PatientFamiliarTable.select(PatientFamiliarTable.id inList familiarsIds) }
            .map {
                PatientFamiliar(
                    it[PatientFamiliarTable.id],
                    it[PatientFamiliarTable.name],
                    it[PatientFamiliarTable.homePhone],
                    it[PatientFamiliarTable.workPhone],
                    it[PatientFamiliarTable.homeAddress],
                    it[PatientFamiliarTable.workAddress],
                    it[PatientFamiliarTable.state],
                )
            }.toList()

        return familiars
    }

    suspend fun read(patientId: Int): PatientCard? {
        return dbQuery {
            PatientCardTable.select { PatientCardTable.id eq patientId }
                .map {
                    PatientCard(
                        it[PatientCardTable.id],
                        it[PatientCardTable.name],
                        it[PatientCardTable.age],
                        it[PatientCardTable.phone],
                        it[PatientCardTable.address],
                        it[PatientCardTable.token],
                        getPatientFamiliars(patientId),
                        it[PatientCardTable.state],
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
                        it[PatientCardTable.id],
                        it[PatientCardTable.name],
                        it[PatientCardTable.age],
                        it[PatientCardTable.phone],
                        it[PatientCardTable.address],
                        it[PatientCardTable.token],
                        getPatientFamiliars(it[PatientCardTable.id]),
                        it[PatientCardTable.state],
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
                it[state] = card.state
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