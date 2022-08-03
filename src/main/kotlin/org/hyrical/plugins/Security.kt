package org.hyrical.plugins

import io.ktor.http.*
import io.ktor.server.sessions.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.util.*
import org.hyrical.users.User
import org.hyrical.users.UserSession
import org.hyrical.users.repository.UserRepository
import org.hyrical.utils.Encryptions
import org.litote.kmongo.eq
import java.io.File

fun Application.configureSecurity() {
    val secretEncryptKey = hex("00112233445566778899aabbccddeeff")
    val secretSignKey = hex("6819b57a326945c1968f45236589")
    install(Sessions) {
        cookie<UserSession>("user_session", SessionStorageMemory()) {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 60 * 60 * 5
            transform(SessionTransportTransformerEncrypt(secretEncryptKey, secretSignKey))
        }
    }

    routing {
        get("/api/login/{email}/{password}") {

            if (call.sessions.get<UserSession>() != null) {
                call.respond(HttpStatusCode.Forbidden, "You are already logged in")
                return@get
            }

            val email = call.parameters["email"] ?: throw IllegalArgumentException("email is required")
            val password = call.parameters["password"] ?: throw IllegalArgumentException("password is required")

            if (UserRepository.findByEmail(email) == null) {
                call.respondText("User not found", status = HttpStatusCode.NotFound)
                return@get
            }

            val user = UserRepository.findByEmail(email)!!

            if (user.password != password) {
                call.respondText("Password is incorrect", status = HttpStatusCode.Unauthorized)
                return@get
            }

            call.sessions.set(UserSession(
                user.id.toString()
            ))
            call.respondText("Logged in")
        }

        get("/api/logout") {
            call.sessions.clear<UserSession>()
            call.respondText("Logged out", status = HttpStatusCode.OK)
        }

        get("/api/delete") {
            val userSession = call.sessions.get<UserSession>()
            if (userSession == null) {
                call.respondText("You are not logged in", status = HttpStatusCode.Unauthorized)
                return@get
            }

            val user = userSession.getUser()

            UserRepository.users.deleteOne(User::id eq user.id)
            call.sessions.clear<UserSession>()

            call.respondText("Deleted", status = HttpStatusCode.OK)
        }
    }
}
