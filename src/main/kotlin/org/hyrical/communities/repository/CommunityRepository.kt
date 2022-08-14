package org.hyrical.communities.repository

import org.hyrical.Mongo
import org.hyrical.communities.Community
import org.litote.kmongo.*

object CommunityRepository {

    val communities = Mongo.database.getCollection<Community>("communities")

    fun searchById(id: String): Community? = communities.findOneById(id)

    fun searchByName(name: String): Community? = communities.findOne(Community::name eq name)

    fun save(community: Community) = communities.save(community)
}