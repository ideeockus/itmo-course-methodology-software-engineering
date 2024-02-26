package com.memoryerasureservice.api

import com.memoryerasureservice.services.DoctorService
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import kotlinx.serialization.Serializable

//fun Application.registerDoctorRoutes() {
//    routing {
//        // маршруты для работы с докторами
//    }
//}

fun Route.doctorApi(doctorService: DoctorService) {
    route("/doctors") {
        post("/apply") {
            val request = call.receive<AddDoctorRequest>()
            val doctor = doctorService.addDoctor(request.name, request.specialty)
            call.respond(HttpStatusCode.Created, doctor)
        }
    }
}


@Serializable
data class AddDoctorRequest(val name: String, val specialty: String)