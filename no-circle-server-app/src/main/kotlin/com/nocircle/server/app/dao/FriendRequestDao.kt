package com.nocircle.server.app.dao

import com.nocircle.server.app.tables.FriendRequest
import com.nocircle.server.app.tables.FriendRequests
import com.nocircle.server.common.exposed.exists
import com.nocircle.server.common.exposed.logicExists
import com.nocircle.server.common.exposed.logicUpdate
import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.SqlExpressionBuilder
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.jdbc.Query
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll

object FriendRequestDao {
	
	fun insertOne(senderId: Int, receiverId: Int): Boolean {
		val insert = FriendRequests.insert {
			it[this.senderId] = senderId
			it[this.receiverId] = receiverId
			it[this.status] = FriendRequests.Status.WAITING
		}
		return insert.insertedCount == 1
	}
	
	fun updateOne(senderId: Int, receiverId: Int, isAgree: Boolean): Boolean {
		val updateCount = FriendRequests.logicUpdate(
			where = { (FriendRequests.senderId eq senderId) and (FriendRequests.receiverId eq receiverId) }
		) {
			it[this.status] = if (isAgree) FriendRequests.Status.AGREED else FriendRequests.Status.REJECTED
		}
		return updateCount == 1
	}
	
	fun getListBySenderId(senderId: Int): List<FriendRequest> {
		val query = FriendRequests.selectAll()
			.where { FriendRequests.senderId eq senderId }
			.logicExists(FriendRequests)
		return FriendRequest.wrapRows(query).toList()
	}
	
	fun getListByReceiverId(receiverId: Int): List<FriendRequest> {
		val query = FriendRequests.selectAll()
			.where { FriendRequests.receiverId eq receiverId }
			.logicExists(FriendRequests)
		return FriendRequest.wrapRows(query).toList()
	}
	
	fun isExistsBySenderIdAndReceiverId(senderId: Int, receiverId: Int): Boolean {
		return FriendRequests.select(FriendRequests.id)
			.where { FriendRequests.senderId eq senderId }
			.andWhere { FriendRequests.receiverId eq receiverId }
			.logicExists(FriendRequests)
			.exists()
	}
}

private fun Query.andWhere(andPart: (SqlExpressionBuilder) -> Op<Boolean>) {}
