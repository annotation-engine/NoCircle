package com.nocircle.server.app.tables

import com.nocircle.server.common.exposed.NoTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import kotlin.time.ExperimentalTime

object FriendRelationships : NoTable("friend_relationship") {
	
	val userId = integer("user_id")
		.references(Users.id)
	
	val friendId = integer("friend_id")
		.references(Users.id)
}

class FriendRelationship(id: EntityID<Int>) : IntEntity(id) {
	
	companion object : IntEntityClass<FriendRelationship>(FriendRelationships)
	
	val userId by FriendRelationships.userId
	
	val friendId by FriendRelationships.friendId
	
	@OptIn(ExperimentalTime::class)
	val createTime by FriendRelationships.createTime
}