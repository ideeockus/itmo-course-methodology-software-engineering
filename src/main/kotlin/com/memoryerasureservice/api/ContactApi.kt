package com.memoryerasureservice.api

import com.memoryerasureservice.model.UserRole
import com.memoryerasureservice.services.ContactService
import com.memoryerasureservice.utils.authorize
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.http.*

fun Application.registerContactRoutes(contactService: ContactService) {
    routing {
        authorize(setOf( UserRole.ADMIN, UserRole.DOCTOR, UserRole.MANAGER, UserRole.NOTIFICATION_AGENT)) {
            route("/contacts") {
                post("/attempt") {
                    val request = call.receive<ContactAttemptRequest>()
                    contactService.addContactAttempt(
                        contactCardId = request.contactCardId,
                        method = request.method,
                        result = request.result
                    )
                    call.respond(HttpStatusCode.OK, "Contact attempt recorded successfully")
                }
            }
        }
    }
}

@kotlinx.serialization.Serializable
data class ContactAttemptRequest(
    val contactCardId: Int,
    val method: String,
    val result: String
)
