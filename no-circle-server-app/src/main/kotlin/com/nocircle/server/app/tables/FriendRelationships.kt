package com.nocircle.server.app.tables

import com.nocircle.server.common.exposed.NoIntEntity
import com.nocircle.server.common.exposed.NoTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntityClass

object FriendRelationships : NoTable("tb_friend_relationship") {
	
	val userId = integer("user_id")
		.references(Users.id)
	
	val friendId = integer("friend_id")
		.references(Users.id)
	
	val pinyin = varchar("pinyin", 100)
}

class FriendRelationship(id: EntityID<Int>) : NoIntEntity<FriendRelationships>(id, FriendRelationships) {
	
	companion object : IntEntityClass<FriendRelationship>(FriendRelationships)
	
	val userId by table.userId
	
	val friendId by table.friendId
	
	val pinyin by table.pinyin
}