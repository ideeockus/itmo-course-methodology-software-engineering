package com.anamnesia.repository.models

import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import kotlinx.serialization.Serializable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*


@Serializable
data class TimeSlot(
    val doctorId: Int,
    val patientId: Int,
    val time: String,
    val date: String,
)

class TimeSlotsRepository(private val database: Database) {

    object TimeSlotTable : Table() {
        val doctor = integer("doctor")
        val patient = integer("patient")
        val datetime = varchar("datetime", length = 30)
        val date = varchar("date", length = 30)
        val time = varchar("time", length = 30)
        override val primaryKey = PrimaryKey(doctor, datetime)
    }

    init {
        transaction(database) {
            SchemaUtils.create(TimeSlotTable)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(timeSlot: TimeSlot): Boolean = dbQuery {
        TimeSlotTable.insert {
            it[doctor] = timeSlot.doctorId
            it[patient] = timeSlot.patientId
            it[datetime] = timeSlot.date + timeSlot.time
            it[date] = timeSlot.date
            it[time] = timeSlot.time
        }.insertedCount > 0
    }

    suspend fun getSlotsByDoctorAndDate(doctorId: Int, date: String): List<TimeSlot> {
        return dbQuery {
            TimeSlotTable.select { TimeSlotTable.doctor eq doctorId and (TimeSlotTable.date eq date) }
                .map {
                    TimeSlot(
                        it[TimeSlotTable.doctor],
                        it[TimeSlotTable.patient],
                        it[TimeSlotTable.time],
                        it[TimeSlotTable.date],
                    )
                }
        }
    }

//    suspend fun update(id: Int, card: PatientCard) {
//        dbQuery {
//            PatientCardTable.update({ PatientCardTable.id eq id }) {
//                it[name] = card.name
//                it[age] = card.age
//                it[phone] = card.phone
//                it[address] = card.address
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