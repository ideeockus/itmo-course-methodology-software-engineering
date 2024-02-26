package com.memoryerasureservice.api

import com.memoryerasureservice.services.ErasureService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.http.*
import java.util.*

fun Application.registerErasureRoutes(erasureService: ErasureService) {
    routing {
        route("/erasure") {
//            post("/start") {
//                val patientId = call.receive<Int>()
//                val sessionId = erasureService.startErasureSession(patientId)
//                call.respond(HttpStatusCode.OK, sessionId)
//            }

            // Подготовка команды стирателей
            get("/team/prepare/{procedureId}") {
                val procedureId = call.parameters["procedureId"]?.toIntOrNull()
                if (procedureId == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid procedure ID")
                    return@get
                }
                val teamMembers = erasureService.prepareErasureTeam(procedureId)
                call.respond(teamMembers)
            }

            post("/complete/{sessionId}") {
                val sessionId = call.parameters["sessionId"]?.let {
                    try {
                        UUID.fromString(it)
                    } catch (e: IllegalArgumentException) {
                        null // или обработать ошибку более конкретно
                    }
                }
                if (sessionId == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid session ID")
                    return@post
                }
                erasureService.completeErasureSession(sessionId)
                call.respond(HttpStatusCode.OK, "Erasure session completed")
            }

            post("/fail/{sessionId}") {
                val sessionId = call.parameters["sessionId"]?.let {
                    try {
                        UUID.fromString(it)
                    } catch (e: IllegalArgumentException) {
                        null
                    }
                }
                if (sessionId == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid session ID")
                    return@post
                }
                erasureService.failErasureSession(sessionId)
                call.respond(HttpStatusCode.OK, "Erasure session failed")
            }
        }
    }
}
