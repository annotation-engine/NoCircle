package com.nocircle.server.app.tables

import com.nocircle.server.common.exposed.NoIntEntity
import com.nocircle.server.common.exposed.NoTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntityClass

object FriendRelationships : NoTable("tb_friend_relationship") {
	
	val senderId = integer("sender_id")
		.references(Users.id)
	
	val receiverId = integer("receiver_id")
		.references(Users.id)
}

class FriendRelationship(id: EntityID<Int>) : NoIntEntity(id, FriendRelationships) {
	
	companion object : IntEntityClass<FriendRelationship>(FriendRelationships)
	
	val senderId by FriendRelationships.senderId
	
	val receiverId by FriendRelationships.receiverId
}