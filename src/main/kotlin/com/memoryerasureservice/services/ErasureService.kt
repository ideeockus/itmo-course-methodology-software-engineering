package com.memoryerasureservice.services

import com.memoryerasureservice.api.ServiceLocator
import com.memoryerasureservice.database.ErasureSessions
import com.memoryerasureservice.database.ErasureTeam
import com.memoryerasureservice.database.toErasureTeamMember
import com.memoryerasureservice.model.ErasureSession
import com.memoryerasureservice.model.ErasureTeamMember
import com.memoryerasureservice.utils.StatisticsKeys
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.*

class ErasureService {

//    fun startErasureSession(patientId: Int): Int = transaction {
//        ErasureSessions.insert {
//            it[ErasureSessions.patientId] = patientId
//            it[ErasureSessions.startTime] = LocalDateTime.now()
//            it[ErasureSessions.status] = "in_progress"
//        } get ErasureSessions.id
//    }

    fun prepareErasureTeam(procedureId: Int): List<ErasureTeamMember> = transaction {
        // Получаем список членов бригады, готовых к процедуре
        val teamMembers = ErasureTeam.select {
            (ErasureTeam.status eq "ready") or (ErasureTeam.status eq "on_route")
        }.map { it.toErasureTeamMember() }

        // Обновляем статус команды и назначаем машину
        teamMembers.forEach { member ->
            ErasureTeam.update({ ErasureTeam.id eq member.id }) {
                it[status] = "at_location"
                it[assignedVehicle] = "VehicleForProcedure_$procedureId"
            }
        }

        teamMembers
    }

    fun completeErasureSession(sessionId: UUID) = transaction {
        ServiceLocator.statisticsService.incrementStatistic(StatisticsKeys.PROCEDURES_COMPLETED)

        ErasureSessions.update({ ErasureSessions.id eq sessionId }) {
            it[endTime] = LocalDateTime.now()
            it[status] = "completed"
        }
    }

    fun failErasureSession(sessionId: UUID) = transaction {
        ErasureSessions.update({ ErasureSessions.id eq sessionId }) {
            it[endTime] = LocalDateTime.now()
            it[status] = "failed"
        }
    }
}

fun getErasureSessionById(id: UUID): ErasureSession? = transaction {
    ErasureSessions.select { ErasureSessions.id eq id }
        .mapNotNull { row ->
            ErasureSession(
                id = row[ErasureSessions.id],
                patientId = row[ErasureSessions.patientId].value,
                startTime = row[ErasureSessions.startTime],
                endTime = row[ErasureSessions.endTime],
                status = row[ErasureSessions.status]
            )
        }.singleOrNull()
}
