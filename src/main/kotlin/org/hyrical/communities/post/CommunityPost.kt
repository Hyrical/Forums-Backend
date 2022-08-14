package org.hyrical.communities.post

import kotlinx.serialization.Contextual
import org.hyrical.communities.post.reply.ForumReply
import org.hyrical.users.User
import org.hyrical.users.repository.UserRepository
import java.util.UUID

@kotlinx.serialization.Serializable
data class CommunityPost(
    val _id: String = UUID.randomUUID().toString().replace("-", "").substring(0, 9),
    val author: String,
    val content: String,
    val createdAt: Long = System.currentTimeMillis(),
    val lastEditedAt: Long = System.currentTimeMillis(),
    var upvotes: Int = 0,
    var downvotes: Int = 0,
    var isRestricted: Boolean = false,

    val upvoters: MutableList<String> = mutableListOf(),

    var comments: List<ForumReply>
)