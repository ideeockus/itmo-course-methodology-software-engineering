package com.memoryerasureservice.api

import com.memoryerasureservice.services.EquipmentService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.http.*

fun Application.registerEquipmentRoutes(erasurePreparationService: EquipmentService) {
    routing {
        route("/erasure") {
            // Проверка доступности оборудования
            get("/equipment/availability") {
                val isAvailable = erasurePreparationService.checkEquipmentAvailability()
                call.respond(mapOf("isAvailable" to isAvailable))
            }

            // Назначение оборудования для процедуры
            post("/equipment/assign") {
                val request = call.receive<EquipmentAssignRequest>()
                erasurePreparationService.assignEquipmentToProcedure(request.equipmentId, request.procedureId)
                call.respond(HttpStatusCode.OK, "Equipment assigned successfully")
            }
        }
    }
}

@kotlinx.serialization.Serializable
data class EquipmentAssignRequest(
    val equipmentId: Int,
    val procedureId: Int
)
