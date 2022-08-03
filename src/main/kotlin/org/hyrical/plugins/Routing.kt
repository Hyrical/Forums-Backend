package org.hyrical.plugins

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.locations.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import org.hyrical.forums.Forum
import org.hyrical.forums.post.Post
import org.hyrical.forums.repository.ForumRepository

@OptIn(KtorExperimentalLocationsAPI::class)
fun Application.configureRouting() {

    data class MofifiedForum(
        var id: String,
        var name: String,
        var description: String,
        var isRestricted: Boolean,
        var category: Forum.Category,

        var recentPost: Post? = null
    )

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/api/forums") {
            val modifiedForums: MutableList<MofifiedForum> = mutableListOf()

            ForumRepository.getAll().map { forum ->
                modifiedForums.add(MofifiedForum(
                    id = forum.id,
                    name = forum.name,
                    description = forum.description,
                    isRestricted = forum.isRestricted,
                    category = forum.category,
                    recentPost = forum.recentPost
                ))
            }

            call.respond("Forum: $modifiedForums")
        }

        get("/api/forum/{forumName}") {
            val forumName = call.parameters["forumName"]

            val forum = ForumRepository.byId(forumName!!)

            if (forum == null) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }

            call.respond("Forum: $forum")
        }
    }
}