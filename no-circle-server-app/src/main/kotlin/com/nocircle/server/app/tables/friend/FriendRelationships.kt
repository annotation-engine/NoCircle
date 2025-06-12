package com.nocircle.server.app.tables.friend

import com.nocircle.server.common.exposed.NoIntEntity
import com.nocircle.server.common.exposed.NoTable
import com.nocircle.server.common.exposed.exists
import com.nocircle.server.common.exposed.logicExists
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.jdbc.orWhere
import org.jetbrains.exposed.v1.jdbc.selectAll

object FriendRelationships : NoTable("tb_friend_relationship") {
	
	val senderId = integer("sender_id")
	
	val receiverId = integer("receiver_id")
	
	fun isFriend(senderId: Int, receiverId: Int): Boolean {
		return FriendRelationships.selectAll()
			.where { (FriendRelationships.senderId eq senderId) and (FriendRelationships.receiverId eq receiverId) }
			.orWhere { (FriendRelationships.senderId eq receiverId) and (FriendRelationships.receiverId eq senderId) }
			.logicExists(FriendRelationships)
			.exists()
	}
}

class FriendRelationship(id: EntityID<Int>) : NoIntEntity(id, FriendRelationships) {
	
	companion object : IntEntityClass<FriendRelationship>(FriendRelationships)
	
	val senderId by FriendRelationships.senderId
	
	val receiverId by FriendRelationships.receiverId
}