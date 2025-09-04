package com.nocircle.server.app.dao

import com.nocircle.server.app.tables.FriendVersions
import com.nocircle.server.common.exposed.logicExists
import com.nocircle.server.common.exposed.logicUpdate
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select

object FriendVersionDao {
	
	fun getVersionByUserId(userId: Int): Int? {
		val resultRow = FriendVersions.select(FriendVersions.version)
			.where { FriendVersions.userId eq userId }
			.logicExists(FriendVersions)
			.singleOrNull() ?: return null
		return resultRow[FriendVersions.version]
	}
	
	fun insertOne(userId: Int): Boolean {
		val insert = FriendVersions.insert {
			it[this.userId] = userId
			it[this.version] = 1
		}
		return insert.insertedCount == 1
	}
	
	fun update(userId: Int, version: Int): Boolean {
		val count = FriendVersions.logicUpdate(
			where = { FriendVersions.userId eq userId },
		) {
			it[this.version] = version
		}
		return count == 1
	}
}