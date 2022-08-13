package org.hyrical.users

import kotlinx.serialization.Contextual
import org.hyrical.forums.post.Post
import org.hyrical.users.repository.UserRepository
import org.litote.kmongo.eq
import org.litote.kmongo.save
import java.util.UUID

@kotlinx.serialization.Serializable
data class User(
    val id: @Contextual UUID = UUID.randomUUID(),
    var username: String,
    var email: String,
    var password: String,
    var verified: Boolean = false,
    var verificationCode: String = "",
    var createdAt: Long = System.currentTimeMillis(),
    var avatar: String = "",
    var messagesSent: Int = 0,
    var upvotesReceived: Int = 0,
    var downvotesReceived: Int = 0,
    var profileComments: List<Post> = mutableListOf(),
    ) {
    fun save() {
        UserRepository.users.save(this)
        //UserRepository.users.replaceOne(User::id eq id, this)
    }
}

data class UserSession(
    val userId: String
) {
    fun getUser(): User {
        return UserRepository.findById(UUID.fromString(userId))!!
    }
}