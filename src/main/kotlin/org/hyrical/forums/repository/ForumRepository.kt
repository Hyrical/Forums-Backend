package org.hyrical.forums.repository

import com.mongodb.client.MongoCollection
import org.hyrical.Mongo
import org.hyrical.forums.Forum
import org.hyrical.users.User
import org.litote.kmongo.eq

object ForumRepository {
    val forums: MongoCollection<Forum> = Mongo.database.getCollection("forums", Forum::class.java)

    fun getAll(): List<Forum> {
        return forums.find().toList()
    }

    fun byId(id: String): Forum? {
        return forums.find(Forum::id eq id).first()
    }
}