package org.hyrical.communities.post.reply

@kotlinx.serialization.Serializable
data class ForumReply(
    val author: String,
    val content: String,
    val createdAt: Long = System.currentTimeMillis()
)