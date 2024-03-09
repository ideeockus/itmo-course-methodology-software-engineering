package com.memoryerasureservice.api

import FamiliarService
import com.memoryerasureservice.model.Familiar
import com.memoryerasureservice.model.FamiliarState
import com.memoryerasureservice.model.Patient
import com.memoryerasureservice.model.PatientState
import com.memoryerasureservice.services.PatientService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.util.UUID

fun Route.patientApi(patientService: PatientService) {
    route("/patients") {
        post("/apply") {
            val request = call.receive<ApplyRequest>()
            val patient = patientService.applyForService(request)
            call.respond(HttpStatusCode.Created, patient)
        }

        post("/create_appointment") {
            val request = call.receive<CreatePatientAppointment>()
            val patient = patientService.createAppointment(request)
            println("create_appointment: patient $patient")
            call.respond(HttpStatusCode.Created, patient)
        }

        get("/get_all_patients") {
            val patients = patientService.getAllPatients()
            call.respond(patients)
        }

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

        post("/by_token") {
            val request = call.receive<GetPatientByToken>()
            val patientToken = UUID.fromString(request.patientToken)
            val patient = patientService.getPatientByToken(patientToken)
            call.respond(HttpStatusCode.Created, patient)
        }

        post("/edit_patient_card") {
            val request = call.receive<UpdatePatientReq>()
            var patient = patientService.getPatientById(request.id)

            patient.name = request.name
            patient.phone = request.phone
            patient.email = request.email
            patient.address = request.address
            patient.age = request.age
            patient.state = request.state

            val updatedPatient = patientService.updatePatient(patient.id, patient)
            if (updatedPatient == null) {
                call.respond(HttpStatusCode.NotFound, "Patient not found")
            } else {
                call.respond(HttpStatusCode.OK, updatedPatient)
            }
        }

        post("/add_patient_familiar") {
            val request = call.receive<AddPatientFamiliarReq>()

            // Создаем объект Familiar на основе данных запроса
            val familiarData = Familiar(
                id = 0,
                name = request.name,
                email = request.email,
                homePhone = request.homePhone,
                workPhone = request.workPhone,
                homeAddress = request.homeAddress,
                workAddress = request.workAddress,
                state = request.state
            )

            // Пытаемся добавить родственника к пациенту
            val newFamiliar = patientService.addFamiliarToPatient(request.patientId, familiarData)

            if (newFamiliar == null) {
                call.respond(HttpStatusCode.BadRequest, "Could not add familiar")
            } else {
                call.respond(HttpStatusCode.Created, newFamiliar)
            }
        }
//        }
    }
}

@Serializable
data class ApplyRequest(
    val name: String,
    val phone: String,
    val email: String?,
    val appointmentDate: String
)

@Serializable
data class CreatePatientAppointment(
    val name: String,
    val phone: String,
    val email: String?,
    val appointmentDate: String,
    val appointmentTime: String,
    val policyChecked: Boolean,
)

@Serializable
data class GetPatientByToken(
    val patientToken: String,
)

@Serializable
data class UpdatePatientReq(
    val id: Int,
    val name: String,
    val phone: String,
    val email: String?,
    val age: Int?,
    val address: String?,

//    val familiars: List<Familiar>,
    val state: PatientState,
)

@Serializable
data class AddPatientFamiliarReq(
    val patientId: Int,
    val name: String,
    val email: String? = null,
    val homePhone: String? = null,
    val workPhone: String? = null,
    val homeAddress: String? = null,
    val workAddress: String? = null,
    val state: FamiliarState
)

/*
*
* name: formData.get("name"),
      email: formData.get("email"),
      phone: formData.get("phone"),
      time: checked_time_slots[0].innerHTML,
      date: date,
* */
