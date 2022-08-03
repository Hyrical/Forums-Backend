package org.hyrical

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.hyrical.forums.Forum
import org.hyrical.plugins.*
import org.hyrical.users.User

fun main() {
    val user = User(
        username = "Nopox",
        email = "test@gmail.com",
        password = "test"
    )
    user.save()
    /*
       val forum = Forum(
           id = "test",
           name = "Test",
           description = "Test forum",
           isRestricted = false,
           category = Forum.Category.ANNOUNCEMENTS,
           posts = mutableListOf(),
       )

       forum.save()
        */
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        configureSecurity()
        configureMonitoring()
        configureRouting()
    }.start(wait = true)
}
