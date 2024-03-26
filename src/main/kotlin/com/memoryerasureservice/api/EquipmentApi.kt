package com.memoryerasureservice.api

import com.memoryerasureservice.model.EquipmentData
import com.memoryerasureservice.model.EquipmentStatus
import com.memoryerasureservice.model.LocalDateSerializer
import com.memoryerasureservice.model.UserRole
import com.memoryerasureservice.services.EquipmentService
import com.memoryerasureservice.utils.authorize
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import java.time.LocalDate

fun Application.registerEquipmentRoutes(equipmentService: EquipmentService) {
    routing {
        authorize(setOf( UserRole.ADMIN, UserRole.TECHNICIAN)) {
            route("/equipment") {
                post("/") {
                    val equipmentData = call.receive<AddEquipmentReq>()
                    val addedEquipment = equipmentService.addEquipment(equipmentData)
                    if (addedEquipment != null) {
                        call.respond(HttpStatusCode.Created, addedEquipment)
                    } else {
                        call.respond(HttpStatusCode.InternalServerError)
                    }
                }

                get("/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull()
                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
                        return@get
                    }
                    val equipmentData = equipmentService.getEquipmentById(id)
                    if (equipmentData != null) {
                        call.respond(HttpStatusCode.OK, equipmentData)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }

                put("/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull()
                    val equipmentData = call.receive<EquipmentData>()
                    if (id == null || !equipmentService.updateEquipment(id, equipmentData)) {
                        call.respond(HttpStatusCode.BadRequest, "Problem updating equipment")
                    } else {
                        call.respond(HttpStatusCode.OK, equipmentData)
                    }
                }

                delete("/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull()
                    if (id == null || !equipmentService.deleteEquipment(id)) {
                        call.respond(HttpStatusCode.BadRequest, "Problem deleting equipment")
                    } else {
                        call.respond(HttpStatusCode.OK)
                    }
                }

                get("/list") {
                    val equipmentList = equipmentService.getAllEquipment()
                    call.respond(HttpStatusCode.OK, equipmentList)
                }


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
}

@kotlinx.serialization.Serializable
data class EquipmentAssignRequest(
    val equipmentId: Int,
    val procedureId: Int
)

@kotlinx.serialization.Serializable
data class NextMaintenanceDateRequest(val nextMaintenanceDate: String?)


@kotlinx.serialization.Serializable
data class AddEquipmentReq(
    val name: String,
    val type: String,
    val status: EquipmentStatus,
    val location: String?,
    @Serializable(with = LocalDateSerializer::class)
    val maintenanceDate: LocalDate?,
    val serviceLife: Int?
)