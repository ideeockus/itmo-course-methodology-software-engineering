package com.anamnesia.plugins

import com.anamnesia.repository.createPatientCard
import com.anamnesia.repository.createPatientCard
import com.anamnesia.repository.fillTimeSlot
import com.anamnesia.requests.CreateRequestReq
import com.anamnesia.requests.CreateRequestResp
import io.ktor.client.request.forms.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*

fun Application.configureRouting() {
    
    routing {
        get("/") {
            call.respondText("404 Sorry Not Found")
        }
        post("/create_request") {
            println("чебурек")

            val req = call.receive<CreateRequestReq>()
            val newUserToken = genUserToken()
            val new_card_id = createPatientCard(req.name, req.phone, newUserToken)
            val isTimeReserved = fillTimeSlot(req.date, req.time)

            println("new card id ${new_card_id}")
            println(req)
            println("hehe")
//            call.respondText("Ok")
            call.respond(CreateRequestResp(new_card_id, newUserToken))
        }
        // Static plugin. Try to access `/static/index.html`
//        staticResources("/static", "static") {
        static("/web_app") {
            resources("static")
        }
    }
}

fun genUserToken(): String {
    val TOKEN_LEN = 22
    return getRandomString(TOKEN_LEN)
}

fun getRandomString(length: Int) : String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}