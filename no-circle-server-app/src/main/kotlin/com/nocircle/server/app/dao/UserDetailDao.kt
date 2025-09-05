package com.nocircle.server.app.dao

import com.nocircle.server.app.tables.UserDetail
import com.nocircle.server.app.tables.UserDetails
import com.nocircle.server.common.exposed.logicExists
import com.nocircle.server.common.exposed.wrapRow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.r2dbc.insert
import org.jetbrains.exposed.v1.r2dbc.select
import org.jetbrains.exposed.v1.r2dbc.selectAll

object UserDetailDao {
	
	suspend fun insertOne(userId: Int): Boolean {
		val insert = UserDetails.insert {
			it[this.userId] = userId
		}
		return insert.insertedCount == 1
	}
	
	suspend fun getOneByUserId(userId: Int): UserDetail {
		return UserDetails.selectAll()
			.where { UserDetails.userId eq userId }
			.logicExists(UserDetails)
			.single()
			.wrapRow(UserDetail)
	}
	
	suspend fun getAvatarUrlByUserId(userId: Int): String? {
		return UserDetails.select(UserDetails.avatarUrl)
			.where { UserDetails.userId eq userId }
			.logicExists(UserDetails)
			.single()
			.wrapRow(UserDetail)
			.avatarUrl
	}
	
	suspend fun getAvatarUrlMapByUserIds(userIds: Collection<Int>): Map<Int, String?> {
		return UserDetails.select(UserDetails.userId, UserDetails.avatarUrl)
			.where { UserDetails.userId inList userIds }
			.logicExists(UserDetails)
			.toList()
			.associate { it[UserDetails.userId] to it[UserDetails.avatarUrl] }
	}
}