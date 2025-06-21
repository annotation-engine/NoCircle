package com.nocircle.server.app.dao

import com.nocircle.server.app.tables.FriendRequest
import com.nocircle.server.app.tables.FriendRequests
import com.nocircle.server.common.exposed.exists
import com.nocircle.server.common.exposed.logicDeleteWhere
import com.nocircle.server.common.exposed.logicExists
import com.nocircle.server.common.exposed.logicUpdate
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.SqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll

object FriendRequestDao {
	
	fun insertOne(senderId: Int, receiverId: Int): Boolean {
		val insert = FriendRequests.insert {
			it[this.senderId] = senderId
			it[this.receiverId] = receiverId
			it[this.status] = FriendRequests.Status.PENDING
		}
		return insert.insertedCount == 1
	}
	
	fun updateOne(id: Int, senderId: Int, receiverId: Int, status: FriendRequests.Status): Boolean {
		val updateCount = FriendRequests.logicUpdate(
			where = {
				(FriendRequests.id eq id) and
						(FriendRequests.senderId eq senderId) and
						(FriendRequests.receiverId eq receiverId)
			}
		) {
			it[this.status] = status
		}
		return updateCount == 1
	}
	
	fun getSentRequests(senderId: Int): List<FriendRequest> {
		val query = FriendRequests.selectAll()
			.where { FriendRequests.senderId eq senderId }
			.logicExists(FriendRequests)
			.orderBy(FriendRequests.createTime, SortOrder.DESC)
		return FriendRequest.wrapRows(query).toList()
	}
	
	fun getReceivedRequests(receiverId: Int): List<FriendRequest> {
		val query = FriendRequests.selectAll()
			.where { FriendRequests.receiverId eq receiverId }
			.andWhere { FriendRequests.status eq FriendRequests.Status.PENDING }
			.logicExists(FriendRequests)
			.orderBy(FriendRequests.createTime, SortOrder.DESC)
		return FriendRequest.wrapRows(query).toList()
	}
	
	fun getReceivedPendingRequestCount(receiverId: Int): Int {
		return FriendRequests.select(FriendRequests.id)
			.where { FriendRequests.receiverId eq receiverId }
			.andWhere { FriendRequests.status eq FriendRequests.Status.PENDING }
			.logicExists(FriendRequests)
			.count()
			.toInt()
	}
	
	fun isAlreadySend(senderId: Int, receiverId: Int): Boolean {
		return FriendRequests.select(FriendRequests.id)
			.where { FriendRequests.senderId eq senderId }
			.andWhere { FriendRequests.receiverId eq receiverId }
			.andWhere { FriendRequests.status eq FriendRequests.Status.PENDING }
			.logicExists(FriendRequests)
			.exists()
	}
	
	fun getOneBySenderIdAndReceiverId(senderId: Int, receiverId: Int): FriendRequest? {
		val resultRow = FriendRequests.selectAll()
			.where { FriendRequests.senderId eq senderId }
			.andWhere { FriendRequests.receiverId eq receiverId }
			.logicExists(FriendRequests)
			.singleOrNull() ?: return null
		return FriendRequest.wrapRow(resultRow)
	}
	
	fun deleteOne(senderId: Int, receiverId: Int): Boolean {
		val deleteCount = FriendRequests.logicDeleteWhere {
			(FriendRequests.senderId eq senderId) and (FriendRequests.receiverId eq receiverId)
		}
		return deleteCount >= 1
	}
}