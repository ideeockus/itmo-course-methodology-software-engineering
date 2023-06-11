package com.anamnesia.plugins

import com.anamnesia.repository.create_user_request
import com.anamnesia.requests.CreateRequestReq
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
            val new_card_id = create_user_request(req)

            println("new card id ${new_card_id}")
            println(req)
            println("hehe")
            call.respondText("Ok")
        }
        // Static plugin. Try to access `/static/index.html`
//        staticResources("/static", "static") {
        static("/web_app") {
            resources("static")
        }
    }
}
