package com.memoryerasureservice.database

import com.memoryerasureservice.model.FamiliarState
import com.memoryerasureservice.model.MemoryScan
import com.memoryerasureservice.model.User
import com.memoryerasureservice.model.UserRole
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

object Users : IntIdTable() {
    val name = varchar("name", 255)
    val role = enumerationByName("role", 25, UserRole::class)
    val passwordHash = varchar("password_hash", 512)
}

fun ResultRow.toUser() = User(
    id = this[Users.id].value,
    role = this[Users.role],
    passwordHash = this[Users.passwordHash],
    name = this[Users.name],
)