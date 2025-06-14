package com.nocircle.server.app.tables.friend

import com.nocircle.server.common.exposed.*
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll

object FriendAddRequests : NoTable("tb_friend_add_request") {
	
	val senderId = integer("sender_id")
	
	val receiverId = integer("receiver_id")
	
	val status = enumerationByName<Status>("status", 7)
	
	enum class Status {
		AGREE,
		REJECT,
		WAITING
	}
	
	
	fun insertOne(senderId: Int, receiverId: Int): Boolean {
		val insert = FriendAddRequests.insert {
			it[this.senderId] = senderId
			it[this.receiverId] = receiverId
			it[this.status] = Status.WAITING
		}
		return insert.insertedCount == 1
	}
	
	fun updateOne(senderId: Int, receiverId: Int, isAgree: Boolean): Boolean {
		val updateCount = FriendAddRequests.logicUpdate(
			where = { (FriendAddRequests.senderId eq senderId) and (FriendAddRequests.receiverId eq receiverId) }
		) {
			it[this.status] = if (isAgree) Status.AGREE else Status.REJECT
		}
		return updateCount == 1
	}
	
	fun getListBySenderId(senderId: Int): List<FriendAddRequest> {
		val query = FriendAddRequests.selectAll()
			.where { FriendAddRequests.senderId eq senderId }
			.logicExists(FriendAddRequests)
		return FriendAddRequest.wrapRows(query).toList()
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