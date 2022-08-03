package org.hyrical

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import org.bson.UuidRepresentation
import org.litote.kmongo.KMongo

object Mongo {

    private val client: MongoClientSettings = MongoClientSettings.builder()
        .applyConnectionString(ConnectionString("mongodb://localhost:27017"))
        .uuidRepresentation(UuidRepresentation.STANDARD)
        .build()

    private val mongoClient: MongoClient = KMongo.createClient(client)
    val database: MongoDatabase = mongoClient.getDatabase("Forums")
}