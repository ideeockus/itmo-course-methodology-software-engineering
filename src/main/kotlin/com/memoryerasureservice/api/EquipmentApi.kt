package com.memoryerasureservice.api

import com.memoryerasureservice.services.EquipmentService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.http.*
import java.time.LocalDate

fun Application.registerEquipmentRoutes(equipmentService: EquipmentService) {
    routing {
        route("/equipment") {
            // Проверка доступности оборудования
            get("/availability") {
                val isAvailable = equipmentService.checkEquipmentAvailability()
                call.respond(mapOf("isAvailable" to isAvailable))
            }

            // Назначение оборудования для процедуры
            post("/assign") {
                val request = call.receive<EquipmentAssignRequest>()
                equipmentService.assignEquipmentToProcedure(request.equipmentId, request.procedureId)
                call.respond(HttpStatusCode.OK, "Equipment assigned successfully")
            }

            get("/maintenance") {
                val equipmentList = equipmentService.getEquipmentForMaintenance()
                call.respond(equipmentList)
            }

            post("/maintenance/{equipmentId}") {
                val equipmentId = call.parameters["equipmentId"]?.toIntOrNull()
                val nextMaintenanceDate = call.receive<NextMaintenanceDateRequest>().nextMaintenanceDate
                if (equipmentId == null || nextMaintenanceDate == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid data")
                    return@post
                }
                equipmentService.performMaintenance(equipmentId, LocalDate.parse(nextMaintenanceDate))
                call.respond(HttpStatusCode.OK, "Maintenance performed successfully")
            }

            post("/decommission/{equipmentId}") {
                val equipmentId = call.parameters["equipmentId"]?.toIntOrNull()
                if (equipmentId == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid equipment ID")
                    return@post
                }
                equipmentService.decommissionEquipment(equipmentId)
                call.respond(HttpStatusCode.OK, "Equipment decommissioned successfully")
            }
        }
    }
}

@kotlinx.serialization.Serializable
data class EquipmentAssignRequest(
    val equipmentId: Int,
    val procedureId: Int
)

@kotlinx.serialization.Serializable
data class NextMaintenanceDateRequest(val nextMaintenanceDate: String?)
