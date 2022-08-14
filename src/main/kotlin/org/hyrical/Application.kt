package org.hyrical

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import org.hyrical.communities.endpoints.createCommunityEndpoints
import org.hyrical.communities.post.endpoints.createPostEndpoints
import org.hyrical.forums.Forum
import org.hyrical.plugins.*
import org.hyrical.users.User
import org.hyrical.users.endpoints.createUserEndpoints

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        configureMonitoring()

        createUserEndpoints()
        createCommunityEndpoints()
    }.start(wait = true)
}

suspend fun respond(call: ApplicationCall, success: Boolean, message: String) {
    call.respond(
        if (success) HttpStatusCode.Accepted else HttpStatusCode.BadRequest,
        mapOf(
            "success" to success.toString(),
            "message" to message
        )
    )
}