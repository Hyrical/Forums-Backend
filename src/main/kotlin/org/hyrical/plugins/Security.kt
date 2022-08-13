package org.hyrical.plugins

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.sessions.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.util.*
import org.hyrical.respond
import org.hyrical.users.User
import org.hyrical.users.UserSession
import org.hyrical.users.repository.UserRepository
import org.hyrical.utils.HashUtils
import org.litote.kmongo.eq
import org.litote.kmongo.json
import java.io.File
import kotlin.random.Random

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

    install(ContentNegotiation) {
        json()
    }

    routing {

        route("/api/users/") {
            post("login") {

                if (call.sessions.get<UserSession>() != null) {
                    respond(call, false, "You are already logged in")
                    return@post
                }

                val email = call.parameters["email"]

                if (email == null) {
                    respond(call, false, "Email is required")
                    return@post
                }

                val password = call.parameters["password"]

                if (password == null) {
                    respond(call, false, "Password is required")
                    return@post
                }

                if (UserRepository.findByEmail(email) == null) {
                    respond(call, false, "User not found")
                    return@post
                }

                val user = UserRepository.findByEmail(email)!!

                if (user.password != HashUtils.hash(password)) {
                    respond(call, false, "Invalid password")
                    return@post
                }

                call.sessions.set(UserSession(
                    user.id.toString()
                ))

                respond(call, true, "Logged in")
            }

            post("logout") {
                if (call.sessions.get<UserSession>() == null) {
                    respond(call, false, "You are not logged in")
                    return@post
                }

                call.sessions.clear<UserSession>()
                respond(call, true, "Logged out")
            }

            post("delete") {
                val userSession = call.sessions.get<UserSession>()

                if (userSession == null) {
                    respond(call, false, "You are not logged in")
                    return@post
                }

                val user = userSession.getUser()

                UserRepository.users.deleteOne(User::id eq user.id)
                call.sessions.clear<UserSession>()

                respond(call, true, "User deleted")
            }

            post("create") {
                if (call.sessions.get<UserSession>() != null) {
                    respond(call, false, "You are already logged in")
                    return@post
                }

                call.receiveText()

                val username = call.parameters["username"]

                if (username == null) {
                    respond(call, false, "Username is required")
                    return@post
                }

                val email = call.parameters["email"]

                if (email == null) {
                    respond(call, false, "Email is required")
                    return@post
                }

                val password = call.parameters["password"]

                if (password == null) {
                    respond(call, false, "Password is required")
                    return@post
                }

                if (UserRepository.findByEmail(email) != null) {
                    respond(call, false, "User already exists")
                    return@post
                }

                val code = java.lang.StringBuilder()
                val random = java.util.Random()

                for (i in 0..6) {
                    code.append(random.nextInt(9))
                }

                val user = User(
                    username = username,
                    email = email,
                    password = HashUtils.hash(password),
                    verificationCode = code.toString()
                )

                UserRepository.users.insertOne(user)

                call.sessions.set(UserSession(
                    user.id.toString()
                ))

                respond(call, true, code.toString())
            }
        }
    }
}
