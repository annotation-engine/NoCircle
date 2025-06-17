package com.nocircle.server.app.dao

import com.nocircle.server.app.tables.FriendRelationships
import com.nocircle.server.common.exposed.exists
import com.nocircle.server.common.exposed.logicExists
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.orWhere
import org.jetbrains.exposed.v1.jdbc.select

object FriendRelationshipDao {
	
	fun insertOne(senderId: Int, receiverId: Int): Boolean {
		val insert = FriendRelationships.insert {
			it[this.senderId] = senderId
			it[this.receiverId] = receiverId
		}
		return insert.insertedCount == 1
	}
	
	fun isFriend(senderId: Int, receiverId: Int): Boolean {
		return FriendRelationships.select(FriendRelationships.id)
			.where { (FriendRelationships.senderId eq senderId) and (FriendRelationships.receiverId eq receiverId) }
			.orWhere { (FriendRelationships.senderId eq receiverId) and (FriendRelationships.receiverId eq senderId) }
			.logicExists(FriendRelationships)
			.exists()
	}
}