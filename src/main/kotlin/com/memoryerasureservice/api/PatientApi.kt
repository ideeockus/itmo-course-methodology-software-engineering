package com.memoryerasureservice.api

import com.memoryerasureservice.service.PatientService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Route.patientApi(patientService: PatientService) {
    route("/patients") {
        post("/apply") {
            val request = call.receive<ApplyRequest>()
            val patient = patientService.applyForService(request)
            call.respond(HttpStatusCode.Created, patient)
        }
    }
}

@Serializable
data class ApplyRequest(val name: String, val phone: String, val email: String?, val appointmentDate: String)
