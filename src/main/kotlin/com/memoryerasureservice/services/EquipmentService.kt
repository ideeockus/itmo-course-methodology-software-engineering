package com.memoryerasureservice.services

import com.memoryerasureservice.database.Equipment
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

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
}
