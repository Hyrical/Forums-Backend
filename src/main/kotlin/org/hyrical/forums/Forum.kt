package org.hyrical.forums

import kotlinx.serialization.Contextual
import org.hyrical.forums.post.Post
import org.hyrical.forums.repository.ForumRepository
import org.litote.kmongo.eq
import org.litote.kmongo.save

@kotlinx.serialization.Serializable
data class Forum(
    var id: String,
    var name: String,
    var description: String,
    var isRestricted: Boolean,
    var category: Category,

    var posts: MutableList<Post>,

    var recentPost: Post? = null
) {
    fun save() {
        ForumRepository.forums.save(this)
        //ForumRepository.forums.replaceOne(Forum::id eq id, this)
    }

    enum class Category {
        ANNOUNCEMENTS,
        GENERAL,
        APPLICATIONS,
        OTHER
    }
}