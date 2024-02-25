package com.memoryerasureservice

import com.memoryerasureservice.api.*
import com.memoryerasureservice.database.DatabaseFactory
import com.memoryerasureservice.services.ContactService
import com.memoryerasureservice.services.DoctorService
import com.memoryerasureservice.services.MemoryScanService
import com.memoryerasureservice.services.PatientService
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(ContentNegotiation) {
            json()
        }
        DatabaseFactory.init()

        routing {
            patientApi(PatientService())
            doctorApi(DoctorService())
        }

        val memoryScanService = MemoryScanService()
        registerMemoryScanRoutes(memoryScanService)

        val contactService = ContactService()
        registerContactRoutes(contactService)

    }.start(wait = true)
}
