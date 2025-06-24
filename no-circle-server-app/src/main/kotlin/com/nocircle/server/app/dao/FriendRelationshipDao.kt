package com.nocircle.server.app.dao

import com.nocircle.server.app.tables.FriendRelationship
import com.nocircle.server.app.tables.FriendRelationships
import com.nocircle.server.app.tables.Users
import com.nocircle.server.common.exposed.exists
import com.nocircle.server.common.exposed.isLogicExists
import com.nocircle.server.common.exposed.logicExists
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.innerJoin
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.jdbc.*

object FriendRelationshipDao {
	
	fun insertOne(userId: Int, friendId: Int): Boolean {
		val insert = FriendRelationships.insert {
			it[this.userId] = userId
			it[this.friendId] = friendId
		}
		return insert.insertedCount == 1
	}
	
	fun isFriend(userId: Int, friendId: Int): Boolean {
		return FriendRelationships.select(FriendRelationships.id)
			.where { (FriendRelationships.userId eq userId) and (FriendRelationships.friendId eq friendId) }
			.logicExists(FriendRelationships)
			.exists()
	}
	
	fun getOneByUserIdAndFriendId(userId: Int, friendId: Int): FriendRelationship? {
		val resultRow = FriendRelationships.selectAll()
			.where { FriendRelationships.userId eq userId }
			.andWhere { FriendRelationships.friendId eq friendId }
			.logicExists(FriendRelationships)
			.singleOrNull() ?: return null
		return FriendRelationship.wrapRow(resultRow)
	}
	
	fun getFriendsByUserId(userId: Int): SizedIterable<Friend> {
		val query = FriendRelationships.innerJoin(
			otherTable = Users,
			onColumn = { this.friendId },
			otherColumn = { this.id },
			additionalConstraint = { isLogicExists(FriendRelationships, Users) }
		)
			.selectAll()
			.where { FriendRelationships.userId eq userId }
		return Friend.wrapRows(query)
	}
}

class Friend(id: EntityID<Int>) : IntEntity(id) {
	
	companion object : IntEntityClass<Friend>(Users)
	
	val friendId by Users.id
	
	val username by Users.username
	
	val nickname by Users.nickname
	
	val avatarUrl by Users.avatarUrl
	
	val pinyin by Users.pinyin
}