package org.hyrical.communities

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.hyrical.communities.post.CommunityPost
import org.hyrical.communities.repository.CommunityRepository
import java.util.UUID

@Serializable
data class Community(
    val _id: @Contextual UUID = UUID.randomUUID(),
    val name: String,
    val title: String = name,
    val logo: String = "",
    val description: String = "There is no description yet.",
    val tags: MutableList<String> = mutableListOf(),
    val rules: MutableList<String> = mutableListOf(),
    val members: MutableList<@Contextual UUID> = mutableListOf(),
    val moderators: MutableList<@Contextual UUID> = mutableListOf(),
    val admins: MutableList<@Contextual UUID> = mutableListOf(),

    val createdAt: Long = System.currentTimeMillis(),
    val pinnedPost: CommunityPost? = null,

    var latestPost: CommunityPost? = null,

    val posts: MutableList<CommunityPost> = mutableListOf()
) {

    /**
     * Save the community to the database.
     */
    fun save() {
        CommunityRepository.save(this)
    }

    /**
     * Create a new post in the community.
     */
    fun addPost(post: CommunityPost) {
        this.posts.add(post)
        latestPost = post
        save()
    }
}