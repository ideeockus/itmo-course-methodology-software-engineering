package com.memoryerasureservice.services

import FamiliarService
import com.memoryerasureservice.api.ApplyRequest
import com.memoryerasureservice.api.CreatePatientAppointment
import com.memoryerasureservice.database.Patients
import com.memoryerasureservice.model.Patient
import com.memoryerasureservice.model.PatientState
import com.memoryerasureservice.utils.parseLocalDateTimeFromString
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

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

    fun createAppointment(req: CreatePatientAppointment): Patient {
        val patientId = transaction {
            val appointmentDateTime = req.appointmentDate + req.appointmentTime
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val dateTime = LocalDateTime.parse(appointmentDateTime.trim(), formatter)

            Patients.insert { row ->
                row[Patients.name] = req.name
                row[Patients.phone] = req.phone
                row[Patients.email] = req.email
                row[Patients.appointmentDate] = dateTime
                row[Patients.userToken] = UUID.randomUUID()
                row[Patients.state] = PatientState.Stage1
            } get Patients.id
        }

        return getPatientById(patientId.value)
    }

    fun getPatientById(id: Int): Patient = transaction {
        // Получение пациента по ID
        Patients.select { Patients.id eq id }
            .map { toPatient(it) }
            .first()
    }

    fun getPatientByToken(id: UUID): Patient = transaction {
        Patients.select { Patients.userToken eq id }
            .map { toPatient(it) }
            .first()
    }

    fun updatePatient(id: Int, patientData: Patient): Patient? = transaction {
        // Проверка, существует ли пациент с данным ID
        Patients.select { Patients.id eq id }
            .singleOrNull() ?: return@transaction null

        // Обновление данных пациента
        Patients.update({ Patients.id eq id }) {
            it[name] = patientData.name
            it[phone] = patientData.phone
            it[email] = patientData.email
        }

        // Возвращение обновленного пациента
        getPatientById(id)
    }

    private fun toPatient(row: ResultRow): Patient {
        val memoryScanId = row[Patients.memoryScanId]
        val erasureSessionId = row[Patients.erasureSessionId]

        val memoryScan = memoryScanId?.let { id ->
            getMemoryScanById(id)
        }

        val erasureSession = erasureSessionId?.let { id ->
            getErasureSessionById(id)
        }

        val patientFamiliars = row[Patients.id].let { id ->
            FamiliarService().getFamiliarsForPatient(id.value)
        }

        return Patient(
            id = row[Patients.id].value,
            name = row[Patients.name],
            phone = row[Patients.phone],
            email = row[Patients.email],
            appointmentDate = row[Patients.appointmentDate],

            age = row[Patients.age],
            address = row[Patients.address],

            memoryScan = memoryScan,
            erasureSession = erasureSession,

            state = row[Patients.state],
            familiars = patientFamiliars,
            userToken = row[Patients.userToken]
        )
    }
}
