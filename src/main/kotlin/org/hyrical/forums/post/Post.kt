package org.hyrical.forums.post

import kotlinx.serialization.Contextual
import java.util.UUID

@kotlinx.serialization.Serializable
data class Post(
    var id: Int,
    var title: String,
    var content: String,
    var author: @Contextual UUID,
    var createdAt: Long,
    var isLocked: Boolean,

    var responses: MutableList<Response> = mutableListOf()
) {
    @kotlinx.serialization.Serializable
    data class Response(
        var author: @Contextual UUID,
        var content: String,
        var createdAt: Long,
    )
}