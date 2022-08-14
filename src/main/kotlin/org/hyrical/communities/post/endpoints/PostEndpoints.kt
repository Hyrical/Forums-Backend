package org.hyrical.communities.post.endpoints

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.hyrical.communities.repository.CommunityRepository
import org.hyrical.respond
import org.hyrical.users.UserSession

fun Application.createPostEndpoints() {
    routing {
        route("/api/posts/") {
            post("upvote") {
                val userSession = call.sessions.get<UserSession>()

                if (userSession == null) {
                    respond(call, false, "You are not logged in")
                    return@post
                }

                val user = userSession.getUser()

                val postId = call.parameters["postId"]

                if (postId == null) {
                    respond(call, false, "No post id provided")
                    return@post
                }

                val communityId = call.parameters["communityId"]

                if (communityId == null) {
                    respond(call, false, "No community name provided")
                    return@post
                }

                val community = CommunityRepository.searchByName(communityId)

                if (community == null) {
                    respond(call, false, "Community not found")
                    return@post
                }

                val post = community.posts.firstOrNull { it._id == postId }

                if (post == null) {
                    respond(call, false, "Post not found")
                    return@post
                }

                if (post.upvoters.contains(user.id.toString())) {
                    respond(call, false, "You have already upvoted this post")
                    return@post
                }

                post.upvoters.add(user.id.toString())
                post.upvotes++

                CommunityRepository.save(community)
            }
        }
    }
}