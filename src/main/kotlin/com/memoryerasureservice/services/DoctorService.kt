package com.memoryerasureservice.services

import com.memoryerasureservice.database.Doctors
import com.memoryerasureservice.model.Doctor
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class DoctorService {

    fun addDoctor(name: String, specialty: String): Doctor = transaction {
        // Вставка нового доктора в базу данных и возврат созданного объекта
        val doctorId = Doctors.insertAndGetId { row ->
            row[Doctors.name] = name
            row[Doctors.specialty] = specialty
        }
        getDoctorById(doctorId.value)!!
    }

    fun getDoctorById(id: Int): Doctor? = transaction {
        // Поиск доктора по идентификатору
        Doctors.select { Doctors.id eq id }
            .mapNotNull { toDoctor(it) }
            .singleOrNull()
    }

    fun updateDoctor(id: Int, name: String?, specialty: String?): Doctor? = transaction {
        // Обновление информации о докторе
        Doctors.update({ Doctors.id eq id }) { row ->
            name?.let { row[Doctors.name] = it }
            specialty?.let { row[Doctors.specialty] = it }
        }
        getDoctorById(id)
    }

    private fun toDoctor(row: ResultRow): Doctor =
        Doctor(
            id = row[Doctors.id].value,
            name = row[Doctors.name],
            specialty = row[Doctors.specialty]
        )
}
