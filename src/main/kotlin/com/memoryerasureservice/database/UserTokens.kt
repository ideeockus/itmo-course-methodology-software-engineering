package com.memoryerasureservice.database

import com.memoryerasureservice.model.UserToken
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime

object UserTokens : IntIdTable() {
    val userId = reference("user_id", Users)
    val token = varchar("token", 512)
    val createdAt = datetime("created_at")
    val expiresAt = datetime("expires_at").nullable()
}

fun ResultRow.toUserToken(): UserToken = UserToken(
    userId = this[UserTokens.userId].value,
    token = this[UserTokens.token],
    createdAt = this[UserTokens.createdAt],
    expiresAt = this[UserTokens.expiresAt],
)