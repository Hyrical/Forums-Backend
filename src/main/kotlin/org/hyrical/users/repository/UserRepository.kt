package org.hyrical.users.repository

import com.mongodb.client.MongoCollection
import org.hyrical.Mongo
import org.hyrical.users.User
import org.litote.kmongo.eq
import java.util.UUID

object UserRepository {

    val users: MongoCollection<User> = Mongo.database.getCollection("users", User::class.java)

    fun findByEmail(email: String): User? {
        return users.find(User::email eq email).first()
    }

    fun findById(id: UUID): User? {
        return users.find(User::id eq id).first()
    }

    fun findByUsername(username: String): User? {
        return users.find(User::username eq username).first()
    }

}