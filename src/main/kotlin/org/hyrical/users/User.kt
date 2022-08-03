package org.hyrical.users

import kotlinx.serialization.Contextual
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
    var createdAt: Long = System.currentTimeMillis()
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