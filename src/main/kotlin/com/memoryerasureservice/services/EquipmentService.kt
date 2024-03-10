package com.memoryerasureservice.services

import com.memoryerasureservice.api.AddEquipmentReq
import com.memoryerasureservice.api.ServiceLocator
import com.memoryerasureservice.database.Equipment
import com.memoryerasureservice.model.EquipmentData
import com.memoryerasureservice.model.EquipmentStatus
import com.memoryerasureservice.utils.StatisticsKeys
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

class EquipmentService {
    fun addEquipment(data: AddEquipmentReq): EquipmentData? = transaction {
        val insertedId = Equipment.insert {
            it[name] = data.name
            it[type] = data.type
            it[status] = data.status
            it[location] = data.location ?: ""
            it[maintenanceDate] = data.maintenanceDate
            it[serviceLife] = data.serviceLife ?: 0
        } get Equipment.id
        getEquipmentById(insertedId.value)
    }

    fun getEquipmentById(id: Int): EquipmentData? = transaction {
        Equipment.select { Equipment.id eq id }
            .mapNotNull { toEquipmentData(it) }
            .singleOrNull()
    }

    fun updateEquipment(id: Int, data: EquipmentData): Boolean = transaction {
        Equipment.update({ Equipment.id eq id }) {
            it[name] = data.name
            it[type] = data.type
            it[status] = data.status
            it[location] = data.location ?: ""
            it[maintenanceDate] = data.maintenanceDate
            it[serviceLife] = data.serviceLife ?: 0
        } > 0
    }

    fun deleteEquipment(id: Int): Boolean = transaction {
        Equipment.deleteWhere { Equipment.id eq id } > 0
    }

    fun getAllEquipment(): List<EquipmentData> = transaction {
        Equipment.selectAll()
            .map { toEquipmentData(it) }
    }


    private fun toEquipmentData(row: ResultRow): EquipmentData =
        EquipmentData(
            id = row[Equipment.id].value,
            name = row[Equipment.name],
            type = row[Equipment.type],
            status = row[Equipment.status],
            location = row[Equipment.location],
            maintenanceDate = row[Equipment.maintenanceDate],
            serviceLife = row[Equipment.serviceLife]
        )


    fun checkEquipmentAvailability(): Boolean = transaction {
        // Проверяем, есть ли доступное оборудование для процедуры
        !Equipment.select { Equipment.status eq EquipmentStatus.Available }.empty()
    }

    fun assignEquipmentToProcedure(equipmentId: Int, procedureId: Int) = transaction {
        // Назначение оборудования для процедуры
        Equipment.update({ Equipment.id eq equipmentId }) {
            it[status] = EquipmentStatus.Busy
        }
    }

    fun repairEquipment(equipmentId: Int) = transaction {
        ServiceLocator.statisticsService.incrementStatistic(StatisticsKeys.EQUIPMENT_REPAIRED)

        Equipment.update({ Equipment.id eq equipmentId }) {
            it[status] = EquipmentStatus.Available
            // Здесь может быть логика обновления даты следующего технического обслуживания
        }
    }

    fun reserveEquipment(equipmentId: Int) = transaction {
        Equipment.update({ Equipment.id eq equipmentId and (Equipment.status eq EquipmentStatus.Available) }) {
            it[status] = EquipmentStatus.Reserved
        }
    }

    fun checkEquipmentStatus(equipmentId: Int): EquipmentStatus = transaction {
        Equipment.select { Equipment.id eq equipmentId }
            .map { it[Equipment.status] }
            .firstOrNull() ?: EquipmentStatus.NotAvailable
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
            it[status] = EquipmentStatus.Available
            it[Equipment.maintenanceDate] = nextMaintenanceDate
        }
    }

    fun decommissionEquipment(equipmentId: Int) = transaction {
        Equipment.update({ Equipment.id eq equipmentId }) {
            it[status] = EquipmentStatus.Corrupted
        }
    }
}
