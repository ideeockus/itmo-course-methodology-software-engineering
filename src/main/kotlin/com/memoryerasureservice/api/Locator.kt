package com.memoryerasureservice.api

import com.memoryerasureservice.services.StatisticsService

object ServiceLocator {
    val statisticsService = StatisticsService()
}