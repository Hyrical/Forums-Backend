package org.hyrical.users.endpoints

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import kotlinx.serialization.json.Json
import org.hyrical.respond
import org.hyrical.users.User
import org.hyrical.users.UserSession
import org.hyrical.users.repository.UserRepository
import org.hyrical.utils.HashUtils
import org.litote.kmongo.eq

fun Application.createUserEndpoints() {
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
        json(Json {
            prettyPrint = true
            isLenient = true
        })
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

                call.sessions.set(
                    UserSession(
                        user.id.toString()
                    )
                )

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

            delete("delete") {
                val userSession = call.sessions.get<UserSession>()

                if (userSession == null) {
                    respond(call, false, "You are not logged in")
                    return@delete
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

                call.sessions.set(
                    UserSession(
                        user.id.toString()
                    )
                )

                respond(call, true, code.toString())
            }

            route("update") {
                post("username") {
                    val userSession = call.sessions.get<UserSession>()

                    if (userSession == null) {
                        respond(call, false, "You are not logged in")
                        return@post
                    }

                    val user = userSession.getUser()

                    val username = call.parameters["username"]

                    if (username == null) {
                        respond(call, false, "Username is required")
                        return@post
                    }

                    if (UserRepository.findByUsername(call.parameters["username"]!!) != null) {
                        respond(call, false, "Another user already has that username")
                        return@post
                    }

                    user.username = username

                    UserRepository.users.updateOne(User::id eq user.id, User::username eq user.username)

                    respond(call, true, "Username updated")
                }


                post("password") {
                    val userSession = call.sessions.get<UserSession>()

                    if (userSession == null) {
                        respond(call, false, "You are not logged in")
                        return@post
                    }

                    val user = userSession.getUser()

                    val password = call.parameters["password"]

                    if (password == null) {
                        respond(call, false, "Password is required")
                        return@post
                    }

                    user.password = HashUtils.hash(password)

                    UserRepository.users.updateOne(User::id eq user.id, User::password eq user.password)

                    respond(call, true, "Password updated")
                }
            }
        }
    }
}