// Файл: src/main/kotlin/com/memoryerasureservice/service/PatientService.kt
package com.memoryerasureservice.service

import com.memoryerasureservice.api.ApplyRequest
import com.memoryerasureservice.database.Patients
import com.memoryerasureservice.model.Patient
import com.memoryerasureservice.utils.parseLocalDateTimeFromString
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class PatientService {

    fun createPatient(name: String, phone: String, email: String?, appointmentDateTime: LocalDateTime): Patient {
        val patientId = transaction {
            // Вставка нового пациента в базу данных
            Patients.insert { row ->
                row[Patients.name] = name
                row[Patients.phone] = phone
                row[Patients.email] = email
                row[Patients.appointmentDate] = appointmentDateTime
            } get Patients.id
        }

        return getPatientById(patientId.value)
    }

    // same as createPatient
    fun applyForService(req: ApplyRequest): Patient {
        val patientId = transaction {
            // Вставка нового пациента в базу данных
            Patients.insert { row ->
                row[Patients.name] = name
                row[Patients.phone] = phone
                row[Patients.email] = email
                row[Patients.appointmentDate] = parseLocalDateTimeFromString(req.appointmentDate)
            } get Patients.id
        }

        return getPatientById(patientId.value)
    }

    private fun getPatientById(id: Int): Patient = transaction {
        // Получение пациента по ID
        Patients.select { Patients.id eq id }
            .map { toPatient(it) }
            .first()
    }

    private fun toPatient(row: ResultRow): Patient =
        Patient(
            id = row[Patients.id].value,
            name = row[Patients.name],
            phone = row[Patients.phone],
            email = row[Patients.email],
            appointmentDate = row[Patients.appointmentDate]
        )
}
