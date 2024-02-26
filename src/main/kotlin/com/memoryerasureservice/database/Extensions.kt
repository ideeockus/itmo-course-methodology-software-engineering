package com.memoryerasureservice.database

import com.memoryerasureservice.model.ErasureTeamMember
import org.jetbrains.exposed.sql.ResultRow

//fun ResultRow.toEquipment() = Equipment(
//    id = this[Equipment.id].value,
//    name = this[Equipment.name],
//    type = this[Equipment.type],
//    status = this[Equipment.status]
//)

fun ResultRow.toErasureTeamMember() = ErasureTeamMember(
    id = this[ErasureTeam.id].value,
    memberName = this[ErasureTeam.memberName],
    role = this[ErasureTeam.role],
    status = this[ErasureTeam.status],
    assignedVehicle = this[ErasureTeam.assignedVehicle]
)
