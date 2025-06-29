package com.nocircle.server.app.dao

import com.nocircle.server.app.tables.UserDetail
import com.nocircle.server.app.tables.UserDetails
import com.nocircle.server.common.exposed.logicExists
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll

object UserDetailDao {
	
	fun insertOne(userId: Int): Boolean {
		val insert = UserDetails.insert {
			it[this.userId] = userId
		}
		return insert.insertedCount == 1
	}
	
	fun getOneByUserId(userId: Int): UserDetail {
		val resultRow = UserDetails.selectAll()
			.where { UserDetails.userId eq userId }
			.logicExists(UserDetails)
			.single()
		return UserDetail.wrapRow(resultRow)
	}
	
	fun getAvatarUrlByUserId(userId: Int): String? {
		val resultRow = UserDetails.select(UserDetails.avatarUrl)
			.where { UserDetails.userId eq userId }
			.logicExists(UserDetails)
			.single()
		return UserDetail.wrapRow(resultRow).avatarUrl
	}
	
	fun getAvatarUrlMapByUserIds(userIds: Collection<Int>): Map<Int, String?> {
		return UserDetails.select(UserDetails.userId, UserDetails.avatarUrl)
			.where { UserDetails.userId inList userIds }
			.logicExists(UserDetails)
			.associate { it[UserDetails.userId] to it[UserDetails.avatarUrl] }
	}
}