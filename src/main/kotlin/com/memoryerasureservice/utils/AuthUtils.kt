package com.memoryerasureservice.utils

import com.memoryerasureservice.database.UserTokens
import com.memoryerasureservice.database.Users
import com.memoryerasureservice.database.toUser
import com.memoryerasureservice.database.toUserToken
import com.memoryerasureservice.model.UserRole
import com.memoryerasureservice.model.UserSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

//suspend fun ApplicationCall.verifyRole(requiredRole: UserRole): Boolean {
//    val token = request.headers["Authorization"]?.removePrefix("Bearer ")
//    if (token == null) {
//        respond(HttpStatusCode.Unauthorized, "Token is missing")
//        return false
//    }
//
//    val userToken = transaction {
//        UserTokens.select { UserTokens.token eq token }
//            .mapNotNull { row ->
//                row.toUserToken()
//            }.singleOrNull()
//    }
//
//    val user = userToken?.let {
//        transaction {
//            Users.select { Users.id eq it.userId }
//                .mapNotNull { row ->
//                    row.toUser()
//                }.singleOrNull()
//        }
//    }
//
//    if (user?.role != requiredRole) {
//        respond(HttpStatusCode.Forbidden, "Access denied")
//        return false
//    }
//
//    return true
//}
//
//fun PipelineContext<Unit, ApplicationCall>.authorize(allowedRoles: Set<UserRole>): Boolean {
//    val userSession = call.sessions.get<UserSession>()
//    return userSession?.role in allowedRoles
//}

//fun Route.authorize(vararg allowedRoles: UserRole, build: Route.() -> Unit): Route {
//    val authorizedRoute = createChild(object : RouteSelector() {
//        override fun evaluate(context: RoutingResolveContext, segmentIndex: Int) = RouteSelectorEvaluation.Transparent
//    })
//    application.intercept(ApplicationCallPipeline.Call) {
//        val role = call.sessions.get<UserSession>()?.role
//        println("check auth for role $role and route $build. Allowed: ${allowedRoles.toList()}")
//
//        if (role in allowedRoles) {
//            proceed()
//        } else {
//            call.respond(HttpStatusCode.Forbidden, "Access Denied")
//            finish()
//        }
//    }
//    authorizedRoute.build()
//    return authorizedRoute
//}

fun Route.authorize(allowedRoles: Set<UserRole>, build: Route.() -> Unit): Route {
    return createChild(object : RouteSelector() {
        override fun evaluate(context: RoutingResolveContext, segmentIndex: Int) = RouteSelectorEvaluation.Transparent
    }).apply {
        build()
        intercept(ApplicationCallPipeline.Features) {
            if (call.sessions.get<UserSession>()?.let { it.role in allowedRoles } != true) {
                call.respond(HttpStatusCode.Forbidden, "Access Denied")
                finish()
            }
        }
    }
}


fun Route.authorizedStatic(roles: List<UserRole>, root: String) {
    val file = File(root)
    if (file.isFile) {
        get(root) {
            val session = call.sessions.get<UserSession>()
            if (session?.role in roles) {
                call.respondFile(file)
            } else {
                call.respond(HttpStatusCode.Forbidden)
            }
        }
    } else if (file.isDirectory) {
        file.walkTopDown().forEach { fileInDir ->
            val path = fileInDir.relativeTo(File(root)).path.replace(File.separatorChar, '/')
            get(path) {
                val session = call.sessions.get<UserSession>()
                if (session?.role in roles) {
                    call.respondFile(fileInDir)
                } else {
                    call.respond(HttpStatusCode.Forbidden)
                }
            }
        }
    }
}
