package com.memoryerasureservice.api

import com.memoryerasureservice.model.Statistic
import com.memoryerasureservice.services.StatisticsService
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.registerStatisticsRoutes(statisticsService: StatisticsService) {
    routing {
        route("/statistics") {
            get("/") {
                val statistics = statisticsService.getAllStatistics()
                call.respond(statistics)
            }

            post("/update") {
                val statistic = call.receive<Statistic>()
                statisticsService.updateStatistic(statistic.key, statistic.value)
                call.respond(mapOf("message" to "Statistic updated successfully"))
            }

            post("/add") {
                val statistic = call.receive<Statistic>()
                statisticsService.addStatistic(statistic.key, statistic.value)
                call.respond(mapOf("message" to "Statistic added successfully"))
            }
        }
    }
}
