package com.memoryerasureservice

import com.memoryerasureservice.api.*
import com.memoryerasureservice.database.DatabaseFactory
import com.memoryerasureservice.services.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import java.io.File

fun main() {
    embeddedServer(Netty, port = 8890, host = "0.0.0.0") {
        install(ContentNegotiation) {
            json()
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

    }.start(wait = true)
}

fun Routing.staticContent() {
    // Настройка корня статического контента
    staticFiles("/", File("src/main/resources/static/new_frontend/"))
    staticFiles("/main", File("src/main/resources/static/new_frontend/main"))
    staticFiles("/page_full_profile", File("src/main/resources/static/new_frontend/page_full_profile"))
    staticFiles("/patient_history", File("src/main/resources/static/new_frontend/patient_history"))
    staticFiles("/technic_profile", File("src/main/resources/static/new_frontend/technic_profile"))
    staticFiles("/patient_apply", File("src/main/resources/static/new_frontend/patient_apply"))
    staticFiles("/patient_profile", File("src/main/resources/static/new_frontend/patient_profile"))
    staticFiles("/auth_form", File("src/main/resources/static/new_frontend/auth_form"))
    staticFiles("/patients_list", File("src/main/resources/static/new_frontend/patients_list"))

//    static("/") {
//        staticFiles("/", File("src/main/resources/static/new_frontend/main")) {
//            defaultResource("index.html")
//        }
//    }
//
//    // Настройка маршрутов для поддиректорий с учетом новой сигнатуры
//    listOf("page_full_profile", "patient_history", "technic_profile").forEach { dir ->
//        static("/$dir") {
//            staticFiles("/", File("src/main/resources/static/new_frontend/$dir")) {
//                defaultResource("index.html")
//            }
//        }
//    }

//    static("/") {
//        staticRootFolder = File("src/resources/static/new_frontend")
//        files("main")
//        default("main/index.html")
//    }

//    static("/page_full_profile") {
//        staticRootFolder = File("src/resources/static/new_frontend")
//        files("page_full_profile")
//        default("page_full_profile/index.html")
//    }
//
//    static("/patient_history") {
//        staticRootFolder = File("src/resources/static/new_frontend")
//        files("patient_history")
//        default("patient_history/index.html")
//    }
//
//    static("/technic_profile") {
//        staticRootFolder = File("src/resources/static/new_frontend")
//        files("technic_profile")
//        default("technic_profile/index.html")
//    }
}
