package com.anamnesia.repository

import com.anamnesia.common.models.TimeSlotState
import com.anamnesia.plugins.UserService
import com.anamnesia.repository.models.PatientCard
import com.anamnesia.repository.models.PatientCardRepository
import com.anamnesia.repository.models.TimeSlot
import com.anamnesia.repository.models.TimeSlotsRepository
import com.anamnesia.requests.CreateRequestReq
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlinx.coroutines.runBlocking


val database = Database.connect(
//    url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
//    url = "jdbc:sqlite:database.db",
    url = "jdbc:postgresql://localhost:5432/mse_lab_db",
    driver = "org.sqlite.JDBC",
    user = "mse_lab_user",
//    driver = "org.h2.Driver",
    password = "pass123",
)


fun createPatientCard(name: String, phone: String, token: String): Int {
    val patientRepo = PatientCardRepository(database)

    val newCard = PatientCard(name, null, phone, null, token)

    return runBlocking {
        val newCardId = patientRepo.create(newCard)
        return@runBlocking newCardId
    }
}

fun fillTimeSlot(date: String, time: String): Boolean {
    val timeSlotRepo = TimeSlotsRepository(database)

    val timeSlot = TimeSlot(0, 0, time.trim(), date.trim())

    return runBlocking {
        return@runBlocking timeSlotRepo.create(timeSlot)
    }
}

fun getTimeSlots(doctorId: Int, date: String): HashMap<String, TimeSlotState> {
    val timeSlotRepo = TimeSlotsRepository(database)

    val slots = listOf(
        "10:00",
        "12:00",
        "14:00",
        "16:00",
        "10:30",
        "12:30",
        "14:30",
        "16:30",
        "11:00",
        "13:00",
        "15:00",
        "17:00",
        "11:30",
        "13:30",
        "15:30",
        "17:30",
    )

    val timeslots: HashMap<String, TimeSlotState> = HashMap(slots.associateWith { TimeSlotState.FREE })

    runBlocking {
        val db_timeslots = timeSlotRepo.getSlotsByDoctorAndDate(doctorId, date)

        db_timeslots.forEach {
            timeslots[it.time] = TimeSlotState.FULL
        }
    }

    return timeslots
}
