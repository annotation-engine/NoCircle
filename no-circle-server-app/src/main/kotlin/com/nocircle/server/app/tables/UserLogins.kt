package com.nocircle.server.app.tables

import com.nocircle.server.common.exposed.NoEntity
import com.nocircle.server.common.exposed.NoEntityClass
import com.nocircle.server.common.exposed.NoTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.datetime.timestamp
import kotlin.time.ExperimentalTime

object UserLogins : NoTable("user_login") {
	
	val userId = integer("user_id")
		.references(Users.id)
	
	@OptIn(ExperimentalTime::class)
	val loginTime = timestamp("login_time")
	
	val method = enumerationByName<Method>("method", 8)
	
	enum class Method { PASSWORD, TOKEN }
}

class UserLogin(id: EntityID<Int>) : NoEntity(id) {
	
	companion object : NoEntityClass<UserLogin>(UserLogins, ::UserLogin)
	
	val userId by UserLogins.userId
	
	@OptIn(ExperimentalTime::class)
	val loginTime by UserLogins.loginTime
	
	val method by UserLogins.method
}