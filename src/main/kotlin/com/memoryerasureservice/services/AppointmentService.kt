package com.memoryerasureservice.services

import com.memoryerasureservice.database.Appointments
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class AppointmentService {
    fun scheduleAppointment(patientId: Int, doctorId: Int, dateTime: LocalDateTime): Int = transaction {
        val appointmentId = Appointments.insert { row ->
            row[Appointments.patientId] = patientId
            row[Appointments.doctorId] = doctorId
            row[Appointments.appointmentDate] = dateTime
            row[Appointments.status] = "scheduled"
        } get Appointments.id

        appointmentId.value
    }
}
