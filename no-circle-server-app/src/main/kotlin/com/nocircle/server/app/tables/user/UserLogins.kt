package com.nocircle.server.app.tables.user

import com.nocircle.server.common.exposed.NoIntEntity
import com.nocircle.server.common.exposed.NoTable
import kotlinx.datetime.Clock
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.datetime.timestamp
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll

object UserLogins : NoTable("tb_user_login") {
	
	val userId = integer("user_id")
	
	val loginTime = timestamp("login_time")
	
	val method = enumerationByName<Method>("method", 8)
	
	enum class Method { PASSWORD, TOKEN }
	
	fun insertOne(userId: Int, method: Method): Boolean {
		val insert = UserLogins.insert {
			it[this.userId] = userId
			it[this.method] = method
			it[this.loginTime] = Clock.System.now()
		}
		return insert.insertedCount == 1
	}
	
	fun getLastLoginByUserId(userId: Int): UserLogin? {
		val row = UserLogins.selectAll()
			.where { UserLogins.userId eq userId }
			.orderBy(id, SortOrder.DESC)
			.limit(2)
			.drop(1)
			.firstOrNull() ?: return null
		return UserLogin.wrapRow(row)
	}
}

class UserLogin(id: EntityID<Int>) : NoIntEntity(id, UserLogins) {
	
	companion object : IntEntityClass<UserLogin>(UserLogins)
	
	val userId by UserLogins.userId
	
	val loginTime by UserLogins.loginTime
	
	val method by UserLogins.method
}