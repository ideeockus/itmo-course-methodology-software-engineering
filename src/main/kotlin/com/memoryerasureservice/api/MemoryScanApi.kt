package com.memoryerasureservice.api

import com.memoryerasureservice.services.MemoryScanService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.http.*

fun Application.registerMemoryScanRoutes(memoryScanService: MemoryScanService) {
    routing {
        route("/memoryScans") {
            post("/record") {
                val request = call.receive<MemoryScanRequest>()
                memoryScanService.recordMemoryScan(
                    patientId = request.patientId,
                    memoryItem = request.memoryItem,
                    activityData = request.activityData
                )
                call.respond(HttpStatusCode.OK, "Memory scan recorded successfully")
            }
        }
    }
}

@kotlinx.serialization.Serializable
data class MemoryScanRequest(
    val patientId: Int,
    val memoryItem: String,
    val activityData: String
)
