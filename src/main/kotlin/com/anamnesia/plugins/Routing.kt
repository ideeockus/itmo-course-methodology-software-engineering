package com.anamnesia.plugins

import com.anamnesia.repository.createPatientCard
import com.anamnesia.repository.database
import com.anamnesia.repository.fillTimeSlot
import com.anamnesia.repository.getTimeSlots
import com.anamnesia.repository.models.FamiliarTransaction
import com.anamnesia.repository.models.FamiliarTransactionRepository
import com.anamnesia.repository.models.PatientCardRepository
import com.anamnesia.requests.*
import io.ktor.client.request.forms.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset


fun Application.configureRouting() {
    
    routing {
//        openAPI(path="openapi", swaggerFile = "openapi/documentation.yaml")
        get("/") {
            call.respondText("404 Sorry Not Found")
        }

        // working with card
        post("/create_request") {
            val req = call.receive<CreateRequestReq>()
            val newUserToken = genUserToken()
            val new_card_id = createPatientCard(req.name, req.phone, req.email, newUserToken)
            val isTimeReserved = fillTimeSlot(req.date, req.time)

            println("new card id ${new_card_id}")
            println(req)
            println("hehe")
            call.respond(CreateRequestResp(new_card_id, newUserToken))
        }

        post("/get_patient_card") {
            val patientRepo = PatientCardRepository(database)

            val req = call.receive<GetPatientCardReq>()

            val patientCard = runBlocking {
                return@runBlocking patientRepo.getByToken(req.patientToken)
            }

            call.respond(GetPatientCardResp(patientCard))
        }

        post("/edit_patient_card") {
            val patientRepo = PatientCardRepository(database)

            val req = call.receive<EditPatientCardReq>()

            val patientCard = runBlocking {
//                val patientId = patientRepo.getByToken(req.patientToken)

                patientRepo.update(req.patientId, req.patientCard)

                return@runBlocking patientRepo.read(req.patientId)
            }

            call.respond(EditPatientCardResp(patientCard))
        }

        post("/add_patient_familiar") {
            val patientRepo = PatientCardRepository(database)

            val req = call.receive<AddPatientFamiliarReq>()

            val familiarId = runBlocking {
                return@runBlocking patientRepo.addFamiliar(req.patientId, req.familiar)
            }

            call.respond(AddPatientFamiliarResp(familiarId))
        }

        // time slots
        post ( "/fill_time_slot" ) {
            val req = call.receive<FillTimeSlotReq>()

            val timeslots = fillTimeSlot(req.date, req.time)
            call.respond(FillTimeSlotResp(true))
        }
        post("/get_time_slots") {
            val req = call.receive<GetTimeSlotsReq>()

            val timeslots = getTimeSlots(req.doctorId, req.date)
            call.respond(GetTimeSlotsResp(timeslots))
        }


        // familiars transaction history
        post ( "/add_familiar_transaction" ) {
            val req = call.receive<AddFamiliarTransactionReq>()
            val transactionRepo = FamiliarTransactionRepository(database)

            runBlocking {
                return@runBlocking transactionRepo.addTransaction(
                    FamiliarTransaction(
                        req.familiarId,
//                        LocalDateTime.ofInstant(Instant.ofEpochSecond(req.timestamp), ZoneOffset.UTC),
                        req.timestamp,
                        req.action
                    )
                )
            }

            call.respond(AddFamiliarTransactionResp(true))
        }
        post("/get_familiar_transactions_history") {
            val req = call.receive<GetFamiliarTransactionHistoryReq>()

            val transactionRepo = FamiliarTransactionRepository(database)

            val history = runBlocking {
                return@runBlocking transactionRepo.getHistory(
                    req.familiarId
                )
            }

            call.respond(GetFamiliarTransactionHistoryResp(history))
        }

        // Static plugin. Try to access `/static/index.html`
        static("/web_app") {
            resources("static")
        }
    }
}

fun genUserToken(): String {
    val TOKEN_LEN = 22
    return getRandomString(TOKEN_LEN)
}

fun getRandomString(length: Int) : String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}