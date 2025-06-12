package com.nocircle.server.app.tables.friend

import com.nocircle.server.common.exposed.NoIntEntity
import com.nocircle.server.common.exposed.NoTable
import com.nocircle.server.common.exposed.exists
import com.nocircle.server.common.exposed.logicExists
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll

object FriendAddRequests : NoTable("tb_friend_add_request") {
	
	val senderId = integer("sender_id")
	
	val receiverId = integer("receiver_id")
	
	fun insertOne(senderId: Int, receiverId: Int): Boolean {
		val insert = FriendAddRequests.insert {
			it[this.senderId] = senderId
			it[this.receiverId] = receiverId
		}
		return insert.insertedCount == 1
	}
	
	fun isExistsBySenderIdAndReceiverId(senderId: Int, receiverId: Int): Boolean {
		return FriendAddRequests.selectAll()
			.where { FriendAddRequests.senderId eq senderId }
			.andWhere { FriendAddRequests.receiverId eq receiverId }
			.logicExists(FriendAddRequests)
			.exists()
	}
}

class FriendAddRequest(id: EntityID<Int>) : NoIntEntity(id, FriendAddRequests) {
	
	companion object : IntEntityClass<FriendAddRequest>(FriendAddRequests)
	
	val senderId by FriendAddRequests.senderId
	
	val receiverId by FriendAddRequests.receiverId
}