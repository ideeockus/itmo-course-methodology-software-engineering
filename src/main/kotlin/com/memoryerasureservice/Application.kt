package com.memoryerasureservice

import com.memoryerasureservice.api.*
import com.memoryerasureservice.database.DatabaseFactory
import com.memoryerasureservice.model.UserRole
import com.memoryerasureservice.model.UserSession
import com.memoryerasureservice.services.*
import com.memoryerasureservice.utils.authorize
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import java.io.File

fun main() {
    embeddedServer(Netty, port = 8890, host = "0.0.0.0") {
        install(ContentNegotiation) {
            json()
        }
        install(StatusPages) {
            status(HttpStatusCode.NotFound) { call, status ->
                call.respondFile(File("src/main/resources/static/new_frontend/page_404.html"))
            }
            status(HttpStatusCode.Forbidden) { call, status ->
                call.respondFile(File("src/main/resources/static/new_frontend/page_403.html"))
            }
            status(HttpStatusCode.Unauthorized) { call, status ->
                call.respondFile(File("src/main/resources/static/new_frontend/page_401.html"))
            }
//            statusFile(HttpStatusCode.Unauthorized, HttpStatusCode.PaymentRequired, filePattern = "error#.html")
        }
        install(Sessions) {
            cookie<UserSession>("USER_SESSION") {
                cookie.extensions["SameSite"] = "lax"
            }
        }
        DatabaseFactory.init()

        routing {
            patientApi(PatientService())
            doctorApi(DoctorService())
//            swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")
            staticContent()
        }

        val memoryScanService = MemoryScanService()
        registerMemoryScanRoutes(memoryScanService)

        val contactService = ContactService()
        registerContactRoutes(contactService)

        val erasureService = ErasureService()
        registerErasureRoutes(erasureService)

        val equipmentService = EquipmentService()
        registerEquipmentRoutes(equipmentService)

        val statisticsService = StatisticsService()
        registerStatisticsRoutes(statisticsService)

        val userService = UserService()
        registerAuthRoutes(userService)

    }.start(wait = true)
}

fun Routing.staticContent() {
    // Настройка корня статического контента
    staticFiles("/", File("src/main/resources/static/new_frontend/"))
    staticFiles("/main", File("src/main/resources/static/new_frontend/main"))
    staticFiles("/patient_profile", File("src/main/resources/static/new_frontend/patient_profile"))
    staticFiles("/patient_apply", File("src/main/resources/static/new_frontend/patient_apply"))
    staticFiles("/auth_form", File("src/main/resources/static/new_frontend/auth_form"))
    staticFiles("/apply", File("src/main/resources/static/new_frontend/apply"))

    // we dont need it (?)
    staticFiles("/page_full_profile", File("src/main/resources/static/new_frontend/page_full_profile"))

    authorize(setOf( UserRole.ADMIN, UserRole.TECHNICIAN, UserRole.MANAGER)) {
        staticFiles("/technic_profile", File("src/main/resources/static/new_frontend/technic_profile"))
    }

    authorize(setOf( UserRole.ADMIN, UserRole.DOCTOR, UserRole.ERASER, UserRole.NOTIFICATION_AGENT)) {
        staticFiles("/patient_history", File("src/main/resources/static/new_frontend/patient_history"))
        staticFiles("/patients_list", File("src/main/resources/static/new_frontend/patients_list"))
        staticFiles("/patient_card_view", File("src/main/resources/static/new_frontend/patient_card_view"))
    }

    authorize(setOf( UserRole.ADMIN, UserRole.MANAGER)) {
        staticFiles("/statistics", File("src/main/resources/static/new_frontend/statistics"))
    }

    authorize(setOf(
        UserRole.ADMIN,
        UserRole.MANAGER,
        UserRole.ERASER,
        UserRole.DOCTOR,
        UserRole.NOTIFICATION_AGENT,
        UserRole.TECHNICIAN,
    )) {
        staticFiles("/main_menu", File("src/main/resources/static/new_frontend/main_menu"))
    }
}

