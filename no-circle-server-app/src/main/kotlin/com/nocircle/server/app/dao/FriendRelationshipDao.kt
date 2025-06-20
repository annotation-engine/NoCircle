package com.nocircle.server.app.dao

import com.nocircle.server.app.tables.FriendRelationship
import com.nocircle.server.app.tables.FriendRelationships
import com.nocircle.server.common.exposed.exists
import com.nocircle.server.common.exposed.logicExists
import com.nocircle.server.common.exposed.page
import org.jetbrains.exposed.v1.core.Expression
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.lowerCase
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll

object FriendRelationshipDao {
	
	fun insertOne(userId: Int, friendId: Int, pinyin: String): Boolean {
		val insert = FriendRelationships.insert {
			it[this.userId] = userId
			it[this.friendId] = friendId
			it[this.pinyin] = pinyin
		}
		return insert.insertedCount == 1
	}
	
	fun isFriend(userId: Int, friendId: Int): Boolean {
		return FriendRelationships.select(FriendRelationships.id)
			.where { (FriendRelationships.userId eq userId) and (FriendRelationships.friendId eq friendId) }
			.logicExists(FriendRelationships)
			.exists()
	}
	
	fun getListByUserIdAndPageAndSize(userId: Int, pageNumber: Int, pageSize: Int, orderType: OrderType): List<FriendRelationship> {
		val query = FriendRelationships.selectAll()
			.where { FriendRelationships.userId eq userId }
			.logicExists(FriendRelationships)
			.orderBy(orderType.column, orderType.order)
			.page(pageNumber, pageSize)
		return FriendRelationship.wrapRows(query).toList()
	}
	
	fun getCountByUserId(userId: Int): Int {
		return FriendRelationships.select(FriendRelationships.id)
			.where { FriendRelationships.userId eq userId }
			.logicExists(FriendRelationships)
			.count()
			.toInt()
	}
	
	@Suppress("unused")
	enum class OrderType(
		val column: Expression<*>,
		val order: SortOrder
	) {
		PINYIN_ASC(FriendRelationships.pinyin.lowerCase(), SortOrder.ASC),
		PINYIN_DESC(FriendRelationships.pinyin.lowerCase(), SortOrder.DESC),
		CREATE_TIME_ASC(FriendRelationships.createTime, SortOrder.ASC),
		CREATE_TIME_DESC(FriendRelationships.createTime, SortOrder.DESC),
	}
}