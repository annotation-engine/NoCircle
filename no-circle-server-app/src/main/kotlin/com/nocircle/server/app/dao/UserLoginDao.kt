package com.nocircle.server.app.dao

import com.nocircle.server.app.tables.UserLogin
import com.nocircle.server.app.tables.UserLogins
import com.nocircle.server.common.exposed.wrapRow
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import kotlin.time.Clock

object UserLoginDao {
	
	fun insertOne(userId: Int, method: UserLogins.Method): Boolean {
		val insert = UserLogins.insert {
			it[this.userId] = userId
			it[this.method] = method
			it[this.loginTime] = Clock.System.now()
		}
		return insert.insertedCount == 1
	}
	
	fun getLastLoginByUserId(userId: Int): UserLogin? {
		return UserLogins.selectAll()
			.where { UserLogins.userId eq userId }
			.orderBy(UserLogins.id, SortOrder.DESC)
			.limit(2)
			.drop(1)
			.firstOrNull()
			?.wrapRow(UserLogin)
	}
}