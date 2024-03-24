package com.memoryerasureservice.services

import com.memoryerasureservice.database.UserTokens
import com.memoryerasureservice.database.Users
import com.memoryerasureservice.database.toUserToken
import com.memoryerasureservice.model.User
import com.memoryerasureservice.model.UserRole
import com.memoryerasureservice.model.UserSession
import com.memoryerasureservice.model.UserToken
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.mindrot.jbcrypt.BCrypt
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.*

class UserService {
    fun createUser(username: String, password: String, role: UserRole): User? {
        val passwordHash = BCrypt.hashpw(password, BCrypt.gensalt())
        val userId =
            transaction {
                Users.insertAndGetId {
                    it[Users.name] = username
                    it[Users.passwordHash] = passwordHash
                    it[Users.role] = role
                }
            }.value
        return getUserById(userId)
    }

    fun getUserById(id: Int): User? = transaction {
        Users.select { Users.id eq id }
            .mapNotNull { row ->
                User(
                    id = row[Users.id].value,
                    name = row[Users.name],
                    passwordHash = row[Users.passwordHash],
                    role = row[Users.role]
                )
            }.singleOrNull()
    }

    fun authenticateUser(name: String, password: String): UserSession? = transaction {
        val user = Users.select { Users.name eq name }
            .mapNotNull { row ->
                User(
                    id = row[Users.id].value,
                    name = row[Users.name],
                    passwordHash = row[Users.passwordHash],
                    role = row[Users.role]
                )
            }.singleOrNull()
        val isPasswordCorrect = user?.let {
            BCrypt.checkpw(password, it.passwordHash)
        } ?: false

        if (isPasswordCorrect && user != null) {
            val token = UUID.randomUUID().toString()
            val now = LocalDateTime.now()
            val expires = now.plusHours(1)

            UserTokens.insert { row ->
                row[userId] = user.id
                row[UserTokens.token] = token
                row[createdAt] = now
                row[expiresAt] = expires
            }

            UserSession(user.id, user.role, token)
        } else {
            null
        }
    }
}
