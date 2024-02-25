package com.memoryerasureservice.api

import com.memoryerasureservice.model.Patient
import com.memoryerasureservice.services.PatientService
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

        route("/patients") {
            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid patient ID")
                    return@get
                }
                val patient = patientService.getPatientById(id)
                if (patient == null) {
                    call.respond(HttpStatusCode.NotFound, "Patient not found")
                } else {
                    call.respond(patient)
                }
            }

            post("/{id}/update") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid patient ID")
                    return@post
                }
                val patientData = call.receive<Patient>()
                val updatedPatient = patientService.updatePatient(id, patientData)
                if (updatedPatient == null) {
                    call.respond(HttpStatusCode.NotFound, "Patient not found")
                } else {
                    call.respond(HttpStatusCode.OK, updatedPatient)
                }
            }
        }
    }
}

@Serializable
data class ApplyRequest(val name: String, val phone: String, val email: String?, val appointmentDate: String)
