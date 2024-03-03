package com.memoryerasureservice.database

import com.memoryerasureservice.model.FamiliarState
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

object Familiars : IntIdTable() {
    val name = varchar("name", 255)
    val phone = varchar("phone", 20)
    val email = varchar("email", 255).nullable()
    val homePhone = varchar("homePhone", 255).nullable()
    val workPhone = varchar("workPhone", 255).nullable()
    val state = enumerationByName("state", 25, FamiliarState::class)
}
