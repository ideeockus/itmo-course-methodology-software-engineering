package com.memoryerasureservice.services

import com.memoryerasureservice.api.ServiceLocator
import com.memoryerasureservice.database.Equipment
import com.memoryerasureservice.model.EquipmentData
import com.memoryerasureservice.utils.StatisticsKeys
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.and
import java.time.LocalDate

class EquipmentService {

    fun checkEquipmentAvailability(): Boolean = transaction {
        // Проверяем, есть ли доступное оборудование для процедуры
        !Equipment.select { Equipment.status eq "available" }.empty()
    }

    fun assignEquipmentToProcedure(equipmentId: Int, procedureId: Int) = transaction {
        // Назначение оборудования для процедуры
        Equipment.update({ Equipment.id eq equipmentId }) {
            it[status] = "in_use"
        }
    }

    fun repairEquipment(equipmentId: Int) = transaction {
        ServiceLocator.statisticsService.incrementStatistic(StatisticsKeys.EQUIPMENT_REPAIRED)

        Equipment.update({ Equipment.id eq equipmentId }) {
            it[status] = "available"
            // Здесь может быть логика обновления даты следующего технического обслуживания
        }
    }

    fun reserveEquipment(equipmentId: Int) = transaction {
        Equipment.update({ Equipment.id eq equipmentId and (Equipment.status eq "available") }) {
            it[status] = "reserved"
        }
    }

    fun checkEquipmentStatus(equipmentId: Int): String = transaction {
        Equipment.select { Equipment.id eq equipmentId }
            .map { it[Equipment.status] }
            .firstOrNull() ?: "not found"
    }

    fun getEquipmentForMaintenance(): List<EquipmentData> = transaction {
        Equipment.select { Equipment.maintenanceDate.lessEq(LocalDate.now()) }
            .map { EquipmentData(
                id = it[Equipment.id].value,
                name = it[Equipment.name],
                type = it[Equipment.type],
                status = it[Equipment.status],
                location = it[Equipment.location],
                maintenanceDate = it[Equipment.maintenanceDate],
                serviceLife = it[Equipment.serviceLife]
            ) }
    }

    fun performMaintenance(equipmentId: Int, nextMaintenanceDate: LocalDate) = transaction {
        Equipment.update({ Equipment.id eq equipmentId }) {
            it[status] = "ok"
            it[Equipment.maintenanceDate] = nextMaintenanceDate
        }
    }

    fun decommissionEquipment(equipmentId: Int) = transaction {
        Equipment.update({ Equipment.id eq equipmentId }) {
            it[status] = "corrupted"
        }
    }
}
