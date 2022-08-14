package org.hyrical.communities.endpoints

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.hyrical.communities.post.endpoints.createPostEndpoints
import org.hyrical.communities.repository.CommunityRepository
import org.hyrical.respond

fun Application.createCommunityEndpoints() {
    createPostEndpoints()

    routing {
        route("/api/communities/") {
            get {
                val communityName = call.parameters["communityName"]

                if (communityName == null) {
                    call.respond(CommunityRepository.communities.find())
                    return@get
                }

                val community = CommunityRepository.searchByName(communityName)

                if (community == null) {
                    respond(call, false, "Community not found")
                    return@get
                }

                call.respond(community)
            }
        }
    }
}