package com.memoryerasureservice.api

import com.memoryerasureservice.model.UserSession
import com.memoryerasureservice.services.UserService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.sessions.*

fun Application.registerAuthRoutes(userService: UserService) {
    routing {
        post("/login") {
            val loginRequest = call.receive<LoginRequest>()
            val userSession = userService.authenticateUser(loginRequest.username, loginRequest.password)
            if (userSession != null) {
                call.sessions.set(userSession)
                call.respond(HttpStatusCode.OK, userSession)
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
            }
        }
        post("/api/logout") {
            // Завершаем сессию пользователя
            call.sessions.clear<UserSession>()

            // Возвращаем ответ, подтверждающий успешный выход
            call.respond(HttpStatusCode.OK, "Logged out successfully")
        }
    }
}

@kotlinx.serialization.Serializable
data class LoginRequest(val username: String, val password: String)
